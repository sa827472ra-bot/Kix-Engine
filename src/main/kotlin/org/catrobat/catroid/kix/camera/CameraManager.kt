/*
 * Kix Engine Mod - Camera Management System
 * Copyright (C) 2026 Kix Engine Contributors
 * Licensed under the GNU Affero General Public License v3.0
 */

package org.catrobat.catroid.kix.camera

import org.catrobat.catroid.content.Sprite
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.cos
import kotlin.math.sin

/**
 * Central camera controller for the Kix Engine.
 * Manages position, zoom, rotation, shake, fade, bounds and follow target.
 */
object CameraManager {

    data class CameraState(
        var x: Float = 0f,
        var y: Float = 0f,
        var zoom: Float = 1f,
        var rotationDeg: Float = 0f,
        var alpha: Float = 1f,          // 1 = fully visible, 0 = faded out
        var followTarget: Sprite? = null,
        var boundsEnabled: Boolean = false,
        var boundLeft: Float = -Float.MAX_VALUE,
        var boundRight: Float = Float.MAX_VALUE,
        var boundBottom: Float = -Float.MAX_VALUE,
        var boundTop: Float = Float.MAX_VALUE,
        var shakeIntensity: Float = 0f,
        var shakeRemainingMs: Long = 0L,
        var lerpSpeed: Float = 0.1f,
        var isometricEnabled: Boolean = false,
        var depthOfFieldFocus: Float = 0f,
        var depthOfFieldRange: Float = 100f
    )

    private val state = AtomicReference(CameraState())

    fun getState(): CameraState = state.get()

    // -------------------------------------------------------------------------
    // Follow / Fixed position
    // -------------------------------------------------------------------------

    fun followPlayer(target: Sprite?) {
        state.get().followTarget = target
    }

    fun setFixedPosition(x: Float, y: Float) {
        val s = state.get()
        s.followTarget = null
        s.x = x
        s.y = y
        applyBounds(s)
    }

    // -------------------------------------------------------------------------
    // Zoom / Rotate
    // -------------------------------------------------------------------------

    fun setZoom(factor: Float) {
        state.get().zoom = factor.coerceIn(0.05f, 20f)
    }

    fun setRotation(degrees: Float) {
        state.get().rotationDeg = degrees % 360f
    }

    // -------------------------------------------------------------------------
    // Shake
    // -------------------------------------------------------------------------

    fun shake(intensity: Float, durationMs: Long) {
        val s = state.get()
        s.shakeIntensity = intensity.coerceAtLeast(0f)
        s.shakeRemainingMs = durationMs.coerceAtLeast(0L)
    }

    // -------------------------------------------------------------------------
    // Fade
    // -------------------------------------------------------------------------

    fun fadeIn(durationMs: Long = 300L) {
        // Instant for now; real implementation would tween over frames
        state.get().alpha = 1f
    }

    fun fadeOut(durationMs: Long = 300L) {
        state.get().alpha = 0f
    }

    fun setAlpha(alpha: Float) {
        state.get().alpha = alpha.coerceIn(0f, 1f)
    }

    // -------------------------------------------------------------------------
    // Bounds
    // -------------------------------------------------------------------------

    fun setBounds(left: Float, right: Float, bottom: Float, top: Float) {
        val s = state.get()
        s.boundsEnabled = true
        s.boundLeft = left
        s.boundRight = right
        s.boundBottom = bottom
        s.boundTop = top
        applyBounds(s)
    }

    fun clearBounds() {
        state.get().boundsEnabled = false
    }

    // -------------------------------------------------------------------------
    // Lerp / smooth follow
    // -------------------------------------------------------------------------

    fun setLerpSpeed(speed: Float) {
        state.get().lerpSpeed = speed.coerceIn(0.01f, 1f)
    }

    // -------------------------------------------------------------------------
    // Advanced modes
    // -------------------------------------------------------------------------

    fun setIsometric(enabled: Boolean) {
        state.get().isometricEnabled = enabled
    }

    fun setDepthOfField(focus: Float, range: Float) {
        val s = state.get()
        s.depthOfFieldFocus = focus
        s.depthOfFieldRange = range.coerceAtLeast(1f)
    }

    fun cinematicCut(x: Float, y: Float, zoom: Float, rotation: Float) {
        val s = state.get()
        s.followTarget = null
        s.x = x
        s.y = y
        s.zoom = zoom.coerceIn(0.05f, 20f)
        s.rotationDeg = rotation % 360f
        applyBounds(s)
    }

    // -------------------------------------------------------------------------
    // Per-frame update (call from stage/render loop)
    // -------------------------------------------------------------------------

    fun update(deltaMs: Long) {
        val s = state.get()

        // Follow target with optional lerp
        s.followTarget?.let { target ->
            // Assumes Sprite exposes x/y; adjust to actual Catroid API
            val targetX = target.look?.xInUserInterfaceDimensionUnit ?: 0f
            val targetY = target.look?.yInUserInterfaceDimensionUnit ?: 0f
            s.x += (targetX - s.x) * s.lerpSpeed
            s.y += (targetY - s.y) * s.lerpSpeed
            applyBounds(s)
        }

        // Shake decay
        if (s.shakeRemainingMs > 0) {
            s.shakeRemainingMs = (s.shakeRemainingMs - deltaMs).coerceAtLeast(0)
            if (s.shakeRemainingMs == 0L) {
                s.shakeIntensity = 0f
            }
        }
    }

    /** Returns current camera offset including shake for rendering. */
    fun getRenderOffset(): Pair<Float, Float> {
        val s = state.get()
        if (s.shakeIntensity <= 0f || s.shakeRemainingMs <= 0) {
            return s.x to s.y
        }
        val angle = (System.currentTimeMillis() % 360).toFloat()
        val ox = cos(Math.toRadians(angle.toDouble())).toFloat() * s.shakeIntensity
        val oy = sin(Math.toRadians(angle.toDouble())).toFloat() * s.shakeIntensity
        return (s.x + ox) to (s.y + oy)
    }

    fun reset() {
        state.set(CameraState())
    }

    private fun applyBounds(s: CameraState) {
        if (!s.boundsEnabled) return
        s.x = s.x.coerceIn(s.boundLeft, s.boundRight)
        s.y = s.y.coerceIn(s.boundBottom, s.boundTop)
    }
}

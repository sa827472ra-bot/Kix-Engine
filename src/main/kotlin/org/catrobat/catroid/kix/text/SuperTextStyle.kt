/*
 * Kix Engine - SuperText style model + animations
 * Copyright (C) 2026 Kix Engine Contributors | AGPL-3.0
 */
package org.catrobat.catroid.kix.text

/**
 * Immutable-ish style snapshot for SuperText rendering.
 * Host renderer reads this each frame; animations mutate a live copy via [SuperTextAnimator].
 */
data class SuperTextStyle(
    var text: String = "",
    var solidColor: Int = 0xFFFFFFFF.toInt(),
    var gradientStart: Int? = null,
    var gradientEnd: Int? = null,
    var gradientVertical: Boolean = false,
    var outlineColor: Int = 0x00000000,
    var outlineWidth: Float = 0f,
    var fontFamily: String = "sans-serif",
    var fontSize: Float = 32f,
    var alignment: Align = Align.LEFT,
    var rotationDeg: Float = 0f,
    var scaleX: Float = 1f,
    var scaleY: Float = 1f,
    var alpha: Float = 1f,
    var x: Float = 0f,
    var y: Float = 0f
) {
    enum class Align { LEFT, CENTER, RIGHT }

    fun copyStyle(): SuperTextStyle = copy()
}

enum class SuperTextAnimation {
    FADE, SLIDE, TYPEWRITER, BOUNCE, ROTATE, PULSE, SHAKE, RAINBOW, GLITCH, WAVE
}

/**
 * Lightweight animator. Call [update] with deltaMs from the stage loop.
 * Returns false when the animation has finished (non-looping).
 */
class SuperTextAnimator(
    val style: SuperTextStyle,
    private val animation: SuperTextAnimation,
    private val durationMs: Long = 1000L,
    private val loop: Boolean = false
) {
    private var elapsed = 0L
    private val base = style.copyStyle()
    private var typewriterIndex = 0
    private val fullText = base.text

    fun update(deltaMs: Long): Boolean {
        elapsed += deltaMs
        val t = if (durationMs <= 0) 1f else (elapsed.toFloat() / durationMs).coerceIn(0f, 1f)

        when (animation) {
            SuperTextAnimation.FADE -> style.alpha = base.alpha * t
            SuperTextAnimation.SLIDE -> {
                style.x = base.x + (1f - t) * -80f
                style.alpha = base.alpha * t
            }
            SuperTextAnimation.TYPEWRITER -> {
                val chars = (fullText.length * t).toInt().coerceIn(0, fullText.length)
                if (chars != typewriterIndex) {
                    typewriterIndex = chars
                    style.text = fullText.substring(0, chars)
                }
            }
            SuperTextAnimation.BOUNCE -> {
                val bounce = kotlin.math.sin(t * Math.PI).toFloat()
                style.y = base.y - bounce * 40f
                style.scaleY = base.scaleY * (1f + bounce * 0.15f)
            }
            SuperTextAnimation.ROTATE -> style.rotationDeg = base.rotationDeg + t * 360f
            SuperTextAnimation.PULSE -> {
                val p = 1f + 0.2f * kotlin.math.sin(t * Math.PI * 2).toFloat()
                style.scaleX = base.scaleX * p
                style.scaleY = base.scaleY * p
            }
            SuperTextAnimation.SHAKE -> {
                val s = (1f - t) * 8f
                style.x = base.x + (Math.random().toFloat() - 0.5f) * 2f * s
                style.y = base.y + (Math.random().toFloat() - 0.5f) * 2f * s
            }
            SuperTextAnimation.RAINBOW -> {
                val hue = (t * 360f) % 360f
                style.solidColor = hsvToColor(hue, 0.85f, 1f)
                style.alpha = base.alpha
            }
            SuperTextAnimation.GLITCH -> {
                if ((elapsed / 40) % 3 == 0L) {
                    style.x = base.x + (Math.random().toFloat() - 0.5f) * 12f
                    style.solidColor = if (Math.random() > 0.5) 0xFFFF00FF.toInt() else base.solidColor
                } else {
                    style.x = base.x
                    style.solidColor = base.solidColor
                }
            }
            SuperTextAnimation.WAVE -> {
                style.y = base.y + kotlin.math.sin(t * Math.PI * 4).toFloat() * 12f
            }
        }

        if (t >= 1f) {
            if (loop) {
                elapsed = 0L
                style.text = if (animation == SuperTextAnimation.TYPEWRITER) "" else fullText
                typewriterIndex = 0
                return true
            }
            // settle on base
            style.alpha = base.alpha
            style.x = base.x
            style.y = base.y
            style.scaleX = base.scaleX
            style.scaleY = base.scaleY
            style.rotationDeg = base.rotationDeg
            style.text = fullText
            return false
        }
        return true
    }

    companion object {
        fun hsvToColor(h: Float, s: Float, v: Float): Int {
            val c = v * s
            val x = c * (1 - kotlin.math.abs((h / 60f) % 2 - 1))
            val m = v - c
            val (r1, g1, b1) = when {
                h < 60 -> Triple(c, x, 0f)
                h < 120 -> Triple(x, c, 0f)
                h < 180 -> Triple(0f, c, x)
                h < 240 -> Triple(0f, x, c)
                h < 300 -> Triple(x, 0f, c)
                else -> Triple(c, 0f, x)
            }
            val r = ((r1 + m) * 255).toInt().coerceIn(0, 255)
            val g = ((g1 + m) * 255).toInt().coerceIn(0, 255)
            val b = ((b1 + m) * 255).toInt().coerceIn(0, 255)
            return (0xFF shl 24) or (r shl 16) or (g shl 8) or b
        }
    }
}

/*
 * Kix Engine - Joystick Manager
 * Copyright (C) 2026 Kix Engine Contributors | AGPL-3.0
 */
package org.catrobat.catroid.kix.joystick

import java.util.concurrent.atomic.AtomicReference
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min

/**
 * Virtual joystick state for D-Pad, Analog and Dual-stick modes.
 * Values are normalized to [-1, 1].
 */
object JoystickManager {

    enum class Mode { OFF, DPAD, ANALOG, DUAL }

    data class StickState(
        var x: Float = 0f,
        var y: Float = 0f,
        var active: Boolean = false
    )

    data class State(
        var mode: Mode = Mode.OFF,
        val left: StickState = StickState(),
        val right: StickState = StickState(), // used in DUAL
        var dpadUp: Boolean = false,
        var dpadDown: Boolean = false,
        var dpadLeft: Boolean = false,
        var dpadRight: Boolean = false
    )

    private val state = AtomicReference(State())

    fun getState(): State = state.get()

    fun setMode(mode: Mode) {
        val s = state.get()
        s.mode = mode
        if (mode == Mode.OFF) resetAxes()
    }

    fun enableDPad(enable: Boolean) {
        setMode(if (enable) Mode.DPAD else Mode.OFF)
    }

    fun enableAnalog(enable: Boolean) {
        setMode(if (enable) Mode.ANALOG else Mode.OFF)
    }

    fun enableDualStick(enable: Boolean) {
        setMode(if (enable) Mode.DUAL else Mode.OFF)
    }

    /** Primary stick input (or left stick in DUAL). */
    fun setLeftStick(x: Float, y: Float, active: Boolean = true) {
        val s = state.get()
        s.left.x = clampAxis(x)
        s.left.y = clampAxis(y)
        s.left.active = active
        if (s.mode == Mode.DPAD) syncDpadFromAnalog(s.left)
    }

    fun setRightStick(x: Float, y: Float, active: Boolean = true) {
        val s = state.get()
        s.right.x = clampAxis(x)
        s.right.y = clampAxis(y)
        s.right.active = active
    }

    fun setDpad(up: Boolean, down: Boolean, left: Boolean, right: Boolean) {
        val s = state.get()
        s.dpadUp = up
        s.dpadDown = down
        s.dpadLeft = left
        s.dpadRight = right
        // Map to left stick for unified getInput()
        s.left.x = when {
            left && !right -> -1f
            right && !left -> 1f
            else -> 0f
        }
        s.left.y = when {
            down && !up -> -1f
            up && !down -> 1f
            else -> 0f
        }
        s.left.active = up || down || left || right
    }

    /** Returns primary (left) stick normalized input. */
    fun getInput(): Pair<Float, Float> {
        val s = state.get()
        if (s.mode == Mode.OFF) return 0f to 0f
        return s.left.x to s.left.y
    }

    fun getRightInput(): Pair<Float, Float> {
        val s = state.get()
        if (s.mode != Mode.DUAL) return 0f to 0f
        return s.right.x to s.right.y
    }

    fun getMagnitude(): Float {
        val (x, y) = getInput()
        return min(1f, hypot(x, y))
    }

    fun resetAxes() {
        val s = state.get()
        s.left.x = 0f; s.left.y = 0f; s.left.active = false
        s.right.x = 0f; s.right.y = 0f; s.right.active = false
        s.dpadUp = false; s.dpadDown = false
        s.dpadLeft = false; s.dpadRight = false
    }

    fun reset() {
        state.set(State())
    }

    private fun clampAxis(v: Float): Float = max(-1f, min(1f, v))

    private fun syncDpadFromAnalog(stick: StickState) {
        val s = state.get()
        val dead = 0.4f
        s.dpadLeft = stick.x < -dead
        s.dpadRight = stick.x > dead
        s.dpadDown = stick.y < -dead
        s.dpadUp = stick.y > dead
    }
}

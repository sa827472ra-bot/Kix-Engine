/*
 * Kix Engine - Joystick Bricks (4)
 * Copyright (C) 2026 Kix Engine Contributors | AGPL-3.0
 */
package org.catrobat.catroid.kix.bricks.joystick

import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.content.actions.ScriptSequenceAction
import org.catrobat.catroid.content.bricks.Brick
import org.catrobat.catroid.content.bricks.FormulaBrick
import org.catrobat.catroid.kix.actions.KixPresetActions

class JoystickDPadBrick : FormulaBrick {
    private var enable: Boolean = true
    constructor() : super()
    constructor(enable: Boolean) : this() { this.enable = enable }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        KixPresetActions.joystickDPadEnable(enable)
    }
    override fun clone(): Brick = JoystickDPadBrick(enable)
}

class JoystickAnalogBrick : FormulaBrick {
    private var enable: Boolean = true
    constructor() : super()
    constructor(enable: Boolean) : this() { this.enable = enable }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        KixPresetActions.joystickAnalogEnable(enable)
    }
    override fun clone(): Brick = JoystickAnalogBrick(enable)
}

class JoystickDualStickBrick : FormulaBrick {
    private var enable: Boolean = true
    constructor() : super()
    constructor(enable: Boolean) : this() { this.enable = enable }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        KixPresetActions.joystickDualStickEnable(enable)
    }
    override fun clone(): Brick = JoystickDualStickBrick(enable)
}

/** Reads joystick input; host can bind result to variables via ActionFactory. */
class GetJoystickInputBrick : FormulaBrick {
    constructor() : super()
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        KixPresetActions.getJoystickInput()
    }
    fun evaluate(): Pair<Float, Float> = KixPresetActions.getJoystickInput()
    override fun clone(): Brick = GetJoystickInputBrick()
}

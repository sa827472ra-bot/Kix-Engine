/*
 * Kix Engine Mod - Advanced Camera Bricks
 * CameraLerpBrick, CameraDepthOfFieldBrick, CameraIsometricBrick, CameraCinematicCutBrick
 * Copyright (C) 2026 Kix Engine Contributors
 * Licensed under AGPL-3.0
 */
package org.catrobat.catroid.kix.bricks.camera

import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.content.actions.ScriptSequenceAction
import org.catrobat.catroid.content.bricks.Brick
import org.catrobat.catroid.content.bricks.FormulaBrick
import org.catrobat.catroid.formulaeditor.Formula
import org.catrobat.catroid.kix.actions.KixPresetActions

/** Sets smooth follow (lerp) speed. 0.01 = slow, 1.0 = instant. */
class CameraLerpBrick : FormulaBrick {
    constructor() : super() { addAllowedBrickField(Brick.BrickField.VALUE, 0) }
    constructor(speed: Float) : this() { setFormulaWithBrickField(Brick.BrickField.VALUE, Formula(speed.toDouble())) }
    constructor(f: Formula) : this() { setFormulaWithBrickField(Brick.BrickField.VALUE, f) }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        KixPresetActions.cameraLerp(getFormulaWithBrickField(Brick.BrickField.VALUE).interpretFloat(sprite))
    }
    override fun clone(): Brick = CameraLerpBrick(getFormulaWithBrickField(Brick.BrickField.VALUE))
}

/** Enables depth-of-field effect with focus distance and range. */
class CameraDepthOfFieldBrick : FormulaBrick {
    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.VALUE, 0) // focus
        addAllowedBrickField(Brick.BrickField.DURATION_IN_SECONDS, 0) // range
    }
    constructor(focus: Float, range: Float) : this() {
        setFormulaWithBrickField(Brick.BrickField.VALUE, Formula(focus.toDouble()))
        setFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS, Formula(range.toDouble()))
    }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        val focus = getFormulaWithBrickField(Brick.BrickField.VALUE).interpretFloat(sprite)
        val range = getFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS).interpretFloat(sprite)
        KixPresetActions.cameraDepthOfField(focus, range)
    }
    override fun clone(): Brick {
        val c = CameraDepthOfFieldBrick()
        c.setFormulaWithBrickField(Brick.BrickField.VALUE, getFormulaWithBrickField(Brick.BrickField.VALUE))
        c.setFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS, getFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS))
        return c
    }
}

/** Toggles isometric projection mode. */
class CameraIsometricBrick : FormulaBrick {
    private var enabled: Boolean = true
    constructor() : super()
    constructor(enabled: Boolean) : this() { this.enabled = enabled }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        KixPresetActions.cameraIsometric(enabled)
    }
    override fun clone(): Brick = CameraIsometricBrick(enabled)
}

/** Instant cinematic cut: set position, zoom and rotation at once. */
class CameraCinematicCutBrick : FormulaBrick {
    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.X_POSITION, 0)
        addAllowedBrickField(Brick.BrickField.Y_POSITION, 0)
        addAllowedBrickField(Brick.BrickField.VALUE, 0) // zoom
        addAllowedBrickField(Brick.BrickField.DURATION_IN_SECONDS, 0) // rotation
    }
    constructor(x: Float, y: Float, zoom: Float, rotation: Float) : this() {
        setFormulaWithBrickField(Brick.BrickField.X_POSITION, Formula(x.toDouble()))
        setFormulaWithBrickField(Brick.BrickField.Y_POSITION, Formula(y.toDouble()))
        setFormulaWithBrickField(Brick.BrickField.VALUE, Formula(zoom.toDouble()))
        setFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS, Formula(rotation.toDouble()))
    }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        val x = getFormulaWithBrickField(Brick.BrickField.X_POSITION).interpretFloat(sprite)
        val y = getFormulaWithBrickField(Brick.BrickField.Y_POSITION).interpretFloat(sprite)
        val zoom = getFormulaWithBrickField(Brick.BrickField.VALUE).interpretFloat(sprite)
        val rot = getFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS).interpretFloat(sprite)
        KixPresetActions.cameraCinematicCut(x, y, zoom, rot)
    }
    override fun clone(): Brick {
        val c = CameraCinematicCutBrick()
        c.setFormulaWithBrickField(Brick.BrickField.X_POSITION, getFormulaWithBrickField(Brick.BrickField.X_POSITION))
        c.setFormulaWithBrickField(Brick.BrickField.Y_POSITION, getFormulaWithBrickField(Brick.BrickField.Y_POSITION))
        c.setFormulaWithBrickField(Brick.BrickField.VALUE, getFormulaWithBrickField(Brick.BrickField.VALUE))
        c.setFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS, getFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS))
        return c
    }
}

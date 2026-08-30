/*
 * Kix Engine Mod - CameraFixedPositionBrick
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

/** Sets the camera to a fixed world position (stops following). */
class CameraFixedPositionBrick : FormulaBrick {

    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.X_POSITION, 0)
        addAllowedBrickField(Brick.BrickField.Y_POSITION, 0)
    }

    constructor(x: Float, y: Float) : this() {
        setFormulaWithBrickField(Brick.BrickField.X_POSITION, Formula(x.toDouble()))
        setFormulaWithBrickField(Brick.BrickField.Y_POSITION, Formula(y.toDouble()))
    }

    constructor(xFormula: Formula, yFormula: Formula) : this() {
        setFormulaWithBrickField(Brick.BrickField.X_POSITION, xFormula)
        setFormulaWithBrickField(Brick.BrickField.Y_POSITION, yFormula)
    }

    override fun getViewResource(): Int = android.R.layout.simple_list_item_1

    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        val x = getFormulaWithBrickField(Brick.BrickField.X_POSITION).interpretFloat(sprite)
        val y = getFormulaWithBrickField(Brick.BrickField.Y_POSITION).interpretFloat(sprite)
        KixPresetActions.cameraFixedPosition(x, y)
    }

    override fun clone(): Brick {
        return CameraFixedPositionBrick(
            getFormulaWithBrickField(Brick.BrickField.X_POSITION),
            getFormulaWithBrickField(Brick.BrickField.Y_POSITION)
        )
    }
}

/*
 * Kix Engine Mod - CameraRotateBrick
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

/** Rotates the camera by the given degrees. */
class CameraRotateBrick : FormulaBrick {

    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.VALUE, 0)
    }

    constructor(degrees: Float) : this() {
        setFormulaWithBrickField(Brick.BrickField.VALUE, Formula(degrees.toDouble()))
    }

    constructor(formula: Formula) : this() {
        setFormulaWithBrickField(Brick.BrickField.VALUE, formula)
    }

    override fun getViewResource(): Int = android.R.layout.simple_list_item_1

    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        val degrees = getFormulaWithBrickField(Brick.BrickField.VALUE).interpretFloat(sprite)
        KixPresetActions.cameraRotate(degrees)
    }

    override fun clone(): Brick {
        return CameraRotateBrick(getFormulaWithBrickField(Brick.BrickField.VALUE))
    }
}

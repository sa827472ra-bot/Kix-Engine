/*
 * Kix Engine Mod - CameraBoundsBrick
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

/** Restricts camera movement within a rectangular world bounds. */
class CameraBoundsBrick : FormulaBrick {

    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.X_POSITION, 0) // left
        addAllowedBrickField(Brick.BrickField.Y_POSITION, 0) // bottom
        addAllowedBrickField(Brick.BrickField.VALUE, 0)      // right (reused)
        addAllowedBrickField(Brick.BrickField.DURATION_IN_SECONDS, 0) // top (reused)
    }

    constructor(left: Float, right: Float, bottom: Float, top: Float) : this() {
        setFormulaWithBrickField(Brick.BrickField.X_POSITION, Formula(left.toDouble()))
        setFormulaWithBrickField(Brick.BrickField.VALUE, Formula(right.toDouble()))
        setFormulaWithBrickField(Brick.BrickField.Y_POSITION, Formula(bottom.toDouble()))
        setFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS, Formula(top.toDouble()))
    }

    override fun getViewResource(): Int = android.R.layout.simple_list_item_1

    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        val left = getFormulaWithBrickField(Brick.BrickField.X_POSITION).interpretFloat(sprite)
        val right = getFormulaWithBrickField(Brick.BrickField.VALUE).interpretFloat(sprite)
        val bottom = getFormulaWithBrickField(Brick.BrickField.Y_POSITION).interpretFloat(sprite)
        val top = getFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS).interpretFloat(sprite)
        KixPresetActions.cameraBounds(left, right, bottom, top)
    }

    override fun clone(): Brick {
        val c = CameraBoundsBrick()
        c.setFormulaWithBrickField(Brick.BrickField.X_POSITION, getFormulaWithBrickField(Brick.BrickField.X_POSITION))
        c.setFormulaWithBrickField(Brick.BrickField.VALUE, getFormulaWithBrickField(Brick.BrickField.VALUE))
        c.setFormulaWithBrickField(Brick.BrickField.Y_POSITION, getFormulaWithBrickField(Brick.BrickField.Y_POSITION))
        c.setFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS, getFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS))
        return c
    }
}

/*
 * Kix Engine Mod - BringLayerToFrontBrick
 * Copyright (C) 2026 Kix Engine Contributors
 * Licensed under the GNU Affero General Public License v3.0
 */

package org.catrobat.catroid.kix.bricks.layers

import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.content.actions.ScriptSequenceAction
import org.catrobat.catroid.content.bricks.Brick
import org.catrobat.catroid.content.bricks.FormulaBrick
import org.catrobat.catroid.formulaeditor.Formula
import org.catrobat.catroid.kix.actions.KixPresetActions

/**
 * Moves the specified layer to the front of the render order (index 9).
 */
class BringLayerToFrontBrick : FormulaBrick {

    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.VALUE, 0)
    }

    constructor(layerIndex: Int) : this() {
        setFormulaWithBrickField(Brick.BrickField.VALUE, Formula(layerIndex))
    }

    constructor(formula: Formula) : this() {
        setFormulaWithBrickField(Brick.BrickField.VALUE, formula)
    }

    override fun getViewResource(): Int = android.R.layout.simple_list_item_1

    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        val layer = getFormulaWithBrickField(Brick.BrickField.VALUE).interpretInteger(sprite)
        KixPresetActions.bringLayerToFront(layer)
    }

    override fun clone(): Brick {
        return BringLayerToFrontBrick(getFormulaWithBrickField(Brick.BrickField.VALUE))
    }
}

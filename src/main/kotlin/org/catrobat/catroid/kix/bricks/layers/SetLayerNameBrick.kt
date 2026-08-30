/*
 * Kix Engine Mod - SetLayerNameBrick
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
 * Sets a custom name for the specified visual layer.
 */
class SetLayerNameBrick : FormulaBrick {

    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.VALUE, 0)   // layer index
        addAllowedBrickField(Brick.BrickField.VARIABLE, 0) // name
    }

    constructor(layerIndex: Int, name: String) : this() {
        setFormulaWithBrickField(Brick.BrickField.VALUE, Formula(layerIndex))
        setFormulaWithBrickField(Brick.BrickField.VARIABLE, Formula(name))
    }

    constructor(layerFormula: Formula, nameFormula: Formula) : this() {
        setFormulaWithBrickField(Brick.BrickField.VALUE, layerFormula)
        setFormulaWithBrickField(Brick.BrickField.VARIABLE, nameFormula)
    }

    override fun getViewResource(): Int = android.R.layout.simple_list_item_1

    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        val layer = getFormulaWithBrickField(Brick.BrickField.VALUE).interpretInteger(sprite)
        val name = getFormulaWithBrickField(Brick.BrickField.VARIABLE).interpretString(sprite)
        KixPresetActions.setLayerName(layer, name)
    }

    override fun clone(): Brick {
        return SetLayerNameBrick(
            getFormulaWithBrickField(Brick.BrickField.VALUE),
            getFormulaWithBrickField(Brick.BrickField.VARIABLE)
        )
    }
}

/*
 * STUB – delete when integrating into real Catroid/NewCatroid.
 */
package org.catrobat.catroid.content.bricks

import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.content.actions.ScriptSequenceAction
import org.catrobat.catroid.formulaeditor.Formula

open class FormulaBrick : Brick {
    private val formulas = mutableMapOf<Brick.BrickField, Formula>()

    open fun addAllowedBrickField(field: Brick.BrickField, id: Int) {}

    open fun setFormulaWithBrickField(field: Brick.BrickField, formula: Formula) {
        formulas[field] = formula
    }

    open fun getFormulaWithBrickField(field: Brick.BrickField): Formula {
        return formulas[field] ?: Formula(0)
    }

    override fun getViewResource(): Int = android.R.layout.simple_list_item_1

    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {}

    override fun clone(): Brick = FormulaBrick()
}

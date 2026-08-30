/*
 * STUBS for standalone compilation of Kix Engine library.
 *
 * When integrating into NewCatroid/Catroid:
 * 1. DELETE this file (or the entire stubs package)
 * 2. Use real classes from the host project via compileOnly(project(":catroid"))
 *
 * These stubs mirror only the API surface used by Kix Engine bricks.
 */
package org.catrobat.catroid.content

import org.catrobat.catroid.content.actions.ScriptSequenceAction
import org.catrobat.catroid.content.bricks.Brick
import org.catrobat.catroid.content.bricks.FormulaBrick
import org.catrobat.catroid.formulaeditor.Formula

/** Minimal Sprite stub. */
open class Sprite {
    open var name: String = ""
    open var look: Look? = Look()
}

/** Minimal Look stub used by CameraManager follow. */
open class Look {
    open var xInUserInterfaceDimensionUnit: Float = 0f
    open var yInUserInterfaceDimensionUnit: Float = 0f
}

package org.catrobat.catroid.content.actions

/** Minimal sequence action stub. */
open class ScriptSequenceAction {
    open fun addAction(action: Any?) {}
}

package org.catrobat.catroid.content.bricks

import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.content.actions.ScriptSequenceAction
import org.catrobat.catroid.formulaeditor.Formula

interface Brick {
    enum class BrickField {
        VALUE,
        VARIABLE,
        X_POSITION,
        Y_POSITION,
        DURATION_IN_SECONDS,
        BRICK_FIELD
    }

    fun getViewResource(): Int
    fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction)
    fun clone(): Brick
}

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

package org.catrobat.catroid.formulaeditor

import org.catrobat.catroid.content.Sprite

/** Minimal Formula stub. */
open class Formula {
    private var numberValue: Double = 0.0
    private var stringValue: String = ""

    constructor() {}
    constructor(value: Int) { numberValue = value.toDouble(); stringValue = value.toString() }
    constructor(value: Double) { numberValue = value; stringValue = value.toString() }
    constructor(value: String) { stringValue = value; numberValue = value.toDoubleOrNull() ?: 0.0 }

    open fun interpretInteger(sprite: Sprite?): Int = numberValue.toInt()
    open fun interpretFloat(sprite: Sprite?): Float = numberValue.toFloat()
    open fun interpretDouble(sprite: Sprite?): Double = numberValue
    open fun interpretString(sprite: Sprite?): String = stringValue
}

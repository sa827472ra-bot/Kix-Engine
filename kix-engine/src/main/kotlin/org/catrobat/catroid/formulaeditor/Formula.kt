/*
 * STUB – delete when integrating into real Catroid/NewCatroid.
 */
package org.catrobat.catroid.formulaeditor

import org.catrobat.catroid.content.Sprite

open class Formula {
    private var numberValue: Double = 0.0
    private var stringValue: String = ""

    constructor(){}
    constructor(value: Int) {
        numberValue = value.toDouble()
        stringValue = value.toString()
    }
    constructor(value: Double) {
        numberValue = value
        stringValue = value.toString()
    }
    constructor(value: String) {
        stringValue = value
        numberValue = value.toDoubleOrNull() ?: 0.0
    }

    open fun interpretInteger(sprite: Sprite?): Int = numberValue.toInt()
    open fun interpretFloat(sprite: Sprite?): Float = numberValue.toFloat()
    open fun interpretDouble(sprite: Sprite?): Double = numberValue
    open fun interpretString(sprite: Sprite?): String = stringValue
}

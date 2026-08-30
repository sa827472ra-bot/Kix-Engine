/*
 * STUB – delete when integrating into real Catroid/NewCatroid.
 */
package org.catrobat.catroid.content.bricks

import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.content.actions.ScriptSequenceAction

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

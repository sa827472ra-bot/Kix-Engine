/*
 * Kix Engine - Collision Bricks (3)
 * Copyright (C) 2026 Kix Engine Contributors | AGPL-3.0
 */
package org.catrobat.catroid.kix.bricks.collision

import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.content.actions.ScriptSequenceAction
import org.catrobat.catroid.content.bricks.Brick
import org.catrobat.catroid.content.bricks.FormulaBrick
import org.catrobat.catroid.formulaeditor.Formula
import org.catrobat.catroid.kix.actions.KixPresetActions

class CollisionDetectionBrick : FormulaBrick {
    private var enable: Boolean = true
    constructor() : super()
    constructor(enable: Boolean) : this() { this.enable = enable }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        KixPresetActions.collisionDetectionEnable(enable)
    }
    override fun clone(): Brick = CollisionDetectionBrick(enable)
}

class CollisionGroupFilterBrick : FormulaBrick {
    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.VARIABLE, 0) // group
        addAllowedBrickField(Brick.BrickField.VALUE, 0)     // collides-with CSV
    }
    constructor(group: String, collidesWithCsv: String) : this() {
        setFormulaWithBrickField(Brick.BrickField.VARIABLE, Formula(group))
        setFormulaWithBrickField(Brick.BrickField.VALUE, Formula(collidesWithCsv))
    }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        val group = getFormulaWithBrickField(Brick.BrickField.VARIABLE).interpretString(sprite)
        val csv = getFormulaWithBrickField(Brick.BrickField.VALUE).interpretString(sprite)
        val set = csv.split(',').map { it.trim() }.filter { it.isNotEmpty() }.toSet()
        KixPresetActions.collisionGroupFilter(group, set)
    }
    override fun clone(): Brick {
        val c = CollisionGroupFilterBrick()
        c.setFormulaWithBrickField(Brick.BrickField.VARIABLE, getFormulaWithBrickField(Brick.BrickField.VARIABLE))
        c.setFormulaWithBrickField(Brick.BrickField.VALUE, getFormulaWithBrickField(Brick.BrickField.VALUE))
        return c
    }
}

/** Registers a collision listener; full script-hat integration is host-side. */
class OnCollisionBrick : FormulaBrick {
    constructor() : super()
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        KixPresetActions.onCollision { _, _ -> }
    }
    override fun clone(): Brick = OnCollisionBrick()
}

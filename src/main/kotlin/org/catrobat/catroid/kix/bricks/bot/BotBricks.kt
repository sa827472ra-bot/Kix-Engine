/*
 * Kix Engine - Bot Bricks (4)
 * Copyright (C) 2026 Kix Engine Contributors | AGPL-3.0
 */
package org.catrobat.catroid.kix.bricks.bot

import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.content.actions.ScriptSequenceAction
import org.catrobat.catroid.content.bricks.Brick
import org.catrobat.catroid.content.bricks.FormulaBrick
import org.catrobat.catroid.formulaeditor.Formula
import org.catrobat.catroid.kix.actions.KixPresetActions

/**
 * Patrol along points encoded as "x1,y1;x2,y2;..."
 */
class BotPatrolBrick : FormulaBrick {
    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.VARIABLE, 0)
    }
    constructor(pointsCsv: String) : this() {
        setFormulaWithBrickField(Brick.BrickField.VARIABLE, Formula(pointsCsv))
    }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        val raw = getFormulaWithBrickField(Brick.BrickField.VARIABLE).interpretString(sprite)
        val points = raw.split(';').mapNotNull { pair ->
            val parts = pair.split(',')
            if (parts.size >= 2) {
                val x = parts[0].trim().toFloatOrNull()
                val y = parts[1].trim().toFloatOrNull()
                if (x != null && y != null) x to y else null
            } else null
        }
        KixPresetActions.botPatrol(sprite, points)
    }
    override fun clone(): Brick = BotPatrolBrick().also {
        it.setFormulaWithBrickField(Brick.BrickField.VARIABLE, getFormulaWithBrickField(Brick.BrickField.VARIABLE))
    }
}

/** Follow another actor by name is host-resolved; here follows self look target via action API. */
class BotFollowTargetBrick : FormulaBrick {
    constructor() : super()
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        // Host ActionFactory should pass the real target Sprite; default no-op target = self
        KixPresetActions.botFollowTarget(sprite, sprite)
    }
    override fun clone(): Brick = BotFollowTargetBrick()
}

class BotPathfindBrick : FormulaBrick {
    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.X_POSITION, 0)
        addAllowedBrickField(Brick.BrickField.Y_POSITION, 0)
    }
    constructor(x: Float, y: Float) : this() {
        setFormulaWithBrickField(Brick.BrickField.X_POSITION, Formula(x.toDouble()))
        setFormulaWithBrickField(Brick.BrickField.Y_POSITION, Formula(y.toDouble()))
    }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        val x = getFormulaWithBrickField(Brick.BrickField.X_POSITION).interpretFloat(sprite)
        val y = getFormulaWithBrickField(Brick.BrickField.Y_POSITION).interpretFloat(sprite)
        KixPresetActions.botPathfind(sprite, x, y)
    }
    override fun clone(): Brick {
        val c = BotPathfindBrick()
        c.setFormulaWithBrickField(Brick.BrickField.X_POSITION, getFormulaWithBrickField(Brick.BrickField.X_POSITION))
        c.setFormulaWithBrickField(Brick.BrickField.Y_POSITION, getFormulaWithBrickField(Brick.BrickField.Y_POSITION))
        return c
    }
}

class BotAIBehaviorBrick : FormulaBrick {
    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.VARIABLE, 0)
    }
    constructor(behaviorId: String) : this() {
        setFormulaWithBrickField(Brick.BrickField.VARIABLE, Formula(behaviorId))
    }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        val id = getFormulaWithBrickField(Brick.BrickField.VARIABLE).interpretString(sprite)
        KixPresetActions.botAiBehavior(sprite, id)
    }
    override fun clone(): Brick = BotAIBehaviorBrick().also {
        it.setFormulaWithBrickField(Brick.BrickField.VARIABLE, getFormulaWithBrickField(Brick.BrickField.VARIABLE))
    }
}

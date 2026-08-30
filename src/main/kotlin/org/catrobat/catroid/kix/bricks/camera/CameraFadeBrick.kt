/*
 * Kix Engine Mod - CameraFadeInBrick / CameraFadeOutBrick
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

/** Fades the camera view in (alpha -> 1). */
class CameraFadeInBrick : FormulaBrick {

    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.DURATION_IN_SECONDS, 0)
    }

    constructor(durationMs: Long) : this() {
        setFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS, Formula(durationMs / 1000.0))
    }

    constructor(formula: Formula) : this() {
        setFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS, formula)
    }

    override fun getViewResource(): Int = android.R.layout.simple_list_item_1

    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        val durationSec = getFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS).interpretFloat(sprite)
        KixPresetActions.cameraFadeIn((durationSec * 1000).toLong())
    }

    override fun clone(): Brick {
        return CameraFadeInBrick(getFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS))
    }
}

/** Fades the camera view out (alpha -> 0). */
class CameraFadeOutBrick : FormulaBrick {

    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.DURATION_IN_SECONDS, 0)
    }

    constructor(durationMs: Long) : this() {
        setFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS, Formula(durationMs / 1000.0))
    }

    constructor(formula: Formula) : this() {
        setFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS, formula)
    }

    override fun getViewResource(): Int = android.R.layout.simple_list_item_1

    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        val durationSec = getFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS).interpretFloat(sprite)
        KixPresetActions.cameraFadeOut((durationSec * 1000).toLong())
    }

    override fun clone(): Brick {
        return CameraFadeOutBrick(getFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS))
    }
}

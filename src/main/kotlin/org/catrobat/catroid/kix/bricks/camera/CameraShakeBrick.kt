/*
 * Kix Engine Mod - CameraShakeBrick
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

/** Shakes the camera with intensity for duration (ms). */
class CameraShakeBrick : FormulaBrick {

    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.VALUE, 0)      // intensity
        addAllowedBrickField(Brick.BrickField.DURATION_IN_SECONDS, 0)
    }

    constructor(intensity: Float, durationMs: Long) : this() {
        setFormulaWithBrickField(Brick.BrickField.VALUE, Formula(intensity.toDouble()))
        setFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS, Formula(durationMs / 1000.0))
    }

    constructor(intensityFormula: Formula, durationFormula: Formula) : this() {
        setFormulaWithBrickField(Brick.BrickField.VALUE, intensityFormula)
        setFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS, durationFormula)
    }

    override fun getViewResource(): Int = android.R.layout.simple_list_item_1

    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        val intensity = getFormulaWithBrickField(Brick.BrickField.VALUE).interpretFloat(sprite)
        val durationSec = getFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS).interpretFloat(sprite)
        KixPresetActions.cameraShake(intensity, (durationSec * 1000).toLong())
    }

    override fun clone(): Brick {
        return CameraShakeBrick(
            getFormulaWithBrickField(Brick.BrickField.VALUE),
            getFormulaWithBrickField(Brick.BrickField.DURATION_IN_SECONDS)
        )
    }
}

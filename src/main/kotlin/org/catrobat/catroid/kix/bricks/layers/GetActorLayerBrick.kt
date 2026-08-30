/*
 * Kix Engine Mod - GetActorLayerBrick
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
 * Retrieves the current layer index of the actor.
 * In a full Catroid integration this would typically write into a user variable.
 * Here it evaluates the layer and can be used as a sensor-style brick.
 */
class GetActorLayerBrick : FormulaBrick {

    constructor() : super()

    override fun getViewResource(): Int = android.R.layout.simple_list_item_1

    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        // The value is available via KixPresetActions.getActorLayer(sprite).
        // Full variable write integration is left for the host Catroid ActionFactory.
        KixPresetActions.getActorLayer(sprite)
    }

    override fun clone(): Brick = GetActorLayerBrick()

    /** Convenience helper for formula / sensor usage. */
    fun evaluate(sprite: Sprite): Int = KixPresetActions.getActorLayer(sprite)
}

/*
 * Kix Engine Mod - CameraFollowPlayerBrick
 * Copyright (C) 2026 Kix Engine Contributors
 * Licensed under AGPL-3.0
 */
package org.catrobat.catroid.kix.bricks.camera

import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.content.actions.ScriptSequenceAction
import org.catrobat.catroid.content.bricks.Brick
import org.catrobat.catroid.content.bricks.FormulaBrick
import org.catrobat.catroid.kix.actions.KixPresetActions

/** Makes the camera follow the current actor (or a target sprite). */
class CameraFollowPlayerBrick : FormulaBrick {

    constructor() : super()

    override fun getViewResource(): Int = android.R.layout.simple_list_item_1

    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        KixPresetActions.cameraFollowPlayer(sprite)
    }

    override fun clone(): Brick = CameraFollowPlayerBrick()
}

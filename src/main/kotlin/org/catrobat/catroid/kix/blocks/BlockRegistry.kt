/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2026 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * Kix Engine Mod - Central Block Registry
 * Copyright (C) 2026 Kix Engine Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.kix.blocks

import org.catrobat.catroid.content.bricks.Brick
import java.util.concurrent.ConcurrentHashMap

/**
 * Central registry that organizes all Kix Engine bricks by category
 * for presentation in the Pocket Code UI.
 */
object BlockRegistry {

    const val CATEGORY_LAYERS = "Layers"
    const val CATEGORY_CAMERA = "Camera"
    const val CATEGORY_JOYSTICK = "Joystick"
    const val CATEGORY_NETWORK = "Network"
    const val CATEGORY_COLLISION = "Collision"
    const val CATEGORY_BOT = "Bot"
    const val CATEGORY_TEXT = "Text"
    const val CATEGORY_CUSTOM = "Custom"

    private val categories = ConcurrentHashMap<String, MutableList<Brick>>()
    private val brickById = ConcurrentHashMap<String, Brick>()

    init {
        listOf(
            CATEGORY_LAYERS,
            CATEGORY_CAMERA,
            CATEGORY_JOYSTICK,
            CATEGORY_NETWORK,
            CATEGORY_COLLISION,
            CATEGORY_BOT,
            CATEGORY_TEXT,
            CATEGORY_CUSTOM
        ).forEach { ensureCategory(it) }
    }

    fun ensureCategory(category: String) {
        categories.putIfAbsent(category, mutableListOf())
    }

    fun register(category: String, brick: Brick, id: String? = null) {
        ensureCategory(category)
        categories[category]?.add(brick)

        val resolvedId = id
            ?: (brick as? CustomBlockBrick)?.getBlockId()
            ?: brick.javaClass.simpleName

        brickById[resolvedId] = brick
    }

    fun registerAll(category: String, bricks: List<Brick>) {
        bricks.forEach { register(category, it) }
    }

    fun unregister(id: String) {
        val brick = brickById.remove(id) ?: return
        categories.values.forEach { list -> list.remove(brick) }
    }

    fun getBricksForCategory(category: String): List<Brick> {
        return categories[category]?.toList() ?: emptyList()
    }

    fun getAllCategories(): List<String> = categories.keys.sorted()

    fun getBrickById(id: String): Brick? = brickById[id]

    fun getAllBricks(): List<Brick> = brickById.values.toList()

    fun clear() {
        categories.values.forEach { it.clear() }
        brickById.clear()
    }

    fun registerDefaultLayerBricks() {
        register(CATEGORY_LAYERS, org.catrobat.catroid.kix.bricks.layers.ShowLayerBrick(), "ShowLayerBrick")
        register(CATEGORY_LAYERS, org.catrobat.catroid.kix.bricks.layers.HideLayerBrick(), "HideLayerBrick")
        register(CATEGORY_LAYERS, org.catrobat.catroid.kix.bricks.layers.ToggleLayerVisibilityBrick(), "ToggleLayerVisibilityBrick")
        register(CATEGORY_LAYERS, org.catrobat.catroid.kix.bricks.layers.SetLayerNameBrick(), "SetLayerNameBrick")
        register(CATEGORY_LAYERS, org.catrobat.catroid.kix.bricks.layers.SetActorLayerBrick(), "SetActorLayerBrick")
        register(CATEGORY_LAYERS, org.catrobat.catroid.kix.bricks.layers.GetActorLayerBrick(), "GetActorLayerBrick")
        register(CATEGORY_LAYERS, org.catrobat.catroid.kix.bricks.layers.BringLayerToFrontBrick(), "BringLayerToFrontBrick")
        register(CATEGORY_LAYERS, org.catrobat.catroid.kix.bricks.layers.SendLayerToBackBrick(), "SendLayerToBackBrick")
    }

    fun registerDefaultCameraBricks() {
        register(CATEGORY_CAMERA, org.catrobat.catroid.kix.bricks.camera.CameraFollowPlayerBrick(), "CameraFollowPlayerBrick")
        register(CATEGORY_CAMERA, org.catrobat.catroid.kix.bricks.camera.CameraFixedPositionBrick(), "CameraFixedPositionBrick")
        register(CATEGORY_CAMERA, org.catrobat.catroid.kix.bricks.camera.CameraZoomBrick(), "CameraZoomBrick")
        register(CATEGORY_CAMERA, org.catrobat.catroid.kix.bricks.camera.CameraRotateBrick(), "CameraRotateBrick")
        register(CATEGORY_CAMERA, org.catrobat.catroid.kix.bricks.camera.CameraShakeBrick(), "CameraShakeBrick")
        register(CATEGORY_CAMERA, org.catrobat.catroid.kix.bricks.camera.CameraFadeInBrick(), "CameraFadeInBrick")
        register(CATEGORY_CAMERA, org.catrobat.catroid.kix.bricks.camera.CameraFadeOutBrick(), "CameraFadeOutBrick")
        register(CATEGORY_CAMERA, org.catrobat.catroid.kix.bricks.camera.CameraBoundsBrick(), "CameraBoundsBrick")
        register(CATEGORY_CAMERA, org.catrobat.catroid.kix.bricks.camera.CameraLerpBrick(), "CameraLerpBrick")
        register(CATEGORY_CAMERA, org.catrobat.catroid.kix.bricks.camera.CameraDepthOfFieldBrick(), "CameraDepthOfFieldBrick")
        register(CATEGORY_CAMERA, org.catrobat.catroid.kix.bricks.camera.CameraIsometricBrick(), "CameraIsometricBrick")
        register(CATEGORY_CAMERA, org.catrobat.catroid.kix.bricks.camera.CameraCinematicCutBrick(), "CameraCinematicCutBrick")
    }

    /** Call once at app/mod startup. */
    fun registerAllDefaults() {
        registerDefaultLayerBricks()
        registerDefaultCameraBricks()
    }
}

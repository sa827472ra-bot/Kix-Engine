/*
 * Kix Engine Mod - Central Block Registry
 * Copyright (C) 2026 Kix Engine Contributors | AGPL-3.0
 */
package org.catrobat.catroid.kix.blocks

import org.catrobat.catroid.content.bricks.Brick
import java.util.concurrent.ConcurrentHashMap

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
            CATEGORY_LAYERS, CATEGORY_CAMERA, CATEGORY_JOYSTICK,
            CATEGORY_NETWORK, CATEGORY_COLLISION, CATEGORY_BOT,
            CATEGORY_TEXT, CATEGORY_CUSTOM
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
        categories.values.forEach { it.remove(brick) }
    }

    fun getBricksForCategory(category: String): List<Brick> =
        categories[category]?.toList() ?: emptyList()

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

    fun registerDefaultJoystickBricks() {
        register(CATEGORY_JOYSTICK, org.catrobat.catroid.kix.bricks.joystick.JoystickDPadBrick(), "JoystickDPadBrick")
        register(CATEGORY_JOYSTICK, org.catrobat.catroid.kix.bricks.joystick.JoystickAnalogBrick(), "JoystickAnalogBrick")
        register(CATEGORY_JOYSTICK, org.catrobat.catroid.kix.bricks.joystick.JoystickDualStickBrick(), "JoystickDualStickBrick")
        register(CATEGORY_JOYSTICK, org.catrobat.catroid.kix.bricks.joystick.GetJoystickInputBrick(), "GetJoystickInputBrick")
    }

    fun registerDefaultNetworkBricks() {
        register(CATEGORY_NETWORK, org.catrobat.catroid.kix.bricks.network.NetworkUDPConnectBrick(), "NetworkUDPConnectBrick")
        register(CATEGORY_NETWORK, org.catrobat.catroid.kix.bricks.network.NetworkUDPSendBrick(), "NetworkUDPSendBrick")
        register(CATEGORY_NETWORK, org.catrobat.catroid.kix.bricks.network.NetworkUDPReceiveBrick(), "NetworkUDPReceiveBrick")
        register(CATEGORY_NETWORK, org.catrobat.catroid.kix.bricks.network.NetworkUDPDisconnectBrick(), "NetworkUDPDisconnectBrick")
        register(CATEGORY_NETWORK, org.catrobat.catroid.kix.bricks.network.NetworkTCPConnectBrick(), "NetworkTCPConnectBrick")
        register(CATEGORY_NETWORK, org.catrobat.catroid.kix.bricks.network.NetworkTCPSendBrick(), "NetworkTCPSendBrick")
        register(CATEGORY_NETWORK, org.catrobat.catroid.kix.bricks.network.NetworkTCPReceiveBrick(), "NetworkTCPReceiveBrick")
        register(CATEGORY_NETWORK, org.catrobat.catroid.kix.bricks.network.NetworkTCPDisconnectBrick(), "NetworkTCPDisconnectBrick")
        register(CATEGORY_NETWORK, org.catrobat.catroid.kix.bricks.network.NetworkBroadcastBrick(), "NetworkBroadcastBrick")
        register(CATEGORY_NETWORK, org.catrobat.catroid.kix.bricks.network.NetworkListenBrick(), "NetworkListenBrick")
    }

    fun registerDefaultCollisionBricks() {
        register(CATEGORY_COLLISION, org.catrobat.catroid.kix.bricks.collision.CollisionDetectionBrick(), "CollisionDetectionBrick")
        register(CATEGORY_COLLISION, org.catrobat.catroid.kix.bricks.collision.CollisionGroupFilterBrick(), "CollisionGroupFilterBrick")
        register(CATEGORY_COLLISION, org.catrobat.catroid.kix.bricks.collision.OnCollisionBrick(), "OnCollisionBrick")
    }

    fun registerDefaultBotBricks() {
        register(CATEGORY_BOT, org.catrobat.catroid.kix.bricks.bot.BotPatrolBrick(), "BotPatrolBrick")
        register(CATEGORY_BOT, org.catrobat.catroid.kix.bricks.bot.BotFollowTargetBrick(), "BotFollowTargetBrick")
        register(CATEGORY_BOT, org.catrobat.catroid.kix.bricks.bot.BotPathfindBrick(), "BotPathfindBrick")
        register(CATEGORY_BOT, org.catrobat.catroid.kix.bricks.bot.BotAIBehaviorBrick(), "BotAIBehaviorBrick")
    }

    fun registerAllDefaults() {
        registerDefaultLayerBricks()
        registerDefaultCameraBricks()
        registerDefaultJoystickBricks()
        registerDefaultNetworkBricks()
        registerDefaultCollisionBricks()
        registerDefaultBotBricks()
    }
}

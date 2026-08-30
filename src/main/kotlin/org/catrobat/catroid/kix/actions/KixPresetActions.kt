/*
 * Kix Engine Mod - Central Action Presets
 * Copyright (C) 2026 Kix Engine Contributors | AGPL-3.0
 */
package org.catrobat.catroid.kix.actions

import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.kix.bot.BotManager
import org.catrobat.catroid.kix.camera.CameraManager
import org.catrobat.catroid.kix.collision.CollisionManager
import org.catrobat.catroid.kix.joystick.JoystickManager
import org.catrobat.catroid.kix.layers.LayerManager
import org.catrobat.catroid.kix.network.NetworkManager

object KixPresetActions {

    // LAYER
    fun showLayer(layerIndex: Int) = LayerManager.showLayer(layerIndex)
    fun hideLayer(layerIndex: Int) = LayerManager.hideLayer(layerIndex)
    fun toggleLayerVisibility(layerIndex: Int) = LayerManager.toggleLayerVisibility(layerIndex)
    fun setLayerName(layerIndex: Int, name: String) = LayerManager.setLayerName(layerIndex, name)
    fun setActorLayer(sprite: Sprite, layerIndex: Int) = LayerManager.setActorLayer(sprite, layerIndex)
    fun getActorLayer(sprite: Sprite): Int = LayerManager.getActorLayer(sprite)
    fun bringLayerToFront(layerIndex: Int) = LayerManager.bringLayerToFront(layerIndex)
    fun sendLayerToBack(layerIndex: Int) = LayerManager.sendLayerToBack(layerIndex)

    // CAMERA
    fun cameraFollowPlayer(target: Sprite) = CameraManager.followPlayer(target)
    fun cameraFixedPosition(x: Float, y: Float) = CameraManager.setFixedPosition(x, y)
    fun cameraZoom(factor: Float) = CameraManager.setZoom(factor)
    fun cameraRotate(degrees: Float) = CameraManager.setRotation(degrees)
    fun cameraShake(intensity: Float, durationMs: Long) = CameraManager.shake(intensity, durationMs)
    fun cameraFadeIn(durationMs: Long = 300L) = CameraManager.fadeIn(durationMs)
    fun cameraFadeOut(durationMs: Long = 300L) = CameraManager.fadeOut(durationMs)
    fun cameraBounds(left: Float, right: Float, bottom: Float, top: Float) =
        CameraManager.setBounds(left, right, bottom, top)
    fun cameraLerp(speed: Float) = CameraManager.setLerpSpeed(speed)
    fun cameraDepthOfField(focus: Float, range: Float) = CameraManager.setDepthOfField(focus, range)
    fun cameraIsometric(enabled: Boolean) = CameraManager.setIsometric(enabled)
    fun cameraCinematicCut(x: Float, y: Float, zoom: Float, rotation: Float) =
        CameraManager.cinematicCut(x, y, zoom, rotation)

    // JOYSTICK
    fun joystickDPadEnable(enable: Boolean) = JoystickManager.enableDPad(enable)
    fun joystickAnalogEnable(enable: Boolean) = JoystickManager.enableAnalog(enable)
    fun joystickDualStickEnable(enable: Boolean) = JoystickManager.enableDualStick(enable)
    fun getJoystickInput(): Pair<Float, Float> = JoystickManager.getInput()
    fun getJoystickRightInput(): Pair<Float, Float> = JoystickManager.getRightInput()

    // NETWORK
    fun networkUdpConnect(host: String, port: Int) = NetworkManager.udpConnect(host, port)
    fun networkUdpSend(data: String) = NetworkManager.udpSend(data)
    fun networkUdpReceive(): String? = NetworkManager.udpReceive()
    fun networkUdpDisconnect() = NetworkManager.udpDisconnect()
    fun networkTcpConnect(host: String, port: Int) = NetworkManager.tcpConnect(host, port)
    fun networkTcpSend(data: String) = NetworkManager.tcpSend(data)
    fun networkTcpReceive(): String? = NetworkManager.tcpReceive()
    fun networkTcpDisconnect() = NetworkManager.tcpDisconnect()
    fun networkBroadcast(message: String) = NetworkManager.broadcast(message)
    fun networkListen(callback: (String) -> Unit) = NetworkManager.listen(callback)

    // COLLISION
    fun collisionDetectionEnable(enable: Boolean) = CollisionManager.setDetectionEnabled(enable)
    fun collisionGroupFilter(group: String, collidesWith: Set<String>) =
        CollisionManager.setGroupFilter(group, collidesWith)
    fun onCollision(callback: (Sprite, Sprite) -> Unit) = CollisionManager.onCollision(callback)
    fun collisionCheck(a: Sprite, b: Sprite): Boolean = CollisionManager.check(a, b)

    // BOT
    fun botPatrol(sprite: Sprite, points: List<Pair<Float, Float>>) = BotManager.patrol(sprite, points)
    fun botFollowTarget(sprite: Sprite, target: Sprite) = BotManager.followTarget(sprite, target)
    fun botPathfind(sprite: Sprite, destinationX: Float, destinationY: Float) =
        BotManager.pathfind(sprite, destinationX, destinationY)
    fun botAiBehavior(sprite: Sprite, behaviorId: String) = BotManager.setAiBehavior(sprite, behaviorId)
}

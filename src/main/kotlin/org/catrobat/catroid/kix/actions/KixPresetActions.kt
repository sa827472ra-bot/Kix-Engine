/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2026 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * Kix Engine Mod - Central Action Presets (26 actions)
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

package org.catrobat.catroid.kix.actions

import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.kix.layers.LayerManager

/**
 * Central registry and executor for all Kix Engine preset actions.
 *
 * Categories:
 *  - Layer Actions (8)
 *  - Camera Actions (4)
 *  - Joystick Actions (3)
 *  - Network Actions (5)
 *  - Collision Actions (2)
 *  - Bot Actions (4)
 *
 * Total: 26 actions
 */
object KixPresetActions {

    // -------------------------------------------------------------------------
    // LAYER ACTIONS (8)
    // -------------------------------------------------------------------------

    fun showLayer(layerIndex: Int) {
        LayerManager.showLayer(layerIndex)
    }

    fun hideLayer(layerIndex: Int) {
        LayerManager.hideLayer(layerIndex)
    }

    fun toggleLayerVisibility(layerIndex: Int) {
        LayerManager.toggleLayerVisibility(layerIndex)
    }

    fun setLayerName(layerIndex: Int, name: String) {
        LayerManager.setLayerName(layerIndex, name)
    }

    fun setActorLayer(sprite: Sprite, layerIndex: Int) {
        LayerManager.setActorLayer(sprite, layerIndex)
    }

    fun getActorLayer(sprite: Sprite): Int {
        return LayerManager.getActorLayer(sprite)
    }

    fun bringLayerToFront(layerIndex: Int) {
        LayerManager.bringLayerToFront(layerIndex)
    }

    fun sendLayerToBack(layerIndex: Int) {
        LayerManager.sendLayerToBack(layerIndex)
    }

    // -------------------------------------------------------------------------
    // CAMERA ACTIONS (4) - stubs for next iteration
    // -------------------------------------------------------------------------

    fun cameraFollowPlayer(target: Sprite) {
        // TODO: implement in Camera subsystem
    }

    fun cameraFixedPosition(x: Float, y: Float) {
        // TODO: implement in Camera subsystem
    }

    fun cameraZoom(factor: Float) {
        // TODO: implement in Camera subsystem
    }

    fun cameraShake(intensity: Float, durationMs: Long) {
        // TODO: implement in Camera subsystem
    }

    // -------------------------------------------------------------------------
    // JOYSTICK ACTIONS (3) - stubs
    // -------------------------------------------------------------------------

    fun joystickDPadEnable(enable: Boolean) {
        // TODO
    }

    fun joystickAnalogEnable(enable: Boolean) {
        // TODO
    }

    fun getJoystickInput(): Pair<Float, Float> {
        // TODO: return (x, y) normalized
        return 0f to 0f
    }

    // -------------------------------------------------------------------------
    // NETWORK ACTIONS (5) - stubs
    // -------------------------------------------------------------------------

    fun networkUdpConnect(host: String, port: Int) {
        // TODO
    }

    fun networkUdpSend(data: String) {
        // TODO
    }

    fun networkUdpReceive(): String? {
        // TODO
        return null
    }

    fun networkTcpConnect(host: String, port: Int) {
        // TODO
    }

    fun networkBroadcast(message: String) {
        // TODO
    }

    // -------------------------------------------------------------------------
    // COLLISION ACTIONS (2) - stubs
    // -------------------------------------------------------------------------

    fun collisionDetectionEnable(enable: Boolean) {
        // TODO
    }

    fun onCollision(spriteA: Sprite, spriteB: Sprite, callback: () -> Unit) {
        // TODO
    }

    // -------------------------------------------------------------------------
    // BOT ACTIONS (4) - stubs
    // -------------------------------------------------------------------------

    fun botPatrol(sprite: Sprite, points: List<Pair<Float, Float>>) {
        // TODO
    }

    fun botFollowTarget(sprite: Sprite, target: Sprite) {
        // TODO
    }

    fun botPathfind(sprite: Sprite, destinationX: Float, destinationY: Float) {
        // TODO
    }

    fun botAiBehavior(sprite: Sprite, behaviorId: String) {
        // TODO
    }
}

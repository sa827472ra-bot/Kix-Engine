/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2026 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * Kix Engine Mod - Layer Management System
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

package org.catrobat.catroid.kix.layers

import org.catrobat.catroid.content.Sprite
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Central manager for exactly 10 visual layers in the Kix Engine.
 *
 * Layers are ordered from 0 (back) to 9 (front).
 * Actors (Sprites) can be assigned to a specific layer.
 * Render order is strictly controlled by layer index + internal actor order.
 */
object LayerManager {

    const val MAX_LAYERS = 10
    const val LAYER_BACK = 0
    const val LAYER_FRONT = 9

    data class Layer(
        val index: Int,
        var name: String = "Layer $index",
        var isVisible: Boolean = true,
        val actors: CopyOnWriteArrayList<Sprite> = CopyOnWriteArrayList()
    )

    private val layers: Array<Layer> = Array(MAX_LAYERS) { i ->
        Layer(index = i, name = "Layer $i")
    }

    // Fast lookup: Sprite -> current layer index
    private val actorLayerMap = ConcurrentHashMap<Sprite, Int>()

    // -------------------------------------------------------------------------
    // Visibility
    // -------------------------------------------------------------------------

    fun showLayer(layerIndex: Int) {
        requireValidLayer(layerIndex)
        layers[layerIndex].isVisible = true
    }

    fun hideLayer(layerIndex: Int) {
        requireValidLayer(layerIndex)
        layers[layerIndex].isVisible = false
    }

    fun toggleLayerVisibility(layerIndex: Int) {
        requireValidLayer(layerIndex)
        layers[layerIndex].isVisible = !layers[layerIndex].isVisible
    }

    fun isLayerVisible(layerIndex: Int): Boolean {
        requireValidLayer(layerIndex)
        return layers[layerIndex].isVisible
    }

    // -------------------------------------------------------------------------
    // Naming
    // -------------------------------------------------------------------------

    fun setLayerName(layerIndex: Int, name: String) {
        requireValidLayer(layerIndex)
        layers[layerIndex].name = name.ifBlank { "Layer $layerIndex" }
    }

    fun getLayerName(layerIndex: Int): String {
        requireValidLayer(layerIndex)
        return layers[layerIndex].name
    }

    // -------------------------------------------------------------------------
    // Actor assignment
    // -------------------------------------------------------------------------

    fun setActorLayer(sprite: Sprite, layerIndex: Int) {
        requireValidLayer(layerIndex)
        removeActorFromCurrentLayer(sprite)
        layers[layerIndex].actors.add(sprite)
        actorLayerMap[sprite] = layerIndex
    }

    fun getActorLayer(sprite: Sprite): Int {
        return actorLayerMap[sprite] ?: LAYER_BACK
    }

    fun addActorToLayer(sprite: Sprite, layerIndex: Int) {
        setActorLayer(sprite, layerIndex)
    }

    fun removeActorFromCurrentLayer(sprite: Sprite) {
        val current = actorLayerMap.remove(sprite) ?: return
        layers[current].actors.remove(sprite)
    }

    fun getActorsOnLayer(layerIndex: Int): List<Sprite> {
        requireValidLayer(layerIndex)
        return layers[layerIndex].actors.toList()
    }

    // -------------------------------------------------------------------------
    // Render order (bring to front / send to back)
    // -------------------------------------------------------------------------

    /**
     * Moves the entire layer to the front of the render order (index 9).
     * Shifts other layers accordingly.
     */
    fun bringLayerToFront(layerIndex: Int) {
        requireValidLayer(layerIndex)
        if (layerIndex == LAYER_FRONT) return
        shiftLayers(layerIndex, LAYER_FRONT)
    }

    /**
     * Moves the entire layer to the back of the render order (index 0).
     */
    fun sendLayerToBack(layerIndex: Int) {
        requireValidLayer(layerIndex)
        if (layerIndex == LAYER_BACK) return
        shiftLayers(layerIndex, LAYER_BACK)
    }

    /**
     * Moves a single actor to the front of its current layer
     * (last in the list = rendered last = on top).
     */
    fun bringActorToFrontInLayer(sprite: Sprite) {
        val layerIdx = getActorLayer(sprite)
        val actors = layers[layerIdx].actors
        if (actors.remove(sprite)) {
            actors.add(sprite)
        }
    }

    /**
     * Moves a single actor to the back of its current layer.
     */
    fun sendActorToBackInLayer(sprite: Sprite) {
        val layerIdx = getActorLayer(sprite)
        val actors = layers[layerIdx].actors
        if (actors.remove(sprite)) {
            actors.add(0, sprite)
        }
    }

    // -------------------------------------------------------------------------
    // Query helpers
    // -------------------------------------------------------------------------

    fun getLayer(layerIndex: Int): Layer {
        requireValidLayer(layerIndex)
        return layers[layerIndex]
    }

    fun getAllLayers(): List<Layer> = layers.toList()

    fun getVisibleLayersInRenderOrder(): List<Layer> {
        return layers.filter { it.isVisible }
    }

    fun clearAll() {
        layers.forEach { it.actors.clear() }
        actorLayerMap.clear()
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    private fun requireValidLayer(index: Int) {
        require(index in 0 until MAX_LAYERS) {
            "Layer index must be between 0 and ${MAX_LAYERS - 1}, got $index"
        }
    }

    /**
     * Shifts layers so that [fromIndex] moves to [toIndex].
     * Preserves relative order of the other layers.
     */
    private fun shiftLayers(fromIndex: Int, toIndex: Int) {
        if (fromIndex == toIndex) return

        val moving = layers[fromIndex]
        if (fromIndex < toIndex) {
            for (i in fromIndex until toIndex) {
                layers[i] = layers[i + 1]
                layers[i].index = i
                // update actor map
                layers[i].actors.forEach { actorLayerMap[it] = i }
            }
        } else {
            for (i in fromIndex downTo toIndex + 1) {
                layers[i] = layers[i - 1]
                layers[i].index = i
                layers[i].actors.forEach { actorLayerMap[it] = i }
            }
        }
        layers[toIndex] = moving
        moving.index = toIndex
        moving.actors.forEach { actorLayerMap[it] = toIndex }
    }
}

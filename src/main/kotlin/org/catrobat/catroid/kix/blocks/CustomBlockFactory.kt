/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2026 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * Kix Engine Mod - Dynamic Custom Block Factory
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

import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.content.actions.ScriptSequenceAction
import org.catrobat.catroid.formulaeditor.Formula
import org.catrobat.catroid.kix.actions.KixPresetActions
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * Transforms JSON definitions or plain strings into usable [CustomBlockBrick] instances.
 * Also acts as the runtime dispatcher for dynamic actions.
 */
object CustomBlockFactory {

    data class BlockDefinition(
        val id: String,
        val category: String,
        val name: String,
        val actionId: String,
        val parameters: List<ParameterDef> = emptyList()
    )

    data class ParameterDef(
        val key: String,
        val defaultValue: String = "0",
        val type: String = "number" // number | text | boolean
    )

    /**
     * Creates a brick from a JSON string definition.
     *
     * Expected JSON shape:
     * {
     *   "id": "show_layer",
     *   "category": "Layers",
     *   "name": "Show Layer",
     *   "actionId": "showLayer",
     *   "parameters": [
     *     { "key": "layer", "defaultValue": "0", "type": "number" }
     *   ]
     * }
     */
    fun fromJson(json: String): CustomBlockBrick {
        val obj = JSONObject(json)
        val def = parseDefinition(obj)
        return createFromDefinition(def)
    }

    /**
     * Creates a brick from a JSON file on disk.
     */
    fun fromFile(file: File): CustomBlockBrick {
        require(file.exists() && file.canRead()) { "Cannot read block definition file: ${file.absolutePath}" }
        return fromJson(file.readText())
    }

    /**
     * Creates multiple bricks from a JSON array string.
     */
    fun fromJsonArray(jsonArray: String): List<CustomBlockBrick> {
        val array = JSONArray(jsonArray)
        val result = mutableListOf<CustomBlockBrick>()
        for (i in 0 until array.length()) {
            result.add(createFromDefinition(parseDefinition(array.getJSONObject(i))))
        }
        return result
    }

    /**
     * Convenience builder for code-based definitions.
     */
    fun create(
        id: String,
        category: String,
        name: String,
        actionId: String,
        parameters: Map<String, String> = emptyMap()
    ): CustomBlockBrick {
        val formulas = parameters.mapValues { (_, value) -> Formula(value) }
        return CustomBlockBrick(
            blockId = id,
            category = category,
            displayName = name,
            actionId = actionId,
            formulas = formulas
        )
    }

    fun createFromDefinition(def: BlockDefinition): CustomBlockBrick {
        val formulas = def.parameters.associate { param ->
            param.key to Formula(param.defaultValue)
        }
        return CustomBlockBrick(
            blockId = def.id,
            category = def.category,
            displayName = def.name,
            actionId = def.actionId,
            formulas = formulas
        )
    }

    private fun parseDefinition(obj: JSONObject): BlockDefinition {
        val params = mutableListOf<ParameterDef>()
        if (obj.has("parameters")) {
            val arr = obj.getJSONArray("parameters")
            for (i in 0 until arr.length()) {
                val p = arr.getJSONObject(i)
                params.add(
                    ParameterDef(
                        key = p.getString("key"),
                        defaultValue = p.optString("defaultValue", "0"),
                        type = p.optString("type", "number")
                    )
                )
            }
        }
        return BlockDefinition(
            id = obj.getString("id"),
            category = obj.optString("category", BlockRegistry.CATEGORY_CUSTOM),
            name = obj.getString("name"),
            actionId = obj.getString("actionId"),
            parameters = params
        )
    }

    /**
     * Runtime execution of a dynamic action identified by [actionId].
     * Dispatches to [KixPresetActions] for known actions.
     */
    fun executeAction(
        actionId: String,
        sprite: Sprite,
        sequence: ScriptSequenceAction,
        formulas: Map<String, Formula>
    ) {
        when (actionId) {
            // Layer actions
            "showLayer" -> {
                val layer = formulas["layer"]?.interpretInteger(sprite) ?: 0
                KixPresetActions.showLayer(layer)
            }
            "hideLayer" -> {
                val layer = formulas["layer"]?.interpretInteger(sprite) ?: 0
                KixPresetActions.hideLayer(layer)
            }
            "toggleLayerVisibility" -> {
                val layer = formulas["layer"]?.interpretInteger(sprite) ?: 0
                KixPresetActions.toggleLayerVisibility(layer)
            }
            "setLayerName" -> {
                val layer = formulas["layer"]?.interpretInteger(sprite) ?: 0
                val name = formulas["name"]?.interpretString(sprite) ?: "Layer"
                KixPresetActions.setLayerName(layer, name)
            }
            "setActorLayer" -> {
                val layer = formulas["layer"]?.interpretInteger(sprite) ?: 0
                KixPresetActions.setActorLayer(sprite, layer)
            }
            "bringLayerToFront" -> {
                val layer = formulas["layer"]?.interpretInteger(sprite) ?: 0
                KixPresetActions.bringLayerToFront(layer)
            }
            "sendLayerToBack" -> {
                val layer = formulas["layer"]?.interpretInteger(sprite) ?: 0
                KixPresetActions.sendLayerToBack(layer)
            }
            // Future actions will be added here
            else -> {
                // Unknown action - no-op (or log in real build)
            }
        }
    }
}

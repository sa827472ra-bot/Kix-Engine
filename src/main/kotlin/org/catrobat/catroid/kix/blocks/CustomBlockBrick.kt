/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2026 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * Kix Engine Mod - Generic Custom Block Brick
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
import org.catrobat.catroid.content.bricks.Brick
import org.catrobat.catroid.content.bricks.FormulaBrick
import org.catrobat.catroid.formulaeditor.Formula

/**
 * Generic, customizable brick that can be generated dynamically from JSON/String definitions.
 * Serves as the base for all Kix Engine custom blocks created via [CustomBlockFactory].
 */
open class CustomBlockBrick : FormulaBrick {

    private var blockId: String = ""
    private var category: String = BlockRegistry.CATEGORY_CUSTOM
    private var displayName: String = "Custom Block"
    private var actionId: String = ""
    private var parameterKeys: MutableList<String> = mutableListOf()

    constructor() : super()

    constructor(
        blockId: String,
        category: String,
        displayName: String,
        actionId: String,
        formulas: Map<String, Formula> = emptyMap()
    ) : this() {
        this.blockId = blockId
        this.category = category
        this.displayName = displayName
        this.actionId = actionId
        formulas.forEach { (key, formula) ->
            parameterKeys.add(key)
            // Map string keys to BrickField dynamically when possible
            addAllowedBrickField(resolveBrickField(key), 0)
            setFormulaWithBrickField(resolveBrickField(key), formula)
        }
    }

    fun getBlockId(): String = blockId
    fun getCategory(): String = category
    fun getDisplayName(): String = displayName
    fun getActionId(): String = actionId
    fun getParameterKeys(): List<String> = parameterKeys.toList()

    fun setBlockId(id: String) { blockId = id }
    fun setCategory(cat: String) { category = cat }
    fun setDisplayName(name: String) { displayName = name }
    fun setActionId(id: String) { actionId = id }

    override fun getViewResource(): Int {
        // Fallback layout; real projects should provide a dedicated layout per block
        return android.R.layout.simple_list_item_1
    }

    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        // Dispatch to the central action executor based on actionId
        val formulas = parameterKeys.associateWith { key ->
            getFormulaWithBrickField(resolveBrickField(key))
        }
        CustomBlockFactory.executeAction(actionId, sprite, sequence, formulas)
    }

    override fun clone(): Brick {
        val clone = CustomBlockBrick(
            blockId = this.blockId,
            category = this.category,
            displayName = this.displayName,
            actionId = this.actionId,
            formulas = parameterKeys.associateWith { key ->
                getFormulaWithBrickField(resolveBrickField(key))
            }
        )
        return clone
    }

    companion object {
        /**
         * Maps a string parameter key to a BrickField.
         * Falls back to a generic VALUE field when the key is unknown.
         */
        fun resolveBrickField(key: String): Brick.BrickField {
            return try {
                Brick.BrickField.valueOf(key.uppercase())
            } catch (e: IllegalArgumentException) {
                // Common fallbacks used in Catroid
                when (key.lowercase()) {
                    "layer", "layerindex", "layer_index" -> Brick.BrickField.BRICK_FIELD
                    "name", "layername" -> Brick.BrickField.VARIABLE
                    "x", "y", "value", "intensity", "duration" -> Brick.BrickField.VALUE
                    else -> Brick.BrickField.VALUE
                }
            }
        }
    }
}

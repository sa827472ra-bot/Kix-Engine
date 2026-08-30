/*
 * Kix Engine - SuperTextBrick (Fluent API mega block)
 * Copyright (C) 2026 Kix Engine Contributors | AGPL-3.0
 */
package org.catrobat.catroid.kix.bricks.text

import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.content.actions.ScriptSequenceAction
import org.catrobat.catroid.content.bricks.Brick
import org.catrobat.catroid.content.bricks.FormulaBrick
import org.catrobat.catroid.formulaeditor.Formula
import org.catrobat.catroid.kix.text.SuperTextAnimation
import org.catrobat.catroid.kix.text.SuperTextManager
import org.catrobat.catroid.kix.text.SuperTextStyle

/**
 * Single mega-block with Fluent API for rich text.
 *
 * Example (host code):
 * ```
 * SuperTextBrick("title")
 *   .text("Kix Engine")
 *   .color(0xFF00C853.toInt())
 *   .size(48f)
 *   .align(SuperTextStyle.Align.CENTER)
 *   .outline(0xFF000000.toInt(), 2f)
 *   .animate(SuperTextAnimation.TYPEWRITER, 1200)
 * ```
 */
class SuperTextBrick : FormulaBrick {

    private var textId: String = "default"
    private var pendingText: String = ""
    private var pendingColor: Int? = null
    private var pendingGradient: Pair<Int, Int>? = null
    private var pendingOutlineColor: Int? = null
    private var pendingOutlineWidth: Float? = null
    private var pendingFont: String? = null
    private var pendingSize: Float? = null
    private var pendingAlign: SuperTextStyle.Align? = null
    private var pendingRotation: Float? = null
    private var pendingScale: Pair<Float, Float>? = null
    private var pendingAlpha: Float? = null
    private var pendingPos: Pair<Float, Float>? = null
    private var pendingAnim: SuperTextAnimation? = null
    private var pendingAnimDuration: Long = 1000L
    private var pendingAnimLoop: Boolean = false

    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.VARIABLE, 0) // text id / content
    }

    constructor(id: String) : this() {
        this.textId = id
        setFormulaWithBrickField(Brick.BrickField.VARIABLE, Formula(id))
    }

    // ----- Fluent API -----

    fun id(id: String): SuperTextBrick {
        textId = id
        return this
    }

    fun text(value: String): SuperTextBrick {
        pendingText = value
        return this
    }

    fun color(argb: Int): SuperTextBrick {
        pendingColor = argb
        pendingGradient = null
        return this
    }

    fun gradient(startArgb: Int, endArgb: Int, vertical: Boolean = false): SuperTextBrick {
        pendingGradient = startArgb to endArgb
        return this
    }

    fun outline(argb: Int, width: Float): SuperTextBrick {
        pendingOutlineColor = argb
        pendingOutlineWidth = width
        return this
    }

    fun font(family: String): SuperTextBrick {
        pendingFont = family
        return this
    }

    fun size(px: Float): SuperTextBrick {
        pendingSize = px
        return this
    }

    fun align(alignment: SuperTextStyle.Align): SuperTextBrick {
        pendingAlign = alignment
        return this
    }

    fun rotation(degrees: Float): SuperTextBrick {
        pendingRotation = degrees
        return this
    }

    fun scale(sx: Float, sy: Float = sx): SuperTextBrick {
        pendingScale = sx to sy
        return this
    }

    fun alpha(a: Float): SuperTextBrick {
        pendingAlpha = a.coerceIn(0f, 1f)
        return this
    }

    fun position(x: Float, y: Float): SuperTextBrick {
        pendingPos = x to y
        return this
    }

    fun animate(
        animation: SuperTextAnimation,
        durationMs: Long = 1000L,
        loop: Boolean = false
    ): SuperTextBrick {
        pendingAnim = animation
        pendingAnimDuration = durationMs
        pendingAnimLoop = loop
        return this
    }

    // ----- Brick lifecycle -----

    override fun getViewResource(): Int = android.R.layout.simple_list_item_1

    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        applyToManager()
    }

    /** Applies fluent state immediately (also usable outside script sequences). */
    fun applyToManager(): SuperTextStyle {
        val style = SuperTextManager.get(textId) ?: SuperTextManager.create(textId, pendingText)
        if (pendingText.isNotEmpty()) style.text = pendingText
        pendingColor?.let {
            style.solidColor = it
            style.gradientStart = null
            style.gradientEnd = null
        }
        pendingGradient?.let { (a, b) ->
            style.gradientStart = a
            style.gradientEnd = b
        }
        pendingOutlineColor?.let { style.outlineColor = it }
        pendingOutlineWidth?.let { style.outlineWidth = it }
        pendingFont?.let { style.fontFamily = it }
        pendingSize?.let { style.fontSize = it }
        pendingAlign?.let { style.alignment = it }
        pendingRotation?.let { style.rotationDeg = it }
        pendingScale?.let { (sx, sy) ->
            style.scaleX = sx
            style.scaleY = sy
        }
        pendingAlpha?.let { style.alpha = it }
        pendingPos?.let { (x, y) ->
            style.x = x
            style.y = y
        }
        pendingAnim?.let { anim ->
            SuperTextManager.play(textId, anim, pendingAnimDuration, pendingAnimLoop)
        }
        return style
    }

    override fun clone(): Brick {
        val c = SuperTextBrick(textId)
        c.pendingText = pendingText
        c.pendingColor = pendingColor
        c.pendingGradient = pendingGradient
        c.pendingOutlineColor = pendingOutlineColor
        c.pendingOutlineWidth = pendingOutlineWidth
        c.pendingFont = pendingFont
        c.pendingSize = pendingSize
        c.pendingAlign = pendingAlign
        c.pendingRotation = pendingRotation
        c.pendingScale = pendingScale
        c.pendingAlpha = pendingAlpha
        c.pendingPos = pendingPos
        c.pendingAnim = pendingAnim
        c.pendingAnimDuration = pendingAnimDuration
        c.pendingAnimLoop = pendingAnimLoop
        return c
    }
}

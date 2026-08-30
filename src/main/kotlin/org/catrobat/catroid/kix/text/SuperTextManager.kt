/*
 * Kix Engine - SuperText Manager (optimized registry)
 * Copyright (C) 2026 Kix Engine Contributors | AGPL-3.0
 */
package org.catrobat.catroid.kix.text

import java.util.concurrent.ConcurrentHashMap

/**
 * Central store for SuperText instances keyed by id.
 * O(1) lookup; update loop only touches active animators.
 */
object SuperTextManager {

    data class Entry(
        val style: SuperTextStyle,
        var animator: SuperTextAnimator? = null,
        var visible: Boolean = true
    )

    private val entries = ConcurrentHashMap<String, Entry>()

    fun create(id: String, text: String = ""): SuperTextStyle {
        val style = SuperTextStyle(text = text)
        entries[id] = Entry(style)
        return style
    }

    fun get(id: String): SuperTextStyle? = entries[id]?.style

    fun getEntry(id: String): Entry? = entries[id]

    fun setVisible(id: String, visible: Boolean) {
        entries[id]?.visible = visible
    }

    fun remove(id: String) {
        entries.remove(id)
    }

    fun play(id: String, animation: SuperTextAnimation, durationMs: Long = 1000L, loop: Boolean = false) {
        val entry = entries[id] ?: return
        entry.animator = SuperTextAnimator(entry.style, animation, durationMs, loop)
    }

    fun stopAnimation(id: String) {
        entries[id]?.animator = null
    }

    /** Call once per frame. Only iterates entries with an active animator. */
    fun update(deltaMs: Long) {
        if (entries.isEmpty()) return
        val finished = ArrayList<String>(2)
        for ((id, entry) in entries) {
            val anim = entry.animator ?: continue
            val running = anim.update(deltaMs)
            if (!running) finished.add(id)
        }
        for (id in finished) {
            entries[id]?.animator = null
        }
    }

    fun visibleStyles(): List<SuperTextStyle> =
        entries.values.mapNotNull { if (it.visible) it.style else null }

    fun clear() {
        entries.clear()
    }

    fun size(): Int = entries.size
}

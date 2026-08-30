/*
 * Kix Engine - Collision Manager
 * Copyright (C) 2026 Kix Engine Contributors | AGPL-3.0
 */
package org.catrobat.catroid.kix.collision

import org.catrobat.catroid.content.Sprite
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Simple AABB collision detection with group filters and event callbacks.
 * Host stage should call [update] each frame with sprite positions/sizes.
 */
object CollisionManager {

    data class Bounds(
        var x: Float,
        var y: Float,
        var width: Float,
        var height: Float
    ) {
        fun intersects(other: Bounds): Boolean {
            return x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y
        }
    }

    data class Body(
        val sprite: Sprite,
        var bounds: Bounds,
        var group: String = "default",
        var enabled: Boolean = true
    )

    private val bodies = ConcurrentHashMap<Sprite, Body>()
    private val groupFilters = ConcurrentHashMap<String, MutableSet<String>>() // group -> groups it collides with
    private val callbacks = CopyOnWriteArrayList<(Sprite, Sprite) -> Unit>()
    private var detectionEnabled = true

    fun setDetectionEnabled(enabled: Boolean) {
        detectionEnabled = enabled
    }

    fun isDetectionEnabled(): Boolean = detectionEnabled

    fun register(sprite: Sprite, x: Float, y: Float, width: Float, height: Float, group: String = "default") {
        bodies[sprite] = Body(sprite, Bounds(x, y, width, height), group)
    }

    fun unregister(sprite: Sprite) {
        bodies.remove(sprite)
    }

    fun updateBounds(sprite: Sprite, x: Float, y: Float, width: Float, height: Float) {
        bodies[sprite]?.bounds = Bounds(x, y, width, height)
    }

    fun setGroup(sprite: Sprite, group: String) {
        bodies[sprite]?.group = group
    }

    /**
     * Restrict [groupA] to only collide with the listed groups.
     * If never set, a group collides with all groups.
     */
    fun setGroupFilter(group: String, collidesWith: Set<String>) {
        groupFilters[group] = collidesWith.toMutableSet()
    }

    fun clearGroupFilter(group: String) {
        groupFilters.remove(group)
    }

    fun onCollision(callback: (Sprite, Sprite) -> Unit) {
        callbacks.add(callback)
    }

    fun removeCallback(callback: (Sprite, Sprite) -> Unit) {
        callbacks.remove(callback)
    }

    /** Returns true if two sprites currently overlap (and pass filters). */
    fun check(a: Sprite, b: Sprite): Boolean {
        if (!detectionEnabled) return false
        val ba = bodies[a] ?: return false
        val bb = bodies[b] ?: return false
        if (!ba.enabled || !bb.enabled) return false
        if (!groupsAllowed(ba.group, bb.group)) return false
        return ba.bounds.intersects(bb.bounds)
    }

    /**
     * Broad-phase: test all pairs, fire callbacks for new overlaps.
     * Call once per frame from the stage.
     */
    fun update() {
        if (!detectionEnabled) return
        val list = bodies.values.filter { it.enabled }
        for (i in list.indices) {
            for (j in i + 1 until list.size) {
                val a = list[i]
                val b = list[j]
                if (!groupsAllowed(a.group, b.group)) continue
                if (a.bounds.intersects(b.bounds)) {
                    callbacks.forEach { runCatching { it(a.sprite, b.sprite) } }
                }
            }
        }
    }

    fun clear() {
        bodies.clear()
        groupFilters.clear()
        callbacks.clear()
        detectionEnabled = true
    }

    private fun groupsAllowed(groupA: String, groupB: String): Boolean {
        val filterA = groupFilters[groupA]
        val filterB = groupFilters[groupB]
        if (filterA != null && groupB !in filterA) return false
        if (filterB != null && groupA !in filterB) return false
        return true
    }
}

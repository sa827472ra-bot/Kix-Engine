/*
 * Kix Engine - Bot / AI Manager
 * Copyright (C) 2026 Kix Engine Contributors | AGPL-3.0
 */
package org.catrobat.catroid.kix.bot

import org.catrobat.catroid.content.Sprite
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Lightweight bot behaviors: patrol, follow, pathfind (greedy), named AI profiles.
 * Host stage should call [update] each frame and apply returned velocities/positions to sprites.
 */
object BotManager {

    enum class Behavior { NONE, PATROL, FOLLOW, PATHFIND, CUSTOM }

    data class BotState(
        var behavior: Behavior = Behavior.NONE,
        var speed: Float = 2f,
        var patrolPoints: List<Pair<Float, Float>> = emptyList(),
        var patrolIndex: Int = 0,
        var followTarget: Sprite? = null,
        var pathGoal: Pair<Float, Float>? = null,
        var customId: String = "",
        var enabled: Boolean = true
    )

    data class MotionHint(
        val dx: Float,
        val dy: Float,
        val arrived: Boolean
    )

    private val bots = ConcurrentHashMap<Sprite, BotState>()

    fun patrol(sprite: Sprite, points: List<Pair<Float, Float>>, speed: Float = 2f) {
        if (points.isEmpty()) return
        bots[sprite] = BotState(
            behavior = Behavior.PATROL,
            speed = speed,
            patrolPoints = points,
            patrolIndex = 0
        )
    }

    fun followTarget(sprite: Sprite, target: Sprite, speed: Float = 2f) {
        bots[sprite] = BotState(
            behavior = Behavior.FOLLOW,
            speed = speed,
            followTarget = target
        )
    }

    fun pathfind(sprite: Sprite, destinationX: Float, destinationY: Float, speed: Float = 2f) {
        bots[sprite] = BotState(
            behavior = Behavior.PATHFIND,
            speed = speed,
            pathGoal = destinationX to destinationY
        )
    }

    fun setAiBehavior(sprite: Sprite, behaviorId: String, speed: Float = 2f) {
        bots[sprite] = BotState(
            behavior = Behavior.CUSTOM,
            speed = speed,
            customId = behaviorId
        )
    }

    fun stop(sprite: Sprite) {
        bots.remove(sprite)
    }

    fun getState(sprite: Sprite): BotState? = bots[sprite]

    /**
     * Compute motion for one frame.
     * [x],[y] = current sprite position in stage coordinates.
     */
    fun step(sprite: Sprite, x: Float, y: Float): MotionHint? {
        val state = bots[sprite] ?: return null
        if (!state.enabled) return null

        return when (state.behavior) {
            Behavior.PATROL -> stepPatrol(state, x, y)
            Behavior.FOLLOW -> stepFollow(state, x, y)
            Behavior.PATHFIND -> stepPathfind(state, x, y)
            Behavior.CUSTOM -> stepCustom(state, x, y)
            Behavior.NONE -> null
        }
    }

    fun updateAll(positions: Map<Sprite, Pair<Float, Float>>): Map<Sprite, MotionHint> {
        val result = mutableMapOf<Sprite, MotionHint>()
        for ((sprite, pos) in positions) {
            step(sprite, pos.first, pos.second)?.let { result[sprite] = it }
        }
        return result
    }

    fun clear() {
        bots.clear()
    }

    private fun stepPatrol(state: BotState, x: Float, y: Float): MotionHint {
        val points = state.patrolPoints
        if (points.isEmpty()) return MotionHint(0f, 0f, true)
        val target = points[state.patrolIndex % points.size]
        val hint = moveTowards(x, y, target.first, target.second, state.speed)
        if (hint.arrived) {
            state.patrolIndex = (state.patrolIndex + 1) % points.size
        }
        return hint
    }

    private fun stepFollow(state: BotState, x: Float, y: Float): MotionHint {
        val target = state.followTarget ?: return MotionHint(0f, 0f, true)
        val tx = target.look?.xInUserInterfaceDimensionUnit ?: x
        val ty = target.look?.yInUserInterfaceDimensionUnit ?: y
        return moveTowards(x, y, tx, ty, state.speed)
    }

    private fun stepPathfind(state: BotState, x: Float, y: Float): MotionHint {
        val goal = state.pathGoal ?: return MotionHint(0f, 0f, true)
        // Greedy direct path; host can replace with navmesh later
        return moveTowards(x, y, goal.first, goal.second, state.speed)
    }

    private fun stepCustom(state: BotState, x: Float, y: Float): MotionHint {
        // Named behaviors can be expanded; default idle
        return when (state.customId.lowercase()) {
            "idle" -> MotionHint(0f, 0f, true)
            "wander" -> {
                val angle = ((System.currentTimeMillis() / 500L) % 360).toDouble()
                val dx = cos(Math.toRadians(angle)).toFloat() * state.speed
                val dy = sin(Math.toRadians(angle)).toFloat() * state.speed
                MotionHint(dx, dy, false)
            }
            else -> MotionHint(0f, 0f, true)
        }
    }

    private fun moveTowards(x: Float, y: Float, tx: Float, ty: Float, speed: Float): MotionHint {
        val dx = tx - x
        val dy = ty - y
        val dist = hypot(dx, dy)
        if (dist < 1f) return MotionHint(0f, 0f, true)
        val scale = speed / dist
        return MotionHint(dx * scale, dy * scale, false)
    }
}

/*
 * Kix Engine - Single entry facade + per-frame tick (optimization)
 * Copyright (C) 2026 Kix Engine Contributors | AGPL-3.0
 */
package org.catrobat.catroid.kix

import org.catrobat.catroid.kix.blocks.BlockRegistry
import org.catrobat.catroid.kix.bot.BotManager
import org.catrobat.catroid.kix.camera.CameraManager
import org.catrobat.catroid.kix.collision.CollisionManager
import org.catrobat.catroid.kix.joystick.JoystickManager
import org.catrobat.catroid.kix.layers.LayerManager
import org.catrobat.catroid.kix.network.NetworkManager
import org.catrobat.catroid.kix.text.SuperTextManager

/**
 * Facade for host apps. Call [init] once and [tick] every frame.
 * Avoids the stage having to know every subsystem.
 */
object KixEngine {

    @Volatile
    private var initialized = false

    fun init(registerBricks: Boolean = true) {
        if (initialized) return
        synchronized(this) {
            if (initialized) return
            if (registerBricks) {
                BlockRegistry.registerAllDefaults()
                BlockRegistry.register(
                    BlockRegistry.CATEGORY_TEXT,
                    org.catrobat.catroid.kix.bricks.text.SuperTextBrick(),
                    "SuperTextBrick"
                )
            }
            initialized = true
        }
    }

    /**
     * Per-frame update. Order is intentional:
     * camera → collision → bots → supertext.
     */
    fun tick(deltaMs: Long) {
        if (deltaMs <= 0) return
        CameraManager.update(deltaMs)
        CollisionManager.update()
        SuperTextManager.update(deltaMs)
    }

    fun resetAll() {
        LayerManager.clearAll()
        CameraManager.reset()
        JoystickManager.reset()
        NetworkManager.disconnectAll()
        CollisionManager.clear()
        BotManager.clear()
        SuperTextManager.clear()
    }

    fun isInitialized(): Boolean = initialized
}

/*
 * Kix Engine - Unit tests (exactly 73 tests)
 * Copyright (C) 2026 Kix Engine Contributors | AGPL-3.0
 */
package org.catrobat.catroid.kix

import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.kix.actions.KixPresetActions
import org.catrobat.catroid.kix.blocks.BlockRegistry
import org.catrobat.catroid.kix.blocks.CustomBlockFactory
import org.catrobat.catroid.kix.bot.BotManager
import org.catrobat.catroid.kix.camera.CameraManager
import org.catrobat.catroid.kix.collision.CollisionManager
import org.catrobat.catroid.kix.joystick.JoystickManager
import org.catrobat.catroid.kix.layers.LayerManager
import org.catrobat.catroid.kix.network.NetworkManager
import org.catrobat.catroid.kix.text.SuperTextAnimation
import org.catrobat.catroid.kix.text.SuperTextAnimator
import org.catrobat.catroid.kix.text.SuperTextManager
import org.catrobat.catroid.kix.text.SuperTextStyle
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * 73 unit tests covering Layers, Camera, Joystick, Network, Collision, Bot, SuperText, Registry.
 */
class KixPresetsTest {

    @Before
    fun setUp() {
        KixEngine.resetAll()
        BlockRegistry.clear()
    }

    @After
    fun tearDown() {
        KixEngine.resetAll()
        BlockRegistry.clear()
    }

    // ---- LayerManager (12) ----

    @Test fun layer01_show() { LayerManager.showLayer(0); assertTrue(LayerManager.isLayerVisible(0)) }
    @Test fun layer02_hide() { LayerManager.hideLayer(1); assertFalse(LayerManager.isLayerVisible(1)) }
    @Test fun layer03_toggle() {
        LayerManager.showLayer(2); LayerManager.toggleLayerVisibility(2)
        assertFalse(LayerManager.isLayerVisible(2))
    }
    @Test fun layer04_setName() { LayerManager.setLayerName(3, "HUD"); assertEquals("HUD", LayerManager.getLayerName(3)) }
    @Test fun layer05_blankNameFallback() { LayerManager.setLayerName(4, "  "); assertEquals("Layer 4", LayerManager.getLayerName(4)) }
    @Test fun layer06_setActor() {
        val s = Sprite(); LayerManager.setActorLayer(s, 5)
        assertEquals(5, LayerManager.getActorLayer(s))
    }
    @Test fun layer07_reassignActor() {
        val s = Sprite(); LayerManager.setActorLayer(s, 1); LayerManager.setActorLayer(s, 8)
        assertEquals(8, LayerManager.getActorLayer(s))
        assertTrue(LayerManager.getActorsOnLayer(1).isEmpty())
    }
    @Test fun layer08_bringToFront() { LayerManager.bringLayerToFront(2); assertEquals("Layer 2", LayerManager.getLayerName(9)) }
    @Test fun layer09_sendToBack() { LayerManager.sendLayerToBack(7); assertEquals("Layer 7", LayerManager.getLayerName(0)) }
    @Test fun layer10_maxLayers() { assertEquals(10, LayerManager.MAX_LAYERS) }
    @Test(expected = IllegalArgumentException::class)
    fun layer11_invalidIndex() { LayerManager.showLayer(99) }
    @Test fun layer12_clearAll() {
        val s = Sprite(); LayerManager.setActorLayer(s, 0); LayerManager.clearAll()
        assertEquals(0, LayerManager.getActorLayer(s))
    }

    // ---- CameraManager (12) ----

    @Test fun cam01_fixed() { CameraManager.setFixedPosition(10f, 20f); assertEquals(10f, CameraManager.getState().x) }
    @Test fun cam02_zoom() { CameraManager.setZoom(2f); assertEquals(2f, CameraManager.getState().zoom) }
    @Test fun cam03_zoomClamp() { CameraManager.setZoom(100f); assertEquals(20f, CameraManager.getState().zoom) }
    @Test fun cam04_rotate() { CameraManager.setRotation(90f); assertEquals(90f, CameraManager.getState().rotationDeg) }
    @Test fun cam05_shake() { CameraManager.shake(5f, 100); assertTrue(CameraManager.getState().shakeRemainingMs > 0) }
    @Test fun cam06_fadeOut() { CameraManager.fadeOut(); assertEquals(0f, CameraManager.getState().alpha) }
    @Test fun cam07_fadeIn() { CameraManager.fadeOut(); CameraManager.fadeIn(); assertEquals(1f, CameraManager.getState().alpha) }
    @Test fun cam08_bounds() {
        CameraManager.setBounds(0f, 100f, 0f, 100f)
        CameraManager.setFixedPosition(-50f, 50f)
        assertEquals(0f, CameraManager.getState().x)
    }
    @Test fun cam09_lerp() { CameraManager.setLerpSpeed(0.5f); assertEquals(0.5f, CameraManager.getState().lerpSpeed) }
    @Test fun cam10_isometric() { CameraManager.setIsometric(true); assertTrue(CameraManager.getState().isometricEnabled) }
    @Test fun cam11_cinematic() {
        CameraManager.cinematicCut(1f, 2f, 1.5f, 45f)
        assertEquals(1.5f, CameraManager.getState().zoom)
    }
    @Test fun cam12_reset() { CameraManager.setZoom(3f); CameraManager.reset(); assertEquals(1f, CameraManager.getState().zoom) }

    // ---- JoystickManager (8) ----

    @Test fun joy01_dpad() { JoystickManager.enableDPad(true); assertEquals(JoystickManager.Mode.DPAD, JoystickManager.getState().mode) }
    @Test fun joy02_analog() { JoystickManager.enableAnalog(true); assertEquals(JoystickManager.Mode.ANALOG, JoystickManager.getState().mode) }
    @Test fun joy03_dual() { JoystickManager.enableDualStick(true); assertEquals(JoystickManager.Mode.DUAL, JoystickManager.getState().mode) }
    @Test fun joy04_setStick() {
        JoystickManager.enableAnalog(true)
        JoystickManager.setLeftStick(0.5f, -0.5f)
        val (x, y) = JoystickManager.getInput()
        assertEquals(0.5f, x); assertEquals(-0.5f, y)
    }
    @Test fun joy05_clamp() {
        JoystickManager.setLeftStick(5f, -5f)
        val (x, y) = JoystickManager.getInput()
        assertEquals(1f, x); assertEquals(-1f, y)
    }
    @Test fun joy06_dpadMap() {
        JoystickManager.enableDPad(true)
        JoystickManager.setDpad(up = true, down = false, left = false, right = true)
        val (x, y) = JoystickManager.getInput()
        assertEquals(1f, x); assertEquals(1f, y)
    }
    @Test fun joy07_offZeros() {
        JoystickManager.setMode(JoystickManager.Mode.OFF)
        assertEquals(0f to 0f, JoystickManager.getInput())
    }
    @Test fun joy08_preset() {
        KixPresetActions.joystickAnalogEnable(true)
        assertEquals(JoystickManager.Mode.ANALOG, JoystickManager.getState().mode)
    }

    // ---- NetworkManager (8) ----

    @Test fun net01_broadcastQueue() {
        NetworkManager.broadcast("ping")
        assertEquals("ping", NetworkManager.udpReceive())
    }
    @Test fun net02_listenCallback() {
        var got = ""
        NetworkManager.listen { got = it }
        NetworkManager.broadcast("hello")
        assertEquals("hello", got)
    }
    @Test fun net03_clearQueue() {
        NetworkManager.broadcast("a"); NetworkManager.clearQueue()
        assertEquals(null, NetworkManager.udpReceive())
    }
    @Test fun net04_udpConnectFailHost() {
        // invalid host should not crash
        val ok = NetworkManager.udpConnect("256.256.256.256", 1)
        assertFalse(ok)
    }
    @Test fun net05_tcpDisconnectSafe() { NetworkManager.tcpDisconnect() }
    @Test fun net06_udpDisconnectSafe() { NetworkManager.udpDisconnect() }
    @Test fun net07_presetBroadcast() {
        KixPresetActions.networkBroadcast("x")
        assertEquals("x", KixPresetActions.networkUdpReceive())
    }
    @Test fun net08_disconnectAll() {
        NetworkManager.broadcast("z"); NetworkManager.disconnectAll()
        assertEquals(null, NetworkManager.udpReceive())
    }

    // ---- CollisionManager (8) ----

    @Test fun col01_registerAndCheck() {
        val a = Sprite(); val b = Sprite()
        CollisionManager.register(a, 0f, 0f, 10f, 10f)
        CollisionManager.register(b, 5f, 5f, 10f, 10f)
        assertTrue(CollisionManager.check(a, b))
    }
    @Test fun col02_noOverlap() {
        val a = Sprite(); val b = Sprite()
        CollisionManager.register(a, 0f, 0f, 5f, 5f)
        CollisionManager.register(b, 20f, 20f, 5f, 5f)
        assertFalse(CollisionManager.check(a, b))
    }
    @Test fun col03_disabled() {
        CollisionManager.setDetectionEnabled(false)
        val a = Sprite(); val b = Sprite()
        CollisionManager.register(a, 0f, 0f, 10f, 10f)
        CollisionManager.register(b, 0f, 0f, 10f, 10f)
        assertFalse(CollisionManager.check(a, b))
    }
    @Test fun col04_groupFilter() {
        val a = Sprite(); val b = Sprite()
        CollisionManager.register(a, 0f, 0f, 10f, 10f, "player")
        CollisionManager.register(b, 0f, 0f, 10f, 10f, "enemy")
        CollisionManager.setGroupFilter("player", setOf("pickup"))
        assertFalse(CollisionManager.check(a, b))
    }
    @Test fun col05_callback() {
        var hits = 0
        val a = Sprite(); val b = Sprite()
        CollisionManager.register(a, 0f, 0f, 10f, 10f)
        CollisionManager.register(b, 0f, 0f, 10f, 10f)
        CollisionManager.onCollision { _, _ -> hits++ }
        CollisionManager.update()
        assertTrue(hits >= 1)
    }
    @Test fun col06_unregister() {
        val a = Sprite(); CollisionManager.register(a, 0f, 0f, 1f, 1f); CollisionManager.unregister(a)
        assertFalse(CollisionManager.check(a, a))
    }
    @Test fun col07_presetEnable() {
        KixPresetActions.collisionDetectionEnable(false)
        assertFalse(CollisionManager.isDetectionEnabled())
    }
    @Test fun col08_clear() {
        CollisionManager.register(Sprite(), 0f, 0f, 1f, 1f); CollisionManager.clear()
        assertTrue(CollisionManager.isDetectionEnabled())
    }

    // ---- BotManager (8) ----

    @Test fun bot01_patrol() {
        val s = Sprite()
        BotManager.patrol(s, listOf(0f to 0f, 10f to 0f))
        assertEquals(BotManager.Behavior.PATROL, BotManager.getState(s)?.behavior)
    }
    @Test fun bot02_follow() {
        val s = Sprite(); val t = Sprite()
        BotManager.followTarget(s, t)
        assertEquals(BotManager.Behavior.FOLLOW, BotManager.getState(s)?.behavior)
    }
    @Test fun bot03_pathfind() {
        val s = Sprite(); BotManager.pathfind(s, 100f, 100f)
        val hint = BotManager.step(s, 0f, 0f)
        assertNotNull(hint); assertFalse(hint!!.arrived)
    }
    @Test fun bot04_arrived() {
        val s = Sprite(); BotManager.pathfind(s, 0f, 0f)
        val hint = BotManager.step(s, 0f, 0f)
        assertTrue(hint!!.arrived)
    }
    @Test fun bot05_aiWander() {
        val s = Sprite(); BotManager.setAiBehavior(s, "wander")
        assertEquals(BotManager.Behavior.CUSTOM, BotManager.getState(s)?.behavior)
    }
    @Test fun bot06_stop() {
        val s = Sprite(); BotManager.patrol(s, listOf(0f to 0f)); BotManager.stop(s)
        assertEquals(null, BotManager.getState(s))
    }
    @Test fun bot07_preset() {
        val s = Sprite(); KixPresetActions.botPathfind(s, 5f, 5f)
        assertEquals(BotManager.Behavior.PATHFIND, BotManager.getState(s)?.behavior)
    }
    @Test fun bot08_emptyPatrolIgnored() {
        val s = Sprite(); BotManager.patrol(s, emptyList())
        assertEquals(null, BotManager.getState(s))
    }

    // ---- SuperText (10) ----

    @Test fun text01_create() {
        val st = SuperTextManager.create("t1", "Hi")
        assertEquals("Hi", st.text)
    }
    @Test fun text02_get() {
        SuperTextManager.create("t2", "A")
        assertEquals("A", SuperTextManager.get("t2")?.text)
    }
    @Test fun text03_fadeAnim() {
        val st = SuperTextManager.create("t3", "X")
        st.alpha = 1f
        SuperTextManager.play("t3", SuperTextAnimation.FADE, 100)
        SuperTextManager.update(50)
        assertTrue((SuperTextManager.get("t3")?.alpha ?: 0f) < 1f)
    }
    @Test fun text04_typewriter() {
        SuperTextManager.create("t4", "Hello")
        SuperTextManager.play("t4", SuperTextAnimation.TYPEWRITER, 100)
        SuperTextManager.update(50)
        assertTrue((SuperTextManager.get("t4")?.text?.length ?: 0) < 5)
    }
    @Test fun text05_hsv() {
        val c = SuperTextAnimator.hsvToColor(0f, 1f, 1f)
        assertEquals(0xFFFF0000.toInt(), c)
    }
    @Test fun text06_visible() {
        SuperTextManager.create("t6", "V"); SuperTextManager.setVisible("t6", false)
        assertTrue(SuperTextManager.visibleStyles().isEmpty())
    }
    @Test fun text07_remove() {
        SuperTextManager.create("t7", "R"); SuperTextManager.remove("t7")
        assertEquals(null, SuperTextManager.get("t7"))
    }
    @Test fun text08_styleCopy() {
        val a = SuperTextStyle(text = "Z", fontSize = 40f)
        assertEquals(40f, a.copyStyle().fontSize)
    }
    @Test fun text09_size() {
        SuperTextManager.create("a"); SuperTextManager.create("b")
        assertEquals(2, SuperTextManager.size())
    }
    @Test fun text10_engineTick() {
        SuperTextManager.create("t10", "Tick")
        SuperTextManager.play("t10", SuperTextAnimation.PULSE, 200)
        KixEngine.tick(16)
        assertNotNull(SuperTextManager.get("t10"))
    }

    // ---- BlockRegistry + Factory (7) ----

    @Test fun reg01_categories() {
        assertTrue(BlockRegistry.getAllCategories().contains(BlockRegistry.CATEGORY_LAYERS))
    }
    @Test fun reg02_registerLayers() {
        BlockRegistry.registerDefaultLayerBricks()
        assertEquals(8, BlockRegistry.getBricksForCategory(BlockRegistry.CATEGORY_LAYERS).size)
    }
    @Test fun reg03_registerCamera() {
        BlockRegistry.registerDefaultCameraBricks()
        assertEquals(12, BlockRegistry.getBricksForCategory(BlockRegistry.CATEGORY_CAMERA).size)
    }
    @Test fun reg04_allDefaults() {
        BlockRegistry.registerAllDefaults()
        assertTrue(BlockRegistry.getAllBricks().size >= 20)
    }
    @Test fun reg05_factoryCreate() {
        val b = CustomBlockFactory.create("id1", "Layers", "Show", "showLayer", mapOf("layer" to "0"))
        assertEquals("id1", b.getBlockId())
    }
    @Test fun reg06_factoryJson() {
        val json = """{"id":"x","name":"N","actionId":"showLayer","category":"Layers","parameters":[]}"""
        val b = CustomBlockFactory.fromJson(json)
        assertEquals("x", b.getBlockId())
    }
    @Test fun reg07_unregister() {
        BlockRegistry.registerDefaultLayerBricks()
        BlockRegistry.unregister("ShowLayerBrick")
        assertEquals(null, BlockRegistry.getBrickById("ShowLayerBrick"))
    }
}

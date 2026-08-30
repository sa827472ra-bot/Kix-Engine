/*
 * Kix Engine - Network Bricks (10) TCP/UDP + broadcast/listen
 * Copyright (C) 2026 Kix Engine Contributors | AGPL-3.0
 */
package org.catrobat.catroid.kix.bricks.network

import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.content.actions.ScriptSequenceAction
import org.catrobat.catroid.content.bricks.Brick
import org.catrobat.catroid.content.bricks.FormulaBrick
import org.catrobat.catroid.formulaeditor.Formula
import org.catrobat.catroid.kix.actions.KixPresetActions

class NetworkUDPConnectBrick : FormulaBrick {
    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.VARIABLE, 0) // host
        addAllowedBrickField(Brick.BrickField.VALUE, 0)     // port
    }
    constructor(host: String, port: Int) : this() {
        setFormulaWithBrickField(Brick.BrickField.VARIABLE, Formula(host))
        setFormulaWithBrickField(Brick.BrickField.VALUE, Formula(port))
    }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        val host = getFormulaWithBrickField(Brick.BrickField.VARIABLE).interpretString(sprite)
        val port = getFormulaWithBrickField(Brick.BrickField.VALUE).interpretInteger(sprite)
        KixPresetActions.networkUdpConnect(host, port)
    }
    override fun clone(): Brick {
        val c = NetworkUDPConnectBrick()
        c.setFormulaWithBrickField(Brick.BrickField.VARIABLE, getFormulaWithBrickField(Brick.BrickField.VARIABLE))
        c.setFormulaWithBrickField(Brick.BrickField.VALUE, getFormulaWithBrickField(Brick.BrickField.VALUE))
        return c
    }
}

class NetworkUDPSendBrick : FormulaBrick {
    constructor() : super() { addAllowedBrickField(Brick.BrickField.VARIABLE, 0) }
    constructor(data: String) : this() { setFormulaWithBrickField(Brick.BrickField.VARIABLE, Formula(data)) }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        KixPresetActions.networkUdpSend(getFormulaWithBrickField(Brick.BrickField.VARIABLE).interpretString(sprite))
    }
    override fun clone(): Brick = NetworkUDPSendBrick().also {
        it.setFormulaWithBrickField(Brick.BrickField.VARIABLE, getFormulaWithBrickField(Brick.BrickField.VARIABLE))
    }
}

class NetworkUDPReceiveBrick : FormulaBrick {
    constructor() : super()
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        KixPresetActions.networkUdpReceive()
    }
    fun evaluate(): String? = KixPresetActions.networkUdpReceive()
    override fun clone(): Brick = NetworkUDPReceiveBrick()
}

class NetworkUDPDisconnectBrick : FormulaBrick {
    constructor() : super()
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        KixPresetActions.networkUdpDisconnect()
    }
    override fun clone(): Brick = NetworkUDPDisconnectBrick()
}

class NetworkTCPConnectBrick : FormulaBrick {
    constructor() : super() {
        addAllowedBrickField(Brick.BrickField.VARIABLE, 0)
        addAllowedBrickField(Brick.BrickField.VALUE, 0)
    }
    constructor(host: String, port: Int) : this() {
        setFormulaWithBrickField(Brick.BrickField.VARIABLE, Formula(host))
        setFormulaWithBrickField(Brick.BrickField.VALUE, Formula(port))
    }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        val host = getFormulaWithBrickField(Brick.BrickField.VARIABLE).interpretString(sprite)
        val port = getFormulaWithBrickField(Brick.BrickField.VALUE).interpretInteger(sprite)
        KixPresetActions.networkTcpConnect(host, port)
    }
    override fun clone(): Brick {
        val c = NetworkTCPConnectBrick()
        c.setFormulaWithBrickField(Brick.BrickField.VARIABLE, getFormulaWithBrickField(Brick.BrickField.VARIABLE))
        c.setFormulaWithBrickField(Brick.BrickField.VALUE, getFormulaWithBrickField(Brick.BrickField.VALUE))
        return c
    }
}

class NetworkTCPSendBrick : FormulaBrick {
    constructor() : super() { addAllowedBrickField(Brick.BrickField.VARIABLE, 0) }
    constructor(data: String) : this() { setFormulaWithBrickField(Brick.BrickField.VARIABLE, Formula(data)) }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        KixPresetActions.networkTcpSend(getFormulaWithBrickField(Brick.BrickField.VARIABLE).interpretString(sprite))
    }
    override fun clone(): Brick = NetworkTCPSendBrick().also {
        it.setFormulaWithBrickField(Brick.BrickField.VARIABLE, getFormulaWithBrickField(Brick.BrickField.VARIABLE))
    }
}

class NetworkTCPReceiveBrick : FormulaBrick {
    constructor() : super()
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        KixPresetActions.networkTcpReceive()
    }
    fun evaluate(): String? = KixPresetActions.networkTcpReceive()
    override fun clone(): Brick = NetworkTCPReceiveBrick()
}

class NetworkTCPDisconnectBrick : FormulaBrick {
    constructor() : super()
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        KixPresetActions.networkTcpDisconnect()
    }
    override fun clone(): Brick = NetworkTCPDisconnectBrick()
}

class NetworkBroadcastBrick : FormulaBrick {
    constructor() : super() { addAllowedBrickField(Brick.BrickField.VARIABLE, 0) }
    constructor(message: String) : this() { setFormulaWithBrickField(Brick.BrickField.VARIABLE, Formula(message)) }
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        KixPresetActions.networkBroadcast(getFormulaWithBrickField(Brick.BrickField.VARIABLE).interpretString(sprite))
    }
    override fun clone(): Brick = NetworkBroadcastBrick().also {
        it.setFormulaWithBrickField(Brick.BrickField.VARIABLE, getFormulaWithBrickField(Brick.BrickField.VARIABLE))
    }
}

class NetworkListenBrick : FormulaBrick {
    constructor() : super()
    override fun getViewResource(): Int = android.R.layout.simple_list_item_1
    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        // Host should register a script trigger; here we only ensure listener infrastructure exists
        KixPresetActions.networkListen { /* no-op placeholder */ }
    }
    override fun clone(): Brick = NetworkListenBrick()
}

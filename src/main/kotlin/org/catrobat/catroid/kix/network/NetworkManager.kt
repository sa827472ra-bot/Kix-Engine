/*
 * Kix Engine - Network Manager (TCP / UDP)
 * Copyright (C) 2026 Kix Engine Contributors | AGPL-3.0
 */
package org.catrobat.catroid.kix.network

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread

/**
 * Lightweight TCP/UDP client helper for Kix bricks.
 * Runs IO on background threads; received messages are queued for the stage thread.
 */
object NetworkManager {

    private val udpSocket = AtomicReference<DatagramSocket?>(null)
    private val udpRemote = AtomicReference<Pair<InetAddress, Int>?>(null)
    private val tcpSocket = AtomicReference<Socket?>(null)
    private val tcpWriter = AtomicReference<BufferedWriter?>(null)
    private val tcpReader = AtomicReference<BufferedReader?>(null)

    private val receiveQueue = ConcurrentLinkedQueue<String>()
    private val listeners = ConcurrentLinkedQueue<(String) -> Unit>()
    private val udpListening = AtomicBoolean(false)
    private val tcpListening = AtomicBoolean(false)

    // ----- UDP -----

    fun udpConnect(host: String, port: Int): Boolean {
        return try {
            udpDisconnect()
            val addr = InetAddress.getByName(host)
            val socket = DatagramSocket()
            socket.soTimeout = 50
            udpSocket.set(socket)
            udpRemote.set(addr to port)
            startUdpListener(socket)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun udpSend(data: String): Boolean {
        val socket = udpSocket.get() ?: return false
        val remote = udpRemote.get() ?: return false
        return try {
            val bytes = data.toByteArray(StandardCharsets.UTF_8)
            val packet = DatagramPacket(bytes, bytes.size, remote.first, remote.second)
            socket.send(packet)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun udpReceive(): String? = receiveQueue.poll()

    fun udpDisconnect() {
        udpListening.set(false)
        try { udpSocket.getAndSet(null)?.close() } catch (_: Exception) {}
        udpRemote.set(null)
    }

    // ----- TCP -----

    fun tcpConnect(host: String, port: Int): Boolean {
        return try {
            tcpDisconnect()
            val socket = Socket(host, port)
            socket.soTimeout = 50
            tcpSocket.set(socket)
            tcpWriter.set(BufferedWriter(OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)))
            tcpReader.set(BufferedReader(InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)))
            startTcpListener()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun tcpSend(data: String): Boolean {
        val writer = tcpWriter.get() ?: return false
        return try {
            writer.write(data)
            writer.newLine()
            writer.flush()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun tcpReceive(): String? = receiveQueue.poll()

    fun tcpDisconnect() {
        tcpListening.set(false)
        try { tcpWriter.getAndSet(null)?.close() } catch (_: Exception) {}
        try { tcpReader.getAndSet(null)?.close() } catch (_: Exception) {}
        try { tcpSocket.getAndSet(null)?.close() } catch (_: Exception) {}
    }

    // ----- Broadcast / Listen (in-process bus for multi-sprite / multiplayer scripts) -----

    fun broadcast(message: String) {
        receiveQueue.offer(message)
        listeners.forEach { runCatching { it(message) } }
    }

    fun listen(callback: (String) -> Unit) {
        listeners.add(callback)
    }

    fun removeListener(callback: (String) -> Unit) {
        listeners.remove(callback)
    }

    fun clearQueue() {
        receiveQueue.clear()
    }

    fun disconnectAll() {
        udpDisconnect()
        tcpDisconnect()
        clearQueue()
        listeners.clear()
    }

    private fun startUdpListener(socket: DatagramSocket) {
        if (!udpListening.compareAndSet(false, true)) return
        thread(name = "kix-udp-rx", isDaemon = true) {
            val buf = ByteArray(4096)
            while (udpListening.get()) {
                try {
                    val packet = DatagramPacket(buf, buf.size)
                    socket.receive(packet)
                    val msg = String(packet.data, 0, packet.length, StandardCharsets.UTF_8)
                    receiveQueue.offer(msg)
                    listeners.forEach { runCatching { it(msg) } }
                } catch (_: Exception) {
                    // timeout / closed
                }
            }
        }
    }

    private fun startTcpListener() {
        if (!tcpListening.compareAndSet(false, true)) return
        thread(name = "kix-tcp-rx", isDaemon = true) {
            val reader = tcpReader.get() ?: return@thread
            while (tcpListening.get()) {
                try {
                    val line = reader.readLine() ?: break
                    receiveQueue.offer(line)
                    listeners.forEach { runCatching { it(line) } }
                } catch (_: Exception) {
                    // timeout / closed
                }
            }
            tcpListening.set(false)
        }
    }
}

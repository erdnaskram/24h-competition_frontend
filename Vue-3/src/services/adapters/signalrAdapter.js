/**
 * signalrAdapter.js
 *
 * Konkrete Implementierung der Backend-Kommunikation über das bestehende
 * ASP.NET SignalR-Backend + REST-Endpunkte.
 *
 * Kein Component darf dieses Modul direkt importieren —
 * immer nur über src/services/participantService.js.
 */

import * as signalR from '@microsoft/signalr'
import { useSwimmerStore } from '../../store/SwimmerStore.js'
import { useLaneStore }    from '../../store/LaneStore.js'
import { toFrontend, toCheckinSwimmer, toBackendProperties, resultToFrontend } from '../mappers/participantMapper.js'

const BASE_URL = import.meta.env.VITE_BACKEND_URL ?? ''

// ─── Hub-Instanz (Singleton) ──────────────────────────────────────────────────

let hub     = null
let hubMode = null  // 'lane' | 'checkin' | null

function buildHub() {
    const h = new signalR.HubConnectionBuilder()
        .withUrl(`${BASE_URL}/participantHub`)
        .withAutomaticReconnect([0, 2000, 5000, 10000, 30000])
        .configureLogging(signalR.LogLevel.Debug)
        .build()

    h.onreconnecting(() => {
        useSwimmerStore().setConnected(false)
    })
    h.onreconnected(() => {
        useSwimmerStore().setConnected(true)
    })
    h.onclose((err) => {
        useSwimmerStore().setConnectionError(err?.message ?? 'Verbindung getrennt')
    })
    return h
}

function getHub() {
    if (!hub) hub = buildHub()
    return hub
}

/**
 * Trennt die Verbindung und erzwingt beim nächsten Aufruf eine neue Verbindung.
 * Wird aufgerufen wenn zwischen Bahn-Ansicht und Check-In-Ansicht gewechselt wird,
 * da das Backend pro Verbindung nur einen Modus (lane/checkin) kennt.
 */
async function resetHub() {
    if (hub && hub.state !== signalR.HubConnectionState.Disconnected) {
        try { await hub.stop() } catch { /* ignorieren */ }
    }
    hub     = null
    hubMode = null
    useSwimmerStore().setConnected(false)
}

async function ensureConnected(mode) {
    if (hubMode !== null && hubMode !== mode) {
        await resetHub()
    }
    const h = getHub()
    if (h.state === signalR.HubConnectionState.Disconnected) {
        useSwimmerStore().setConnecting()
        try {
            await h.start()
        } catch (err) {
            useSwimmerStore().setConnectionError(err?.message ?? 'Verbindung fehlgeschlagen')
            throw err
        }
    }
    useSwimmerStore().setConnected(true)
    hubMode = mode
}

// ─── Bahn-Ansicht ─────────────────────────────────────────────────────────────

/**
 * Registriert den Client für eine Bahn.
 * Der Server schickt daraufhin alle aktuellen Schwimmer via addParticipant.
 */
async function registerLane(laneId) {
    const h = getHub()
    const swimmerStore = useSwimmerStore()

    // Events registrieren (erst einmal, vorherige Handler entfernen)
    h.off('addParticipant')
    h.off('removeParticipant')

    h.on('addParticipant', (participant) => {
        swimmerStore.upsertSwimmer(toFrontend(participant))
    })

    h.on('removeParticipant', (participantId) => {
        swimmerStore.removeSwimmer(participantId)
    })

    await ensureConnected('lane')
    swimmerStore.reset()
    await h.invoke('register', laneId)
}

/** Länge zählen — 1 Aufruf = 2 Bahnen = 50 m im Backend */
async function countLane(laneId, swimmerId) {
    await getHub().invoke('countLane', laneId, swimmerId)
}

/** Schwimmer geht auf Pause */
async function setInactive(laneId, swimmerId) {
    await getHub().invoke('setInactive', laneId, swimmerId)
}

/** Schwimmer kehrt zurück ins Wasser */
async function setActive(laneId, swimmerId) {
    await getHub().invoke('setActive', laneId, swimmerId)
}

/** Schwimmer verlässt die Bahn komplett */
async function leaveLane(laneId, swimmerId) {
    await getHub().invoke('leaveLane', laneId, swimmerId)
}

/**
 * Erkennungsmerkmale speichern.
 * Erwartet ein Frontend-Swimmer-Objekt, konvertiert intern via Mapper.
 */
async function saveProperties(laneId, swimmer) {
    const p = toBackendProperties(swimmer)
    await getHub().invoke(
        'saveProperties',
        laneId,
        swimmer.id,
        p.clothingType,
        p.clothingColor,
        p.googlesColor,
        p.hairColor,
        p.hatColor,
        p.tattoo,
        p.headphones,
        p.remarks,
    )
}

/** Verbindung und Store aufräumen (beim Verlassen der Bahn-Seite) */
async function unregisterLane() {
    const h = getHub()
    h.off('addParticipant')
    h.off('removeParticipant')
    useSwimmerStore().reset()
    // Verbindung offen lassen — wird beim nächsten registerLane/Checkin wiederverwendet
}

// ─── Check-In ─────────────────────────────────────────────────────────────────

/**
 * Registriert den Client als Check-In-Terminal.
 * Der Server schickt updateLaneInfo und updateTotalLanes Events.
 */
async function registerCheckin() {
    const h = getHub()
    const swimmerStore = useSwimmerStore()
    const laneStore    = useLaneStore()

    h.off('updateParticipant')
    h.off('updateLaneInfo')
    h.off('updateTotalLanes')

    h.on('updateParticipant', (participant) => {
        swimmerStore.upsertSwimmer(toFrontend(participant))
    })

    h.on('updateLaneInfo', (laneNo, info) => {
        swimmerStore.updateLaneInfo(laneNo, info)
    })

    h.on('updateTotalLanes', (totalLanes) => {
        swimmerStore.setTotalLanes(totalLanes)
    })

    await ensureConnected('checkin')
    await h.invoke('registerCheckin')
}

/** Schwimmer einer Bahn zuweisen */
async function assignToLane(laneId, swimmerId) {
    await getHub().invoke('assignToLane', laneId, swimmerId)
}

/** Suche nach Anfangsbuchstaben */
async function searchByPrefix(letter) {
    const res = await fetch(`${BASE_URL}/checkin/SearchByPrefix/${encodeURIComponent(letter)}`, {
        method: 'POST',
    })
    if (!res.ok) throw new Error(`SearchByPrefix fehlgeschlagen: ${res.status}`)
    const data = await res.json()
    return data.map(toCheckinSwimmer)
}

/** Live-Suche bei Eingabe */
async function searchAsYouType(query) {
    const res = await fetch(`${BASE_URL}/checkin/SearchAsYouType?filter=${encodeURIComponent(query)}`, {
        method: 'POST',
    })
    if (!res.ok) throw new Error(`SearchAsYouType fehlgeschlagen: ${res.status}`)
    const data = await res.json()
    return data.map(toCheckinSwimmer)
}

// ─── Ergebnisanzeige ──────────────────────────────────────────────────────────

/** Ergebnisse abrufen (wird alle 60 s aufgerufen) */
async function fetchResults() {
    const res = await fetch(`${BASE_URL}/results`, { method: 'POST' })
    if (!res.ok) throw new Error(`Ergebnisse laden fehlgeschlagen: ${res.status}`)
    const data = await res.json()
    return data.map(resultToFrontend)
}

// ─── Export ───────────────────────────────────────────────────────────────────

export const signalrAdapter = {
    // Bahn-Ansicht
    registerLane,
    unregisterLane,
    countLane,
    setActive,
    setInactive,
    leaveLane,
    saveProperties,
    // Check-In
    registerCheckin,
    assignToLane,
    searchByPrefix,
    searchAsYouType,
    // Ergebnisse
    fetchResults,
}

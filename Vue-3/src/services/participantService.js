/**
 * participantService.js
 *
 * Einzige Import-Stelle für die gesamte Backend-Kommunikation.
 * Views und Store importieren ausschließlich von hier — nie direkt den Adapter.
 *
 * Backend wechseln = nur die eine aktive Zeile unten tauschen.
 *
 * Verfügbare Methoden:
 *
 *   Bahn-Ansicht:
 *     registerLane(laneId)               – Bahn anmelden, Events starten
 *     unregisterLane()                   – Aufräumen beim Verlassen
 *     countLane(laneId, swimmerId)       – Länge zählen (= 2 Bahnen = 50 m)
 *     setActive(laneId, swimmerId)       – Schwimmer aktiv setzen
 *     setInactive(laneId, swimmerId)     – Schwimmer pausieren
 *     leaveLane(laneId, swimmerId)       – Schwimmer von Bahn entfernen
 *     saveProperties(laneId, swimmer)    – Erkennungsmerkmale speichern
 *
 *   Check-In:
 *     registerCheckin()                  – Check-In Terminal anmelden
 *     assignToLane(laneId, swimmerId)    – Schwimmer zuweisen
 *     searchByPrefix(letter)             – Suche nach Anfangsbuchstabe
 *     searchAsYouType(query)             – Live-Suche
 *
 *   Ergebnisse:
 *     fetchResults()                     – Einmaliger Abruf (alle 60 s aufrufen)
 */

import { signalrAdapter } from './adapters/signalrAdapter.js'
// import { mockAdapter } from './adapters/mockAdapter.js'   // zum lokalen Entwickeln ohne Backend

export const participantService = signalrAdapter

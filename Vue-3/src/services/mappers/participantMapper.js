/**
 * participantMapper.js
 *
 * Einzige Stelle, die weiß wie Backend-Felder auf Frontend-Felder zeigen.
 * Backend wechseln = nur diese Datei anpassen.
 *
 * Backend-Einheit: 1 Bahn = 25 m, 1 Zähler-Klick = 2 Bahnen = 50 m
 * Frontend-Einheit: swimDistance in Metern
 */

// ─── Lookup-Tabellen ──────────────────────────────────────────────────────────

const GENDER_TO_FRONTEND = {
    'm': 'male',
    'f': 'female',
    'd': 'diverse',
}

const GENDER_TO_BACKEND = {
    'male': 'm',
    'female': 'f',
    'diverse': 'm',  // Backend kennt kein 'd' – Fallback bis neues Backend kommt
}

const CLOTHING_TYPE_TO_FRONTEND = {
    'Shorts': 'board-shorts',
    'Trunks': 'short-pants',
    'SwimSuit': 'swimsuit',
    'Bikini': 'bikini',
}

const CLOTHING_TYPE_TO_BACKEND = {
    'board-shorts': 'Shorts',
    'short-pants': 'Trunks',
    'swimsuit': 'SwimSuit',
    'bikini': 'Bikini',
}

// ─── Hilfsfunktionen ─────────────────────────────────────────────────────────

/** Teilt "Max Mustermann" in { first: "Max", last: "Mustermann" } */
function splitName(fullName = '') {
    const parts = fullName.trim().split(' ')
    const last = parts.pop() ?? ''
    const first = parts.join(' ')
    return {first, last}
}

/** Farb-Leerstring → 'none', sonst unverändert */
function colorOrNone(value) {
    return value && value !== 'none' ? value : 'none'
}

/** 'none' → '', sonst unverändert (für Backend) */
function noneToEmpty(value) {
    return value === 'none' ? '' : (value ?? '')
}

// ─── Haupt-Mapper ─────────────────────────────────────────────────────────────

/**
 * Konvertiert ein Backend-Participant-Objekt in ein Frontend-Swimmer-Objekt.
 *
 * @param {object} p  Backend participant
 * @returns {object}  Frontend swimmer
 */
export function toFrontend(p) {
    return {
        id: p.id,
        swimmerName: splitName(p.name),
        age: p.age ?? 0,
        gender: GENDER_TO_FRONTEND[p.gender] ?? 'diverse',
        swimDistance: (p.lanes ?? 0) * 25,       // Bahnen × 25 m
        team: '',                          // Backend hat kein team-Feld
        family: '',                          // Backend hat kein family-Feld
        isActive: p.isActive ?? false,
        isMinimized: false,
        lane: p.lane ?? null,              // zugewiesene Bahn (Check-In)
        averageTimeSeconds: p.averageTimeSeconds || null,
        lastCountTime:      Date.now() - (p.lastChangesSecondsAgo ?? 0) * 1000,
        characteristics: {
            swimwearType: CLOTHING_TYPE_TO_FRONTEND[p.clothingType] ?? 'board-shorts',
            swimwearColor: colorOrNone(p.clothingColor),
            googles: colorOrNone(p.googlesColor),
            swimCap: colorOrNone(p.hatColor),
            hair: colorOrNone(p.hairColor),
            tattoo: p.tattoo ?? false,
            headphones: p.headphones ?? false,
            notes: p.remarks ?? '',
        },
    }
}

/**
 * Extrahiert die Erkennungsmerkmale eines Frontend-Swimmers als flache
 * Backend-Parameter für hub.invoke('saveProperties', ...).
 *
 * Rückgabe in der Reihenfolge die das Backend erwartet:
 * clothingType, clothingColor, googlesColor, hairColor, hatColor, tattoo, headphones, remarks
 *
 * @param {object} swimmer  Frontend swimmer
 * @returns {object}
 */
export function toBackendProperties(swimmer) {
    const c = swimmer.characteristics
    return {
        clothingType: CLOTHING_TYPE_TO_BACKEND[c.swimwearType] ?? 'Trunks',
        clothingColor: noneToEmpty(c.swimwearColor),
        googlesColor: noneToEmpty(c.googles),
        hairColor: noneToEmpty(c.hair),
        hatColor: noneToEmpty(c.swimCap),
        tattoo: c.tattoo ?? false,
        headphones: c.headphones ?? false,
        remarks: c.notes ?? '',
    }
}

/**
 * Konvertiert ein Backend-Participant in das flache Format das die
 * CheckIn-Komponenten (CheckInSwimmerList, CheckInAssignDialog) erwarten.
 *
 * @param {object} p  Backend participant
 * @returns {object}  CheckIn swimmer
 */
export function toCheckinSwimmer(p) {
    return {
        id: p.id,
        firstName: splitName(p.name).first,
        lastName: splitName(p.name).last,
        age: p.age ?? 0,
        gender: p.gender ?? 'm',        // 'm'/'f' – GenderIcon versteht beides
        lanes: p.lanes ?? 0,            // Anzahl Bahnen à 25 m
        averageTimeSeconds: p.averageTimeSeconds ?? null,
        lane: p.lane ?? null,
        isActive: p.isActive ?? false,
    }
}

/**
 * Konvertiert ein Backend-Result-Objekt (Ergebnisanzeige) ins Frontend-Format.
 *
 * @param {object} r  Backend result { name, lanes }
 * @returns {object}
 */
export function resultToFrontend(r) {
    return {
        id: r.id ?? 0,
        first_name: splitName(r.name).first,
        last_name: splitName(r.name).last,
        lanes: r.lanes ?? 0,               // bleibt als Bahnen (× 25 m = Distanz)
    }
}

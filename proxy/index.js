import 'dotenv/config'
import express from 'express'
import cors from 'cors'
import { createProxyMiddleware } from 'http-proxy-middleware'
import { createServer } from 'http'

const BACKEND_URL  = process.env.BACKEND_URL  || 'http://localhost:5000'
const FRONTEND_URL = process.env.FRONTEND_URL || 'http://localhost:5173'
const PORT         = process.env.PORT         || 3001

const app = express()

// CORS: Frontend-Origin erlauben (credentials für SignalR Cookies/Header)
app.use(cors({
    origin: FRONTEND_URL,
    credentials: true,
}))

// Alle Anfragen an das Backend weiterleiten
const proxy = createProxyMiddleware({
    target: BACKEND_URL,
    changeOrigin: true,
    ws: true,   // WebSocket für SignalR
    on: {
        error: (err, req, res) => {
            console.error(`Proxy-Fehler [${req.method} ${req.url}]:`, err.message)
            if (res.writeHead) res.writeHead(502).end('Backend nicht erreichbar')
        },
    },
})

app.use('/', proxy)

// HTTP-Server manuell erstellen damit WebSocket-Upgrades (SignalR) funktionieren
const server = createServer(app)
server.on('upgrade', proxy.upgrade)

server.listen(PORT, () => {
    console.log(`Proxy läuft auf http://localhost:${PORT}`)
    console.log(`  → Frontend erlaubt: ${FRONTEND_URL}`)
    console.log(`  → Backend:          ${BACKEND_URL}`)
})

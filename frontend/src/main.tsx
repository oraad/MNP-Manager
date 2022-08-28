import React from 'react'
import ReactDOM from 'react-dom/client'
import 'reflect-metadata'
import App from './App'
import './index.css'

if (import.meta.env.MODE == 'test') {
    const { worker } = await import('@/test/browser')
    worker.start({ onUnhandledRequest: 'bypass' })
}

// eslint-disable-next-line @typescript-eslint/no-non-null-assertion
ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
)

import { beforeAll, afterAll, afterEach } from 'vitest'
import { server } from './server'
import { fetch } from 'cross-fetch'

// Add `fetch` polyfill
global.fetch = fetch

beforeAll(() => server.listen({ onUnhandledRequest: 'error' }))
afterAll(() => server.close())
afterEach(() => server.resetHandlers())

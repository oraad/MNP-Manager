import { describe, expect, it } from 'vitest'
import { errorHandler } from '@/utils/fetchHandler'

describe('Test error handler', () => {

    // TODO: implement AbortError handler test
    it('should not throw error for DOMExceptions', () => {
        // DOMException 'The user aborted a request.', 'AbortError'

    })

    it('should handle Failed to Fetch as unknown error', () => {
        expect(() => errorHandler(new TypeError('Failed to fetch')))
            .toThrowError('Unknown request error')
    })

    it('should pass through other errors', () => {
        expect(() => errorHandler(new Error('other errors')))
            .toThrowError('other errors')
    })
})

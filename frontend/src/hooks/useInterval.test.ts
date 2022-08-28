import { renderHook } from '@testing-library/react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import { useInterval } from './useInterval'

describe('Test useInterval', () => {

    const mock = vi.fn(() => { return })

    beforeEach(() => {
        vi.useFakeTimers()
    })

    afterEach(() => {
        vi.restoreAllMocks()
    })

    it('should not work for unspecified delay', () => {

        renderHook(() => useInterval(mock, null))

        vi.advanceTimersByTime(60*1000)
        expect(mock).toHaveBeenCalledTimes(0)
    })

    it('should execute at specific intervals', () => {

        renderHook(() => useInterval(mock, 1000))

        vi.advanceTimersToNextTimer()
        expect(mock).toHaveBeenCalledTimes(1)
        vi.advanceTimersToNextTimer()
        expect(mock).toHaveBeenCalledTimes(2)
    })

    it('should execute immediately and at specific intervals', () => {

        renderHook(() => useInterval(mock, 1000, true))

        vi.advanceTimersToNextTimer()
        expect(mock).toHaveBeenCalledTimes(2)
        vi.advanceTimersToNextTimer()
        expect(mock).toHaveBeenCalledTimes(3)
    })

    it('should execute at specific intervals and not rerender', () => {


        const { rerender } = renderHook(
            ({ callback }) => useInterval(callback, 1000),
            {
                initialProps: {
                    callback: mock
                }
            }
        )

        vi.advanceTimersToNextTimer()
        expect(mock).toHaveBeenCalledTimes(1)

        const mock2 = vi.fn(() => { return })

        rerender({
            callback: mock2
        })

        vi.advanceTimersToNextTimer()
        expect(mock2).toHaveBeenCalledTimes(1)
    })
})

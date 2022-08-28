import { MobileOperator } from '@/models/MobileOperator.model'
import { act, renderHook } from '@testing-library/react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import { State, useStore } from '@/store/store'

describe('Auth Store', () => {



    let storeHook: {
        current: State
    }

    beforeEach(() => {
        vi.mock('zustand')
        storeHook = renderHook(() => useStore()).result
    })

    afterEach(() => {
        vi.restoreAllMocks()
    })

    const mobileOperator = new MobileOperator('Operator1', 'operator1')

    it('should have initial state', () => {

        const state = storeHook.current
        const { organizationHeader, isAuth } = state.auth
        expect(isAuth).toBe(false)
        expect(organizationHeader).toBe(null)
    })

    it('should login', () => {

        const state = storeHook.current

        act(() => {
            state.auth.login(mobileOperator)
        })

        const newState = storeHook.current
        const { organizationHeader, isAuth } = newState.auth
        expect(isAuth).toBe(true)
        expect(organizationHeader).toBe(mobileOperator.organizationHeader)
    })

    it('should logout', () => {

        const state = storeHook.current

        act(() => {
            state.auth.login(mobileOperator)
            state.auth.logout()
        })

        const newState = storeHook.current
        const { organizationHeader, isAuth } = newState.auth
        expect(isAuth).toBe(false)
        expect(organizationHeader).toBe(null)
    })
})

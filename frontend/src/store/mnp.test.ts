import { act, renderHook } from '@testing-library/react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import { State, useStore } from '@/store/store'

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

describe('MNP All Slice', () => {

    const mnpRequestType: keyof State['mnpRequests'] = 'all'

    it('should have initial state', () => {
        const state = storeHook.current
        const { pagination, sorting } = state.mnpRequests[mnpRequestType]
        expect(pagination).toStrictEqual({ pageIndex: 0, pageSize: 10 })
        expect(sorting).toStrictEqual([])
    })

    it('should set pagination', () => {
        const state = storeHook.current

        act(() => {
            state.mnpRequests[mnpRequestType]
                .setPagination({ pageIndex: 10, pageSize: 100 })
        })

        const newState = storeHook.current
        const { pagination, sorting } = newState.mnpRequests[mnpRequestType]
        expect(pagination).toStrictEqual({ pageIndex: 10, pageSize: 100 })
        expect(sorting).toStrictEqual([])
    })

    it('should  set sorting', () => {
        const state = storeHook.current

        const sortingOrder = [{ id: 'column1', desc: true }]

        act(() => {
            state.mnpRequests[mnpRequestType].setSorting(sortingOrder)
        })

        const newState = storeHook.current
        const { pagination, sorting } = newState.mnpRequests[mnpRequestType]
        expect(pagination).toStrictEqual({ pageIndex: 0, pageSize: 10 })
        expect(sorting).toStrictEqual(sortingOrder)
    })
})

describe('MNP Pending Slice', () => {

    const mnpRequestType: keyof State['mnpRequests'] = 'pending'

    it('should have initial state', () => {
        const state = storeHook.current
        const { pagination, sorting } = state.mnpRequests[mnpRequestType]
        expect(pagination).toStrictEqual({ pageIndex: 0, pageSize: 10 })
        expect(sorting).toStrictEqual([])
    })

    it('should set pagination', () => {
        const state = storeHook.current

        act(() => {
            state.mnpRequests[mnpRequestType]
                .setPagination({ pageIndex: 10, pageSize: 100 })
        })

        const newState = storeHook.current
        const { pagination, sorting } = newState.mnpRequests[mnpRequestType]
        expect(pagination).toStrictEqual({ pageIndex: 10, pageSize: 100 })
        expect(sorting).toStrictEqual([])
    })

    it('should  set sorting', () => {
        const state = storeHook.current

        const sortingOrder = [{ id: 'column1', desc: true }]

        act(() => {
            state.mnpRequests[mnpRequestType].setSorting(sortingOrder)
        })

        const newState = storeHook.current
        const { pagination, sorting } = newState.mnpRequests[mnpRequestType]
        expect(pagination).toStrictEqual({ pageIndex: 0, pageSize: 10 })
        expect(sorting).toStrictEqual(sortingOrder)
    })
})

describe('MNP Accepted Slice', () => {

    const mnpRequestType: keyof State['mnpRequests'] = 'accepted'

    it('should have initial state', () => {
        const state = storeHook.current
        const { pagination, sorting } = state.mnpRequests[mnpRequestType]
        expect(pagination).toStrictEqual({ pageIndex: 0, pageSize: 10 })
        expect(sorting).toStrictEqual([])
    })

    it('should set pagination', () => {
        const state = storeHook.current

        act(() => {
            state.mnpRequests[mnpRequestType]
                .setPagination({ pageIndex: 10, pageSize: 100 })
        })

        const newState = storeHook.current
        const { pagination, sorting } = newState.mnpRequests[mnpRequestType]
        expect(pagination).toStrictEqual({ pageIndex: 10, pageSize: 100 })
        expect(sorting).toStrictEqual([])
    })

    it('should  set sorting', () => {
        const state = storeHook.current

        const sortingOrder = [{ id: 'column1', desc: true }]

        act(() => {
            state.mnpRequests[mnpRequestType].setSorting(sortingOrder)
        })

        const newState = storeHook.current
        const { pagination, sorting } = newState.mnpRequests[mnpRequestType]
        expect(pagination).toStrictEqual({ pageIndex: 0, pageSize: 10 })
        expect(sorting).toStrictEqual(sortingOrder)
    })
})

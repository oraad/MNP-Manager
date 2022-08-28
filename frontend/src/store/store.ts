import create, { StateCreator } from 'zustand'
import { persist } from 'zustand/middleware'
import { AuthSlice, createAuthSlice } from './auth'
import { createMNPSlice, MNPSlice } from './mnp'

export type State =
    AuthSlice
    & MNPSlice

export type MutatorsInput = [
    ['zustand/persist', unknown]
]

export type MutatorsOutput = [
]

type StateMutatorsOutput = MutatorsInput | MutatorsOutput
interface PersistedState {
    organizationHeader: string
}

const deepMerge = (currentState: State, persistedState: PersistedState) => {
    currentState.auth.organizationHeader = persistedState.organizationHeader
    return currentState
}

const createRootSlice: StateCreator<State, MutatorsInput, MutatorsOutput> =
    (...accessors) => ({
        ...createAuthSlice(...accessors),
        ...createMNPSlice(...accessors)
    })

export const useStore = create<State, StateMutatorsOutput>(
    persist(
        createRootSlice, {
        name: 'mnp-portal',
        getStorage: () => sessionStorage,
        partialize: (state: State) => ({
            organizationHeader: state.auth.organizationHeader
        } as PersistedState),
        merge: (persistedState, currentState) =>
            deepMerge(currentState, persistedState as PersistedState),
    })
)




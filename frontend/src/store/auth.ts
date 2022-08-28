import { StateCreator } from 'zustand'
import { MobileOperator } from '@/models/MobileOperator.model'
import { MutatorsInput, MutatorsOutput, State } from './store'
import produce from 'immer'

export interface AuthSlice {
    auth: AuthProps
}

interface AuthProps {
    organizationHeader: string | null
    isAuth: boolean
    login(mobileOperator: MobileOperator): void
    logout(): void
}

export const createAuthSlice:
    StateCreator<State, MutatorsInput, MutatorsOutput, AuthSlice> =
    (set) => ({
        auth: {
            organizationHeader: null,
            isAuth: false,
            login: (mobileOperator: MobileOperator) =>
                set(produce((state: AuthSlice) => {
                    state.auth.organizationHeader = mobileOperator.organizationHeader
                    state.auth.isAuth = true
                })),
            logout: () => set(produce((state: AuthSlice) => {
                state.auth.organizationHeader = null
                state.auth.isAuth = false
            }))
        }
    })

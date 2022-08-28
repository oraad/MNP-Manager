import { useStore } from '@/store/store'
import { render, screen, } from '@testing-library/react'
import { MemoryRouter, Route, Routes } from 'react-router-dom'
import { describe, it, expect, vi, beforeAll, afterAll } from 'vitest'
import { RequireAuth } from './RequireAuth'

beforeAll(() => {
    vi.mock('zustand')
})

afterAll(() => {
    vi.restoreAllMocks()
})

describe('Test RequireAuth', () => {
    it('should navigate to /login for unauthenticated session', () => {

        const PrivateRoute = () => <div>private route</div>
        const LoginRoute = () => <div>login</div>

        render(
            <MemoryRouter initialEntries={['/']}>
                <Routes>
                    <Route
                        index
                        element={
                            <RequireAuth>
                                <PrivateRoute />
                            </RequireAuth>
                        }
                    />
                    <Route
                        path='/login'
                        element={<LoginRoute />}
                    />
                </Routes>
            </MemoryRouter>
        )

        expect(screen.getByText('login')).toBeDefined()
    })

    it('should allow private route for authenticated session', () => {

        const PrivateRoute = () => <div>private route</div>
        const LoginRoute = () => <div>login</div>

        useStore.setState(state => {
            state.auth.isAuth = true
            return state
        })

        render(
            <MemoryRouter initialEntries={['/']}>
                <Routes>
                    <Route
                        index
                        element={
                            <RequireAuth>
                                <PrivateRoute />
                            </RequireAuth>
                        }
                    />
                    <Route
                        path='/login'
                        element={<LoginRoute />}
                    />
                </Routes>
            </MemoryRouter>
        )

        expect(screen.getByText('private route')).toBeDefined()
    })
})

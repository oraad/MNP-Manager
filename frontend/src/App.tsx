import { FC, lazy, useMemo } from 'react'
import { Toaster } from 'react-hot-toast'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import { RequireAuth } from './routes/RequireAuth'
import { Layout } from './views/Layout'
import { Login } from './views/Login'
import { NoMatch } from './views/NoMatch'

const NewMNPRequest = lazy(() => import('./views/NewMNPRequest'))
const ViewMNPAcceptedRequests = lazy(() => import('./views/ViewMNPAcceptedRequests'))
const ViewMNPPendingRequests = lazy(() => import('./views/ViewMNPPendingRequests'))
const ViewMNPRequests = lazy(() => import('./views/ViewMNPRequests'))
const ViewMobileNumber = lazy(() => import('./views/ViewMobileNumber'))

const App: FC = () => {

    const navRoutes = useMemo(() => [
        {
            path: '/',
            name: 'All Requests'
        },
        {
            path: '/pending',
            name: 'Pending Requests'
        },
        {
            path: '/accepted',
            name: 'Accepted Requests'
        }
    ], [])

    const navAction = useMemo(() => ({
        path: '/create',
        name: 'Create Request'
    }), [])

    return (
        <div className='h-full w-full flex flex-col'>
            <Router>
                <Routes>

                    <Route path={'/login'}
                        element={<Login />}
                    />

                    <Route path="/" element={
                        <Layout
                            navRoutes={navRoutes}
                            navAction={navAction} />
                    }>

                        <Route index
                            element={
                                <RequireAuth>
                                    <ViewMNPRequests />
                                </RequireAuth>
                            } />

                        <Route path={'/create'}
                            element={
                                <RequireAuth>
                                    <NewMNPRequest />
                                </RequireAuth>
                            } />

                        <Route path={'/view/:mobileNumber'}
                            element={
                                <RequireAuth>
                                    <ViewMobileNumber />
                                </RequireAuth>
                            } />

                        <Route
                            path={'/pending'}
                            element={
                                <RequireAuth>
                                    <ViewMNPPendingRequests />
                                </RequireAuth>
                            } />

                        <Route
                            path={'/accepted'}
                            element={
                                <RequireAuth>
                                    <ViewMNPAcceptedRequests />
                                </RequireAuth>
                            } />

                        <Route path="*" element={<NoMatch />} />
                    </Route>
                </Routes>
            </Router>
            <Toaster
                toastOptions={{
                    success: {
                        className: 'bg-green'
                    },
                    error: {
                        className: 'bg-red'
                    },
                    position: 'bottom-right'
                }}
            />
        </div>
    )
}

export default App

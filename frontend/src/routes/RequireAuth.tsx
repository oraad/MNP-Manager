import { FC, ReactNode } from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import shallow from 'zustand/shallow'
import { useStore } from '@/store/store'

interface RequireAuthProps {
    children?: ReactNode
}

export const RequireAuth: FC<RequireAuthProps> = ({ children }) => {

    const isAuth = useStore(state => state.auth.isAuth, shallow)
    const location = useLocation()

    if (!isAuth)
        return <Navigate to="/login" state={{ from: location }} replace />

    return <>{children}</>
}

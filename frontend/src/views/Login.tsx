import { FC, useEffect, useRef } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import shallow from 'zustand/shallow'
import { loginAgentByOperatorName, loginAgentByOrganizationHeader }
    from '@/services/mobileNumberPortingService'
import { useStore } from '@/store/store'
import { LocationState } from '@/types/location'
import { toastError } from '@/utils/toastHandler'
import { LoginForm } from '@/components/LoginForm'

interface LoginProps {
}

export const Login: FC<LoginProps> = () => {

    const { isAuth, organizationHeader, login } =
        useStore(state => state.auth, shallow)

    const navigate = useNavigate()
    const location = useLocation()

    const abortLogin = useRef<AbortController>()

    const from = (location.state as LocationState)?.from?.pathname ?? '/'

    useEffect(() => {
        if (isAuth)
            navigate(from, { replace: true })
    }, [isAuth])

    useEffect(() => {
        if (organizationHeader)
            loginAgentByOrganizationHeader(organizationHeader)
                .then(login)
                .catch(toastError)
    }, [organizationHeader])

    const handleSubmit = (operatorName: string) => {

        abortLogin.current?.abort()
        abortLogin.current = new AbortController()

        loginAgentByOperatorName(operatorName, abortLogin.current.signal)
            .then(login)
            .catch(toastError)
    }

    if (organizationHeader)
        return <div>Signing in...</div>

    return (
        <LoginForm onSubmit={handleSubmit} />
    )
}

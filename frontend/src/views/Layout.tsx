import { FC, Suspense, useTransition } from 'react'
import { Outlet } from 'react-router-dom'
import { useStore } from '@/store/store'
import { Footer } from '@/components/Footer'
import { NavBar, NavItem } from '@/components/NavBar'
import { ProfileActions } from '@/components/ProfileMenu'
import shallow from 'zustand/shallow'
import { LoadingBar } from '@/components/LoadingBar'

interface LayoutProps {
    navRoutes: NavItem[]
    navAction?: NavItem
}

export const Layout: FC<LayoutProps> = ({ navRoutes, navAction }) => {

    const [isPending, startTransition] = useTransition()

    const { logout } = useStore(state => ({ logout: state.auth.logout }), shallow)

    const profileActions: ProfileActions = {
        logout
    }

    return (
        <div className='h-full w-full flex flex-col'>
            <NavBar
                navList={navRoutes}
                navAction={navAction}
                startTransition={startTransition}
                profileActions={profileActions} />

            <div className='flex-1'>
                {/* <div className='h-1'>
                    {isPending && <LoadingBar />}
                </div> */}
                <Suspense fallback={<LoadingBar />}>
                    <Outlet />
                </Suspense>
            </div>
            <Footer />
        </div>
    )
}

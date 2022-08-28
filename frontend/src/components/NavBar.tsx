import { Disclosure } from '@headlessui/react'
import { XMarkIcon, Bars3Icon as MenuIcon } from '@heroicons/react/24/outline'
import classNames from 'classnames'
import { FC, TransitionStartFunction } from 'react'
import { Link, NavLink } from 'react-router-dom'
import { NavLinkWithTransition } from './NavLinkWithTransition'
import { ProfileActions, ProfileMenu } from './ProfileMenu'

interface NavBarProps {
    navList: NavItem[]
    navAction?: NavItem
    profileActions: ProfileActions
    startTransition: TransitionStartFunction
}

export interface NavItem {
    path: string
    name: string
}



export const NavBar: FC<NavBarProps> = ({
    navList, navAction, profileActions,
    startTransition
}) => {

    return (

        <Disclosure as="nav" className="bg-gray-800">
            {({ open }) => (
                <>
                    <div className="max-w-7xl mx-auto px-2 sm:px-6 lg:px-8">
                        <div className="relative flex items-center justify-between h-16">
                            <div className="absolute inset-y-0 left-0 flex items-center sm:hidden">
                                {/* Mobile menu button*/}
                                <Disclosure.Button
                                    className="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-white hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white">
                                    <span className="sr-only">Open main menu</span>
                                    {open ? (
                                        <XMarkIcon className="block h-6 w-6" aria-hidden="true" />
                                    ) : (
                                        <MenuIcon className="block h-6 w-6" aria-hidden="true" />
                                    )}
                                </Disclosure.Button>
                            </div>
                            <div className="flex-1 flex items-center justify-center sm:items-stretch sm:justify-start">
                                <div className="flex-shrink-0 flex items-center">
                                    <div className='font-bold text-xl text-white'>MNP Portal</div>
                                </div>
                                <div className="hidden sm:block sm:ml-6">
                                    <div className="flex space-x-4">
                                        {navList.map((item) => (
                                            <NavLinkWithTransition
                                                key={item.name}
                                                to={item.path}
                                                className={({ isActive }) => classNames(
                                                    isActive ? 'bg-gray-900 text-white'
                                                        : 'text-gray-300 hover:bg-gray-700 hover:text-white',
                                                    'px-3 py-2 rounded-md text-sm font-medium'
                                                )}
                                                startTransition={startTransition}
                                            >
                                                {item.name}
                                            </NavLinkWithTransition>
                                        ))}
                                    </div>
                                </div>
                            </div>
                            <div className="absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0">
                                {
                                    navAction == undefined ? null
                                        : <Link
                                            to={navAction.path}
                                            className={classNames('text-white bg-blue-700 hover:bg-blue-800',
                                                'focus:outline-none  font-medium rounded-lg',
                                                'text-sm px-5 py-2.5 text-center mr-3 md:mr-0 dark:bg-blue-600',
                                                'dark:hover:bg-blue-700 dark:focus:ring-blue-800')}
                                        >{navAction.name}</Link>
                                }
                                <ProfileMenu actions={profileActions} />
                            </div>
                        </div>
                    </div>

                    <Disclosure.Panel className="sm:hidden">
                        <div className="px-2 pt-2 pb-3 space-y-1">
                            {navList.map((item) => (
                                <NavLink
                                    key={item.name}
                                    to={item.path}
                                    className={({ isActive }) => classNames(
                                        isActive ? 'bg-gray-900 text-white'
                                            : 'text-gray-300 hover:bg-gray-700 hover:text-white',
                                        'block px-3 py-2 rounded-md text-base font-medium'
                                    )}
                                >
                                    {item.name}
                                </NavLink>
                            ))}
                        </div>
                    </Disclosure.Panel>
                </>
            )}
        </Disclosure>
    )
}


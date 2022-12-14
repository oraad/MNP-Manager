import { Menu, Transition } from '@headlessui/react'
import { UserCircleIcon } from '@heroicons/react/24/outline'
import classNames from 'classnames'
import { FC, Fragment } from 'react'

interface ProfileMenuProps {
    actions: ProfileActions
}

export interface ProfileActions {
    logout(): void
}

export const ProfileMenu: FC<ProfileMenuProps> = ({ actions }) => {

    const { logout } = actions

    return (
        <Menu as="div" className="ml-3 relative">
            <div>
                <Menu.Button className="bg-gray-800 flex text-sm rounded-full focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-800 focus:ring-white">
                    <span className="sr-only">Open user menu</span>
                    <UserCircleIcon className='w-8 h-8 text-slate-300' />
                </Menu.Button>
            </div>
            <Transition
                as={Fragment}
                enter="transition ease-out duration-100"
                enterFrom="transform opacity-0 scale-95"
                enterTo="transform opacity-100 scale-100"
                leave="transition ease-in duration-75"
                leaveFrom="transform opacity-100 scale-100"
                leaveTo="transform opacity-0 scale-95"
            >
                <Menu.Items
                    className="origin-top-right absolute right-0 mt-2 w-48 rounded-md shadow-lg py-1 bg-white ring-1 ring-black ring-opacity-5 focus:outline-none">
                    <Menu.Item>
                        {({ active }) => (
                            <div
                                onClick={() => logout()}
                                className={
                                    classNames(active ? 'bg-gray-100'
                                        : '',
                                        'block px-4 py-2 text-sm text-gray-700 cursor-pointer')
                                }
                            >
                                Sign out
                            </div>
                        )}
                    </Menu.Item>
                </Menu.Items>
            </Transition>
        </Menu>
    )
}

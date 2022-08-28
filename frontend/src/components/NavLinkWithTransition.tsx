import { forwardRef, TransitionStartFunction } from 'react'
import { useLocation, useResolvedPath } from 'react-router-dom'
import { LinkWithTransition, LinkWithTransitionProps } from './LinkWithTransition'

export interface NavLinkWithTransitionProps
    extends Omit<LinkWithTransitionProps, 'className' | 'style' | 'children'> {
    children?:
    | React.ReactNode
    | ((props: { isActive: boolean }) => React.ReactNode)
    caseSensitive?: boolean
    className?: string | ((props: { isActive: boolean }) => string | undefined)
    end?: boolean
    style?:
    | React.CSSProperties
    | ((props: { isActive: boolean }) => React.CSSProperties)
    startTransition?: TransitionStartFunction
}

/**
 * A <Link> wrapper that knows if it's 'active' or not.
 *
 * @see https://reactrouter.com/docs/en/v6/components/nav-link
 */
export const NavLinkWithTransition = forwardRef<HTMLAnchorElement, NavLinkWithTransitionProps>(
    function NavLinkWithRef(
        {
            'aria-current': ariaCurrentProp = 'page',
            caseSensitive = false,
            className: classNameProp = '',
            end = false,
            style: styleProp,
            to,
            children,
            startTransition,
            ...rest
        },
        ref
    ) {
        const location = useLocation()
        const path = useResolvedPath(to)

        let locationPathname = location.pathname
        let toPathname = path.pathname
        if (!caseSensitive) {
            locationPathname = locationPathname.toLowerCase()
            toPathname = toPathname.toLowerCase()
        }

        const isActive =
            locationPathname === toPathname ||
            (!end &&
                locationPathname.startsWith(toPathname) &&
                locationPathname.charAt(toPathname.length) === '/')

        const ariaCurrent = isActive ? ariaCurrentProp : undefined

        let className: string | undefined
        if (typeof classNameProp === 'function') {
            className = classNameProp({ isActive })
        } else {
            // If the className prop is not a function, we use a default `active`
            // class for <NavLink />s that are active. In v5 `active` was the default
            // value for `activeClassName`, but we are removing that API and can still
            // use the old default behavior for a cleaner upgrade path and keep the
            // simple styling rules working as they currently do.
            className = [classNameProp, isActive ? 'active' : null]
                .filter(Boolean)
                .join(' ')
        }

        const style =
            typeof styleProp === 'function' ? styleProp({ isActive }) : styleProp

        return (
            <LinkWithTransition
                {...rest}
                aria-current={ariaCurrent}
                className={className}
                ref={ref}
                style={style}
                to={to}
                startTransition={startTransition}
            >
                {typeof children === 'function' ? children({ isActive }) : children}
            </LinkWithTransition>
        )
    }
)

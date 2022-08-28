import { forwardRef, TransitionStartFunction } from 'react'
import { To, useHref, useLinkClickHandler } from 'react-router-dom'

export interface LinkWithTransitionProps
    extends Omit<React.AnchorHTMLAttributes<HTMLAnchorElement>, 'href'> {
    reloadDocument?: boolean
    replace?: boolean
    state?: any
    to: To
    startTransition?: TransitionStartFunction
}

/**
 * The public API for rendering a history-aware <a>.
 *
 * @see https://reactrouter.com/docs/en/v6/components/link
 */
export const LinkWithTransition = forwardRef<HTMLAnchorElement, LinkWithTransitionProps>(
    function LinkWithRef(
        { onClick, reloadDocument, replace = false, state, target, to, startTransition, ...rest },
        ref
    ) {
        let href = useHref(to)
        let internalOnClick = useLinkClickHandler(to, { replace, state, target })

        function handleClick(
            event: React.MouseEvent<HTMLAnchorElement, MouseEvent>
        ) {
            if (onClick) onClick(event)
            if (!event.defaultPrevented && !reloadDocument) {
                startTransition?.(() =>
                    internalOnClick(event)
                ) ?? internalOnClick(event)
            }
        }

        return (
            // eslint-disable-next-line jsx-a11y/anchor-has-content
            <a
                {...rest}
                href={href}
                onClick={handleClick}
                ref={ref}
                target={target}
            />
        )
    }
)

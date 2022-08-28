import { useEffect, useRef } from 'react'

export const useInterval = (
    callback: () => void,
    delay: number | null,
    immediate?: boolean,
    deps: React.DependencyList | undefined = []
) => {

    const savedCallback = useRef(callback)

    deps = deps.concat([delay, immediate])

    // Remember the latest callback if it changes.
    useEffect(() => {
        savedCallback.current = callback
    }, [callback])

    // Set up the interval.
    useEffect(() => {

        if (delay == undefined)
            return

        const id = setInterval(() => savedCallback.current(), delay)

        if (immediate)
            savedCallback.current()

        return () => clearInterval(id)
    }, deps)
}

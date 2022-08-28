import { BrowserContext } from '@playwright/test'

export const authorizeSession = (context: BrowserContext, organizationHeader: string) => {

    const storageState = {
        state: {
            organizationHeader: organizationHeader
        },
        version: 0
    }

    context.addInitScript((storageState: unknown) => {
        window.sessionStorage.setItem(
            'mnp-portal',
            JSON.stringify(storageState)
        )
    }, storageState)

    return context.newPage()
}

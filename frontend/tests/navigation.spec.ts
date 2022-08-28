import { expect, test } from '@playwright/test'
import { authorizeSession } from './utils'

test.describe('Test navigation', () => {

    test('Main navigation', async ({context}) => {

        const page = await authorizeSession(context, 'operatorA')

        await page.goto('/')

        await expect(page).toHaveTitle('Mobile Number Portability Portal')

    })

    test('No Match navigation', async ({context}) => {

        const page = await authorizeSession(context, 'operatorA')

        await page.goto('/no-match')

        expect(page.locator('[text="No Match"]')).toBeDefined()

    })
})

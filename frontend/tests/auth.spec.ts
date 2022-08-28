import { expect, test } from '@playwright/test'
import { authorizeSession } from './utils'

test.describe('Test session authenticate', () => {

    test('authenticate new session', async ({ page }) => {

        await page.goto('/')

        // Wait for page to be redirect to login
        await page.waitForURL('/login', { timeout: 3000 })

        // Check if Login form visible
        expect(page.locator('text=Enter mobile operator name:')).toBeVisible()

        // Fill Operator Name input
        await page.locator('input[name="operator-name"]').fill('OperatorA')
        await page.locator('button[name="signin"]').click()

        // Wait for page to be redirect to login
        await page.waitForURL('/', { timeout: 3000 })
    })

    test('authenticate previous session', async ({ context }) => {

        const page = await authorizeSession(context, 'operatorA')

        await page.goto('/')

        // Wait for page to be redirect to login
        await page.waitForURL('/login')

        // Wait for page to be redirect to login
        await page.waitForURL('/')
    })
})





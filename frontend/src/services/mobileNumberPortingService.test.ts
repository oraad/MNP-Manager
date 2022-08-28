import 'reflect-metadata'
import { afterAll, beforeAll, describe, expect, it, vi } from 'vitest'
import {
    getAllMobileNumberPorting,
    loginAgentByOperatorName,
    loginAgentByOrganizationHeader,
    PageableAndSortingOptions
} from '@/services/mobileNumberPortingService'
import { mobileOperatorDtos } from '@/mocks/data'
import { mapper } from '@/mappings/mapper'
import { MobileOperatorDto } from '@/dtos/MobileOperatorDto.dto'
import { MobileOperator } from '@/models/MobileOperator.model'
import { useStore } from '@/store/store'
import { Pageable } from '@/models/Pageable.model'
import { PortingStatus } from '@/models/PortingStatus.model'



describe('Authenticate operator agent by credentials', () => {
    it('should authenticate operator agent successfully', async () => {

        const mobileOperatorDto = mobileOperatorDtos[0]
        const mobileOperator =
            mapper.map(mobileOperatorDto, MobileOperatorDto, MobileOperator)

        const mobileOperatorName = mobileOperator.operatorName
        const mobileOperatorResult = await loginAgentByOperatorName(mobileOperatorName)

        expect(mobileOperatorResult).toStrictEqual(mobileOperator)
    })

    it('should not authenticate operator agent', async () => {

        let error: Error

        try {
            await loginAgentByOperatorName('')
        } catch (err) {
            error = err as Error
        }

        expect(() => { throw error }).toThrowError('Operator not found.')
    })
})

describe('Authenticate operator agent by organization header', () => {
    it('should authenticate operator agent successfully', async () => {

        const mobileOperatorDto = mobileOperatorDtos[0]
        const mobileOperator =
            mapper.map(mobileOperatorDto, MobileOperatorDto, MobileOperator)

        const mobileOperatorOrganizationHeader = mobileOperator.organizationHeader
        const mobileOperatorResult =
            await loginAgentByOrganizationHeader(mobileOperatorOrganizationHeader)

        expect(mobileOperatorResult).toStrictEqual(mobileOperator)
    })

    it('should not authenticate operator agent', async () => {

        let error: Error

        try {
            await loginAgentByOrganizationHeader('')
        } catch (err) {
            error = err as Error
        }

        expect(() => { throw error })
            .toThrowError('Required request header `organization` is not present')
    })
})

describe('Getting All MobileNumberPorting', () => {

    beforeAll(() => {
        vi.mock('zustand')
    })

    afterAll(()=> {
        vi.restoreAllMocks()
    })

    it('should get all mobile number porting', async () => {

        const mobileOperator: MobileOperator = mobileOperatorDtos[0]

        useStore.getState().auth.login(mobileOperator)

        const pageableResult = await getAllMobileNumberPorting()

            expect(pageableResult).toBeInstanceOf(Pageable)
            expect(pageableResult.first).toBeTruthy()
            expect(pageableResult.page).toBe(1)
            expect(pageableResult.empty).toBeFalsy()
            expect(pageableResult.totalElements).toBeGreaterThan(0)
        const mobileNumberPortings = pageableResult.content

        const filterCondition = mobileNumberPortings.every(mobileNumberPorting =>
            mobileNumberPorting.status == PortingStatus.ACCEPTED
            || mobileNumberPorting.donorOperator == mobileOperator.operatorName
            || mobileNumberPorting.recipientOperator == mobileOperator.operatorName
        )

        expect(filterCondition).toBeTruthy()
    })

    it('should get all mobile number porting paged', async () => {

        const mobileOperator: MobileOperator = mobileOperatorDtos[0]

        useStore.getState().auth.login(mobileOperator)

        const opts: PageableAndSortingOptions = {
            size: 1,
            page: 2
        }

        const pageableResult = await getAllMobileNumberPorting(opts)

        expect(pageableResult).toBeInstanceOf(Pageable)
        expect(pageableResult.first).toBeFalsy()
        expect(pageableResult.page).toBe(2)
        expect(pageableResult.empty).toBeFalsy()
        expect(pageableResult.totalElements).toBeGreaterThan(0)
        // const mobileNumberPortings = pageableResult.content
    })

    it('should get all mobile number porting sorted', async () => {

        const mobileOperator: MobileOperator = mobileOperatorDtos[0]

        useStore.getState().auth.login(mobileOperator)

        const opts: PageableAndSortingOptions = {
            sorting: [
                {
                    id: 'id',
                    desc: true
                }
            ]
        }

        const pageableResult = await getAllMobileNumberPorting(opts)

        expect(pageableResult).toBeInstanceOf(Pageable)
        expect(pageableResult.first).toBeTruthy()
        expect(pageableResult.page).toBe(1)
        expect(pageableResult.empty).toBeFalsy()
        expect(pageableResult.totalElements).toBeGreaterThan(0)
        const mobileNumberPortings = pageableResult.content

        for (let i = 0; i < mobileNumberPortings.length - 1; i++) {

            const currentItem = mobileNumberPortings[i].id
            const nextItem = mobileNumberPortings[i + 1].id

            expect(currentItem).toBeGreaterThan(nextItem)
        }

    })
})

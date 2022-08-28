import 'reflect-metadata'
import { MobileNumberPortingActionsDto } from '@/dtos/MobileNumberPortingActionsDto.dto'
import { MobileNumberPortingDto } from '@/dtos/MobileNumberPortingDto.dto'
import { PortingStatusDto } from '@/dtos/PortingStatusDto.dto'
import { mapper } from '@/mappings/mapper'
import { MobileNumberPorting } from '@/models/MobileNumberPorting.model'
import { MobileNumberPortingActions } from '@/models/MobileNumberPortingActions.model'
import { expect, it } from 'vitest'
import { MobileNumberPortingWithActionsDto } from '@/dtos/MobileNumberPortingWithActionsDto.dto'
import { MobileNumberPortingWithActions } from '@/models/MobileNumberPortingWithActions.model'

it('should map MobileNumberPortingDto to MobileNumberPorting', () => {

    const mobileNumberPortingDto: MobileNumberPortingDto = {
        id: 123,
        mobileNumber: '011101',
        donorOperator: 'Operator1',
        recipientOperator: 'Operator2',
        status: PortingStatusDto.ACCEPTED,
        createdOn: new Date(),
        updatedOn: new Date()
    }

    const mobileNumberPorting =
        mapper.map(mobileNumberPortingDto,
            MobileNumberPortingDto,
            MobileNumberPorting)

    expect(mobileNumberPorting.id).toBe(mobileNumberPortingDto.id)
    expect(mobileNumberPorting.mobileNumber)
        .toBe(mobileNumberPortingDto.mobileNumber)
    expect(mobileNumberPorting.donorOperator)
        .toBe(mobileNumberPortingDto.donorOperator)
    expect(mobileNumberPorting.recipientOperator)
        .toBe(mobileNumberPortingDto.recipientOperator)
    expect(mobileNumberPorting.status)
        .toBe(mobileNumberPortingDto.status)
    expect(mobileNumberPorting.createdOn)
        .toBe(mobileNumberPortingDto.createdOn)
    expect(mobileNumberPorting.updatedOn)
        .toBe(mobileNumberPortingDto.updatedOn)

})

it('should map MobileNumberPortingActionsDto to MobileNumberPortingActions', () => {

    const mobileNumberPortingActionsDto: MobileNumberPortingActionsDto = {
        accept: 'api/0111/accept',
        reject: 'api/0111/reject'
    }

    const mobileNumberPortingActions =
        mapper.map(mobileNumberPortingActionsDto,
            MobileNumberPortingActionsDto,
            MobileNumberPortingActions)

    expect(mobileNumberPortingActions.accept)
        .toBe(mobileNumberPortingActionsDto.accept)
    expect(mobileNumberPortingActions.reject)
        .toBe(mobileNumberPortingActionsDto.reject)

})

it('should map MobileNumberPortingWithActionsDto to MobileNumberPortingWithActions', () => {

    const mobileNumberPortingWithActionsDto: MobileNumberPortingWithActionsDto = {
        id: 123,
        mobileNumber: '011101',
        donorOperator: 'Operator1',
        recipientOperator: 'Operator2',
        status: PortingStatusDto.ACCEPTED,
        createdOn: new Date(),
        updatedOn: new Date(),
        actions: {
            accept: 'api/0111/accept',
            reject: 'api/0111/reject'
        }
    }

    const mobileNumberPortingWithActions =
        mapper.map(mobileNumberPortingWithActionsDto,
            MobileNumberPortingWithActionsDto,
            MobileNumberPortingWithActions)

    expect(mobileNumberPortingWithActions.id)
        .toBe(mobileNumberPortingWithActionsDto.id)
    expect(mobileNumberPortingWithActions.mobileNumber)
        .toBe(mobileNumberPortingWithActionsDto.mobileNumber)
    expect(mobileNumberPortingWithActions.donorOperator)
        .toBe(mobileNumberPortingWithActionsDto.donorOperator)
    expect(mobileNumberPortingWithActions.recipientOperator)
        .toBe(mobileNumberPortingWithActionsDto.recipientOperator)
    expect(mobileNumberPortingWithActions.status)
        .toBe(mobileNumberPortingWithActionsDto.status)
    expect(mobileNumberPortingWithActions.createdOn)
        .toBe(mobileNumberPortingWithActionsDto.createdOn)
    expect(mobileNumberPortingWithActions.updatedOn)
        .toBe(mobileNumberPortingWithActionsDto.updatedOn)
    expect(mobileNumberPortingWithActions.actions)
        .toEqual(mobileNumberPortingWithActionsDto.actions)

})

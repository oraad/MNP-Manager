import { MobileNumberPortingDto } from '@/dtos/MobileNumberPortingDto.dto'
import { MobileOperatorDto } from '@/dtos/MobileOperatorDto.dto'
import { PortingStatusDto } from '@/dtos/PortingStatusDto.dto'
import dayjs from 'dayjs'

// Mock Data

export const mobileOperatorDtos: MobileOperatorDto[] = [
    {
        operatorName: 'OperatorA',
        organizationHeader: 'operatorA'
    },
    {
        operatorName: 'OperatorB',
        organizationHeader: 'operatorB'
    }
]

export const mobileNumberPortingDtos: MobileNumberPortingDto[] = [
    {
        id: 1,
        mobileNumber: '1001',
        donorOperator: 'OperatorA',
        recipientOperator: 'OperatorB',
        createdOn: new Date(),
        status: PortingStatusDto.PENDING,
        updatedOn: undefined
    },
    {
        id: 2,
        mobileNumber: '1002',
        donorOperator: 'OperatorA',
        recipientOperator: 'OperatorB',
        createdOn: new Date(),
        status: PortingStatusDto.ACCEPTED,
        updatedOn: dayjs(new Date()).add(1, 'minute').toDate()
    },
    {
        id: 3,
        mobileNumber: '1003',
        donorOperator: 'OperatorA',
        recipientOperator: 'OperatorC',
        createdOn: new Date(),
        status: PortingStatusDto.ACCEPTED,
        updatedOn: dayjs(new Date()).add(1, 'minute').toDate()
    },
    {
        id: 4,
        mobileNumber: '1004',
        donorOperator: 'OperatorA',
        recipientOperator: 'OperatorC',
        createdOn: new Date(),
        status: PortingStatusDto.PENDING,
        updatedOn: dayjs(new Date()).add(1, 'minute').toDate()
    },
    {
        id: 5,
        mobileNumber: '1005',
        donorOperator: 'OperatorA',
        recipientOperator: 'OperatorC',
        createdOn: new Date(),
        status: PortingStatusDto.CANCELED,
        updatedOn: dayjs(new Date()).add(1, 'minute').toDate()
    }
]

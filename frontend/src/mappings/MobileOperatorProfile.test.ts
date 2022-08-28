import 'reflect-metadata'
import { MobileOperatorDto } from '@/dtos/MobileOperatorDto.dto'
import { mapper } from '@/mappings/mapper'
import { MobileOperator } from '@/models/MobileOperator.model'
import { expect, it } from 'vitest'

it('should map MobileOperatorDto to MobileOperator', () => {

    const mobileOperatorDto: MobileOperatorDto = {
        operatorName: 'Operator1',
        organizationHeader: 'operator1'
    }

    const mobileOperator =
        mapper.map(mobileOperatorDto, MobileOperatorDto, MobileOperator)

    expect(mobileOperator.operatorName).toBe(mobileOperatorDto.operatorName)
    expect(mobileOperator.organizationHeader).toBe(mobileOperatorDto.organizationHeader)

})

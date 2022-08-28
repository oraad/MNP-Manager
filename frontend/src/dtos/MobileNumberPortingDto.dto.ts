import { Type } from 'class-transformer'
import { PortingStatusDto } from './PortingStatusDto.dto'

export class MobileNumberPortingDto {
    id!: number
    mobileNumber!: string
    donorOperator!: string
    recipientOperator!: string
    status!: PortingStatusDto

    @Type(() => Date)
    createdOn!: Date

    @Type(() => Date)
    updatedOn?: Date
}

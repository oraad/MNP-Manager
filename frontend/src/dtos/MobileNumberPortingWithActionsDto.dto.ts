import { Type } from 'class-transformer'
import { MobileNumberPortingActionsDto } from './MobileNumberPortingActionsDto.dto'
import { MobileNumberPortingDto } from './MobileNumberPortingDto.dto'

export class MobileNumberPortingWithActionsDto extends MobileNumberPortingDto {
    @Type(() => MobileNumberPortingActionsDto)
    actions!: MobileNumberPortingActionsDto
}


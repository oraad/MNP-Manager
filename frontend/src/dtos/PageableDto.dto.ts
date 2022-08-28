import type { ClassConstructor } from '@/types/class'
import { Type, Exclude } from 'class-transformer'
import { SortableDto } from './SortableDto.dto'

export class PageableDto<T> {

    @Exclude()
    private genericType?: ClassConstructor<T>

    @Type(options => (options?.newObject as PageableDto<T>)
        .genericType as ClassConstructor<T>)
    content?: T[]

    // pageable: INSTANCE,
    first?: boolean
    last?: boolean
    empty?: boolean
    size?: number
    number?: number
    numberOfElements?: number
    totalPages?: number
    totalElements?: number

    @Type(() => SortableDto)
    sort?: SortableDto

    constructor(genericType: ClassConstructor<T>) {
        this.genericType = genericType
    }
}

import {
    addProfile, createMap, createMapper,
    extend, forMember, mapDefer, mapFrom, MappingProfile, mapWith
} from '@automapper/core'
import { classes } from '@automapper/classes'
import { MobileOperator } from '@/models/MobileOperator.model'
import { MobileOperatorDto } from '@/dtos/MobileOperatorDto.dto'
import { Sortable } from '@/models/Sortable.model'
import { SortableDto } from '@/dtos/SortableDto.dto'
import { MobileNumberPortingActions } from '@/models/MobileNumberPortingActions.model'
import { MobileNumberPortingActionsDto } from '@/dtos/MobileNumberPortingActionsDto.dto'
import { MobileNumberPortingDto } from '@/dtos/MobileNumberPortingDto.dto'
import { MobileNumberPorting } from '@/models/MobileNumberPorting.model'
import { MobileNumberPortingWithActions } from '@/models/MobileNumberPortingWithActions.model'
import { MobileNumberPortingWithActionsDto } from '@/dtos/MobileNumberPortingWithActionsDto.dto'
import { Pageable } from '@/models/Pageable.model'
import { PageableDto } from '@/dtos/PageableDto.dto'

// Create and export the mapper
export const mapper = createMapper({
    strategyInitializer: classes(),
})

const mobileOperatorProfile: MappingProfile = (mapper) => {
    createMap(mapper, MobileOperatorDto, MobileOperator)
}

const mobileNumberPortingProfile: MappingProfile = (mapper) => {
    const mobileNumberPortingMapping =
        createMap(mapper, MobileNumberPortingDto, MobileNumberPorting)
    createMap(mapper,
        MobileNumberPortingActionsDto, MobileNumberPortingActions)
    createMap(mapper,
        MobileNumberPortingWithActionsDto,
        MobileNumberPortingWithActions,
        extend(mobileNumberPortingMapping)
    )
}

const pageableProfile: MappingProfile = (mapper) => {
    createMap(mapper, SortableDto, Sortable)
    createMap(mapper, PageableDto, Pageable,
        forMember(dest => dest.page, mapFrom(src => src.number)),
        forMember(dest => dest.content,
            mapDefer(src => {
                if (src.content?.[0] instanceof MobileNumberPortingWithActionsDto)
                    return mapWith(
                        MobileNumberPortingActions,
                        MobileNumberPortingActionsDto,
                        src => src.content
                    )

                if (src.content?.[0] instanceof MobileNumberPortingDto)
                    return mapWith(
                        MobileNumberPorting,
                        MobileNumberPortingDto,
                        src => src.content
                    )


                return mapFrom(src => src.content)
            })
        )
    )
}

addProfile(mapper, mobileOperatorProfile)
addProfile(mapper, mobileNumberPortingProfile)
addProfile(mapper, pageableProfile)

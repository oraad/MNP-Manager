import 'reflect-metadata'
import { MobileOperatorDto } from '@/dtos/MobileOperatorDto.dto'
import { mapper } from '@/mappings/mapper'
import { expect, it } from 'vitest'
import { Sortable } from '@/models/Sortable.model'
import { SortableDto } from '@/dtos/SortableDto.dto'
import { PageableDto } from '@/dtos/PageableDto.dto'
import { Pageable } from '@/models/Pageable.model'
import { MobileNumberPortingDto } from '@/dtos/MobileNumberPortingDto.dto'
import { MobileNumberPorting } from '@/models/MobileNumberPorting.model'

it('should map SortableDto to Sortable', () => {

    const sortableDto: SortableDto = {
        empty: true,
        sorted: false,
        unsorted: false
    }

    const sortable =
        mapper.map(sortableDto, SortableDto, Sortable)

    expect(sortable).toEqual(sortableDto)

})

it('should map PageableDto to Pageable', () => {

    const pageableDto: PageableDto<never> = {
        first: true,
        last: true,
        content: [],
        empty: true,
        number: 0,
        numberOfElements: 0,
        size: 0,
        totalElements: 0,
        totalPages: 0,
        sort: {
            empty: true,
            sorted: false,
            unsorted: false
        }
    }

    const pageable = mapper.map(pageableDto, PageableDto, Pageable)

    expect(pageable.first).toEqual(pageableDto.first)
    expect(pageable.last).toEqual(pageableDto.last)
    expect(pageable.empty).toEqual(pageableDto.empty)
    expect(pageable.page).toEqual(pageableDto.number)
    expect(pageable.numberOfElements)
        .toEqual(pageableDto.numberOfElements)
    expect(pageable.size).toEqual(pageableDto.size)
    expect(pageable.totalElements)
        .toEqual(pageableDto.totalElements)
    expect(pageable.totalPages).toEqual(pageableDto.totalPages)
    expect(pageable.sort).toEqual(pageableDto.sort)
    expect(pageable.content).toEqual(pageableDto.content)
})

it('should map PageableDto<MobileOperatorDto> to Pageable<MobileOperator>', () => {

    const mobileOperatorsDto: MobileOperatorDto[] = [new MobileOperatorDto(), new MobileOperatorDto()]

    const pageableDto: PageableDto<MobileOperatorDto> = {
        first: true,
        last: true,
        content: mobileOperatorsDto,
        empty: true,
        number: 0,
        numberOfElements: 0,
        size: 0,
        totalElements: 0,
        totalPages: 0,
        sort: {
            empty: true,
            sorted: false,
            unsorted: false
        }
    }

    const pageable = mapper.map(pageableDto, PageableDto, Pageable)

    expect(pageable.first).toEqual(pageableDto.first)
    expect(pageable.last).toEqual(pageableDto.last)
    expect(pageable.empty).toEqual(pageableDto.empty)
    expect(pageable.page).toEqual(pageableDto.number)
    expect(pageable.numberOfElements)
        .toEqual(pageableDto.numberOfElements)
    expect(pageable.size).toEqual(pageableDto.size)
    expect(pageable.totalElements)
        .toEqual(pageableDto.totalElements)
    expect(pageable.totalPages).toEqual(pageableDto.totalPages)
    expect(pageable.sort).toEqual(pageableDto.sort)
    expect(pageable.content).toEqual(pageableDto.content)
})

it('should map PageableDto<MobileOperatorDto> to Pageable<MobileOperator>', () => {

    const mobileOperatorsDto: MobileOperatorDto[] = [new MobileOperatorDto(), new MobileOperatorDto()]

    const pageableDto: PageableDto<MobileOperatorDto> = {
        first: true,
        last: true,
        content: mobileOperatorsDto,
        empty: true,
        number: 1,
        numberOfElements: 2,
        size: 2,
        totalElements: 2,
        totalPages: 1,
        sort: {
            empty: true,
            sorted: false,
            unsorted: false
        }
    }

    const pageable = mapper.map(pageableDto,
        PageableDto<MobileOperatorDto>,
        Pageable<MobileOperatorDto>
    )

    expect(pageable.content).toEqual(pageableDto.content)
    expect(pageable.content[0]).toBeInstanceOf(MobileOperatorDto)
})


it('should map PageableDto<MobileNumberPortingDto> to Pageable<MobileNumberPorting>', () => {

    const mobileNumberPortingDto: MobileNumberPortingDto[] = [new MobileNumberPortingDto()]

    const pageableDto: PageableDto<MobileNumberPortingDto> = {
        first: true,
        last: true,
        content: mobileNumberPortingDto,
        empty: true,
        number: 1,
        numberOfElements: 1,
        size: 1,
        totalElements: 1,
        totalPages: 1,
        sort: {
            empty: true,
            sorted: false,
            unsorted: false
        }
    }

    const pageable = mapper.map(pageableDto,
        PageableDto<MobileNumberPortingDto>,
        Pageable<MobileNumberPorting>
    )

    expect(pageable.content).toEqual(pageableDto.content)
    expect(pageable.content[0]).toBeInstanceOf(MobileNumberPorting)
})

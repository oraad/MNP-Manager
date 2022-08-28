import { Sortable } from './Sortable.model'

export class Pageable<T> {
    content: T[]
    // pageable: INSTANCE,
    first: boolean
    last: boolean
    empty: boolean
    size: number
    page: number
    numberOfElements: number
    totalPages!: number
    totalElements: number
    sort: Sortable

    constructor(
        first: boolean, last: boolean,
        empty: boolean, page: number,
        size: number, numberOfElements: number,
        totalPages: number, totalElements: number,
        sort: Sortable,
        content: T[]) {
        this.first = first
        this.last = last
        this.empty = empty
        this.page = page
        this.totalPages = totalPages
        this.size = size
        this.numberOfElements = numberOfElements
        this.totalElements = totalElements
        this.content = content
        this.sort = sort
    }

    static Empty() {
        return new Pageable(false, false, true, 0, 0, 0, 0, 0, Sortable.Empty(), [])
    }

}

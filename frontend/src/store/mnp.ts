import { StateCreator, StoreApi } from 'zustand'
import { produce } from 'immer'
import { MutatorsInput, MutatorsOutput, State } from './store'

export interface MNPSlice {
    mnpRequests: {
        all: PagingAndSorting
        accepted: PagingAndSorting
        pending: PagingAndSorting
    }
}

interface Pagination {
    pageIndex: number
    pageSize: number
}

type Sorting = SortingColumn[]

interface SortingColumn {
    id: string
    desc: boolean
}

interface PagingAndSorting {
    pagination: Pagination
    setPagination(paginationUpdated: Pagination
        | ((old: Pagination) => Pagination)): void
    resetPagination(): void
    sorting: Sorting
    setSorting(sorting: Sorting | ((old: Sorting) => Sorting)): void
}


const DEFAULT_PAGINATION: Pagination = { pageIndex: 0, pageSize: 10 }

const DEFAULT_SORTING: Sorting = []

export const createMNPSlice: StateCreator<State, MutatorsInput, MutatorsOutput, MNPSlice> = (set): MNPSlice => ({
    mnpRequests: {
        all: getPagingAndSortingState('all', set),
        accepted: getPagingAndSortingState('accepted', set),
        pending: getPagingAndSortingState('pending', set),
    }
})

const getPagingAndSortingState =
    (key: keyof MNPSlice['mnpRequests'],
        set: StoreApi<MNPSlice>['setState']
    ) => ({
        pagination: DEFAULT_PAGINATION,
        setPagination: (paginationUpdater: Pagination | ((old: Pagination) => Pagination)) =>
            set(
                produce((state: MNPSlice) => {
                    state.mnpRequests[key].pagination =
                        (paginationUpdater instanceof Function)
                            ? paginationUpdater(state.mnpRequests[key].pagination)
                            : paginationUpdater
                })
            ),
        resetPagination: () =>
            set(
                produce((state: MNPSlice) => {
                    state.mnpRequests[key].pagination.pageIndex = 0
                })
            ),
        sorting: DEFAULT_SORTING,
        setSorting: (sortingUpdater: Sorting | ((old: Sorting) => Sorting)) =>
            set(
                produce((state: MNPSlice) => {

                    state.mnpRequests[key].sorting =
                        (sortingUpdater instanceof Function)
                            ? sortingUpdater(state.mnpRequests[key].sorting)
                            : sortingUpdater
                })
            )
    })


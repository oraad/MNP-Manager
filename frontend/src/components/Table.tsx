import {
    ColumnDef, OnChangeFn, PaginationState,
    getCoreRowModel, getFilteredRowModel,
    getSortedRowModel, SortingState, useReactTable
} from '@tanstack/react-table'
import { TablePaging } from './TablePaging'
import { TableHeader } from './TableHeader'
import { TableBody } from './TableBody'

export type { PaginationState, SortingState, ColumnDef } from '@tanstack/react-table'

interface MNPTableProps<T> {
    data: T[]
    pageCount?: number
    firstPage?: boolean
    lastPage?: boolean
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    columns: ColumnDef<T, any>[]
    pagination?: PaginationState
    setPagination?: OnChangeFn<PaginationState>
    sorting?: SortingState
    setSorting?: OnChangeFn<SortingState>
}

export function Table<T>({
    data, columns, pageCount,
    pagination, setPagination,
    sorting, setSorting
}: MNPTableProps<T>) {

    const table = useReactTable({
        data,
        columns,
        state: {
            pagination,
            sorting
        },
        pageCount: pageCount ?? -1,
        onPaginationChange: setPagination,
        onSortingChange: setSorting,
        getCoreRowModel: getCoreRowModel(),
        getFilteredRowModel: getFilteredRowModel(),
        getSortedRowModel: getSortedRowModel(),
        manualPagination: true
        // debugTable: true,
    })


    return (
        <div className="p-2 h-full w-full flex flex-col">
            <div className="h-2" />
            <div className='flex-1'>
                <table className='w-full'>
                    <TableHeader table={table} />
                    <TableBody table={table} />
                </table>
            </div>
            <div className="h-2" />

            <TablePaging
                enable={pagination != undefined}
                table={table}
            />

            {/* <div>{instance.getRowModel().rows.length} Rows</div> */}
        </div>
    )


}

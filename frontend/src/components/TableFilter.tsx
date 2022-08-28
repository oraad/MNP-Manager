import { Column, Table } from '@tanstack/react-table'
import { PortingStatus } from '@/models/PortingStatus.model'

interface TableFilterProps<T> {
    enable: boolean
    column: Column<T, unknown>
    table: Table<T>
}

export function TableFilter<T>({ enable, column, table }: TableFilterProps<T>) {

    if (!enable) return null

    const firstValue = table
        .getPreFilteredRowModel()
        .flatRows[0]?.getValue(column.id)

    const columnFilterValue = column.getFilterValue()

    if (column.id === 'status')
        return (
            <select
                title="Status"
                value={(columnFilterValue ?? '') as string}
                onChange={e => column.setFilterValue(e.target.value)}
                className="w-36 border shadow rounded">
                <option>Select Status...</option>

                {/* TODO: find a way to make it generic */}
                {
                    Object.keys(PortingStatus).map(key =>
                        <option key={key} value={key}>{key}</option>
                    )
                }
            </select>
        )

    // if (firstValue instanceof Date)
    //     return (
    //         <input
    //             type="date"
    //             value={(columnFilterValue ?? '') as string}
    //             onChange={e => column.setFilterValue(e.target.value)}
    //             placeholder={`Search...`}
    //             className="w-36 border shadow rounded"
    //         />
    //     )


    if (typeof firstValue === 'number')
        return (
            <div className="flex space-x-2">
                <input
                    type="number"
                    value={(columnFilterValue as [number, number])?.[0] ?? ''}
                    onChange={e =>
                        column.setFilterValue((old: [number, number]) => [
                            e.target.value,
                            old?.[1],
                        ])
                    }
                    placeholder={'Min'}
                    className="w-24 border shadow rounded"
                />
                <input
                    type="number"
                    value={(columnFilterValue as [number, number])?.[1] ?? ''}
                    onChange={e =>
                        column.setFilterValue((old: [number, number]) => [
                            old?.[0],
                            e.target.value,
                        ])
                    }
                    placeholder={'Max'}
                    className="w-24 border shadow rounded"
                />
            </div>
        )

    return (
        <input
            type="text"
            value={(columnFilterValue ?? '') as string}
            onChange={e => column.setFilterValue(e.target.value)}
            placeholder={'Search...'}
            className="w-36 border shadow rounded"
        />
    )

}

import { flexRender, Table } from '@tanstack/react-table'
import { TableFilter } from './TableFilter'

interface TableHeaderProps<T> {
    table: Table<T>
}

export function TableHeader<T>({ table }: TableHeaderProps<T>) {

    return (
        <thead>
            {table.getHeaderGroups().map(headerGroup => (
                <tr key={headerGroup.id}>
                    {headerGroup.headers.map(header => {
                        return (
                            <th key={header.id} colSpan={header.colSpan}>
                                {
                                    header.isPlaceholder ? null : (
                                        <div>
                                            <div
                                                className={header.column.getCanSort()
                                                    ? 'cursor-pointer select-none'
                                                    : ''}
                                                onClick={header.column.getToggleSortingHandler()}
                                            >
                                                {flexRender(
                                                    header.column.columnDef.header,
                                                    header.getContext()
                                                )}
                                                {{
                                                    asc: ' 🔼',
                                                    desc: ' 🔽',
                                                }[header.column.getIsSorted() as string] ?? null}
                                            </div>
                                            <div>
                                                <TableFilter
                                                    enable={header.column.getCanFilter()}
                                                    column={header.column}
                                                    table={table}
                                                />
                                            </div>
                                        </div>
                                    )
                                }
                            </th>
                        )
                    })}
                </tr>
            ))
            }
        </thead >
    )
}

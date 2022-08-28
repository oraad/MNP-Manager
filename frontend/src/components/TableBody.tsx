import { flexRender, Table } from '@tanstack/react-table'

interface TableBodyProps<T> {
    table: Table<T>
}

export function TableBody<T>({ table }: TableBodyProps<T>) {

    return (
        <tbody>
            {table.getRowModel().rows.map(row => {
                return (
                    <tr key={row.id}>
                        {row.getVisibleCells().map(cell => {
                            return (
                                <td key={cell.id} className='text-center'>
                                    {
                                        flexRender(
                                            cell.column.columnDef.cell,
                                            cell.getContext()
                                        )
                                    }
                                </td>
                            )
                        })}
                    </tr>
                )
            })}
        </tbody>
    )
}

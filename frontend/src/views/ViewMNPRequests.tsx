import { FC, useEffect, useRef, useState } from 'react'

import { getAllMobileNumberPorting } from '@/services/mobileNumberPortingService'
import { toastError } from '@/utils/toastHandler'
import { Table, ColumnDef } from '@/components/Table'
import { MobileNumberPorting } from '@/models/MobileNumberPorting.model'
import { StatusCell } from '@/components/StatusCell'
import { DateCell } from '@/components/DateCell'
import { Link } from 'react-router-dom'
import { Pageable } from '@/models/Pageable.model'
import { useStore } from '@/store/store'
import shallow from 'zustand/shallow'
import { createColumnHelper } from '@tanstack/react-table'

interface ViewMNPRequestsProps {

}

const columnHelper = createColumnHelper<MobileNumberPorting>()

// eslint-disable-next-line @typescript-eslint/no-explicit-any
const columns: ColumnDef<MobileNumberPorting, any>[] = [
    columnHelper.accessor('mobileNumber', {
        cell: info => <Link
            className='text-indigo-600'
            to={`/view/${info.getValue()}`}>{info.getValue()}</Link>,
        header: () => <span>Mobile Number</span>
    }),
    columnHelper.accessor('donorOperator', {
        cell: info => info.getValue(),
        header: () => <span>Donor</span>
    }),
    columnHelper.accessor('recipientOperator', {
        cell: info => info.getValue(),
        header: () => <span>Recipient</span>
    }),
    columnHelper.accessor('createdOn', {
        cell: info =>
            <DateCell date={info.getValue()} pattern="YYYY-MM-DD HH:mm" />,
        header: () => <span>Created On</span>
    }),
    columnHelper.accessor('updatedOn', {
        cell: info => <DateCell date={info.getValue()} pattern="YYYY-MM-DD HH:mm" />,
        header: () => <span>Updated On</span>
    }),
    columnHelper.accessor('status', {
        cell: info => <StatusCell status={info.getValue()} />,
        header: () => <span>Status</span>
    })
]

export const ViewMNPRequests: FC<ViewMNPRequestsProps> = () => {

    const {
        pagination, setPagination,
        resetPagination,
        sorting, setSorting
    } = useStore(state => state.mnpRequests.all, shallow)

    const [data, setData] =
        useState<Pageable<MobileNumberPorting>>(Pageable.Empty())

    const abortGetMNP = useRef<AbortController>()

    useEffect(() => {
        resetPagination()
    }, [])

    useEffect(() => {
        abortGetMNP.current?.abort()
        abortGetMNP.current = new AbortController()

        const { pageIndex, pageSize } = pagination

        getAllMobileNumberPorting({
            page: pageIndex,
            size: pageSize,
            sorting
        }, abortGetMNP.current.signal)
            .then(pageableResult => {
                setData(pageableResult)
            })
            .catch(toastError)
    }, [pagination.pageIndex, pagination.pageSize, sorting])

    return (
        <Table
            columns={columns}
            data={data.content}
            pageCount={data.totalPages}
            pagination={pagination}
            setPagination={setPagination}
            sorting={sorting}
            setSorting={setSorting} />
    )
}

export default ViewMNPRequests

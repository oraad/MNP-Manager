import { FC, useRef, useState } from 'react'

import { getAllAcceptedMobileNumberPorting } from '@/services/mobileNumberPortingService'
import { toastError } from '@/utils/toastHandler'
import { ColumnDef, Table } from '@/components/Table'
import { MobileNumberPorting } from '@/models/MobileNumberPorting.model'
import { DateCell } from '@/components/DateCell'
import { Link } from 'react-router-dom'
import { useInterval } from '@/hooks/useInterval'
import { Pageable } from '@/models/Pageable.model'
import { useStore } from '@/store/store'
import shallow from 'zustand/shallow'
import { createColumnHelper } from '@tanstack/react-table'

interface ViewMNPAcceptedRequestsProps {

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
    columnHelper.accessor('createdOn', {
        cell: info => <DateCell date={info.getValue()} pattern="YYYY-MM-DD HH:mm" />,
        header: () => <span>Created On</span>
    }),
    columnHelper.accessor('updatedOn', {
        cell: info => <DateCell date={info.getValue()} pattern="YYYY-MM-DD HH:mm" />,
        header: () => <span>Updated On</span>
    })
]

export const ViewMNPAcceptedRequests: FC<ViewMNPAcceptedRequestsProps> = () => {

    const {
        pagination, setPagination, sorting
    } = useStore(state => state.mnpRequests.accepted, shallow)

    const [data, setData] = useState<Pageable<MobileNumberPorting>>(Pageable.Empty())

    const abortGetMNPAccepted = useRef<AbortController>()

    useInterval(() => {

        abortGetMNPAccepted.current?.abort()
        abortGetMNPAccepted.current = new AbortController()

        const { pageIndex, pageSize } = pagination

        getAllAcceptedMobileNumberPorting({
            page: pageIndex,
            size: pageSize,
            sorting
        }, abortGetMNPAccepted.current.signal)
            .then(pageableResult => {
                setData(pageableResult)
            })
            .catch(toastError)

    }, 10000, true, [pagination.pageIndex, pagination.pageSize, sorting])

    return (
        <Table
            columns={columns}
            data={data.content}
            pageCount={data.totalPages}
            pagination={pagination}
            setPagination={setPagination} />
    )
}

export default ViewMNPAcceptedRequests

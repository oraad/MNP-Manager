import { FC, useMemo, useRef, useState } from 'react'

import {
    applyActionForMobileNumberPorting,
    getAllPendingMobileNumberPorting
} from '@/services/mobileNumberPortingService'
import { toastError, toastInfo } from '@/utils/toastHandler'
import { Table, ColumnDef } from '@/components/Table'
import { DateCell } from '@/components/DateCell'
import { Link } from 'react-router-dom'
import { ActionCell } from '@/components/ActionCell'
import { MobileNumberPortingWithActions } from '@/models/MobileNumberPortingWithActions.model'
import { useInterval } from '@/hooks/useInterval'
import { useStore } from '@/store/store'
import shallow from 'zustand/shallow'
import { removeItemByProperty } from '@/utils/arrayUtils'
import { createColumnHelper } from '@tanstack/react-table'

interface ViewMNPPendingRequestsProps {

}

interface ColumnActions {
    onAction(id: number, uri: string): void
}

const columnHelper = createColumnHelper<MobileNumberPortingWithActions>()

const getColumns =
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    ({ onAction }: ColumnActions): ColumnDef<MobileNumberPortingWithActions, any>[] => [
        columnHelper.accessor('mobileNumber', {
            cell: info => <Link
                className='text-indigo-600'
                to={`/view/${info.getValue()}`}>{info.getValue()}</Link>,
            header: () => <span>Mobile Number</span>
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
        columnHelper.accessor('actions', {
            cell: info => <ActionCell
                id={info.row.original.id}
                actions={info.getValue()}
                onAction={onAction} />,
            header: () => <span>Actions</span>,
            enableColumnFilter: false
        })
    ]

export const ViewMNPPendingRequests: FC<ViewMNPPendingRequestsProps> = () => {

    const { sorting, setSorting } =
        useStore(state => state.mnpRequests.pending, shallow)

    const [data, setData] = useState<MobileNumberPortingWithActions[]>([])

    const abortGetMNPPending = useRef<AbortController>()

    useInterval(() => {
        abortGetMNPPending.current?.abort()
        abortGetMNPPending.current = new AbortController()

        getAllPendingMobileNumberPorting(
            undefined,
            abortGetMNPPending.current.signal)
            .then(pageableResult => {
                setData(pageableResult.content)
            })
            .catch(toastError)
    }, 10000, true, [sorting])

    const handleAction = (id: number, uri: string) => {
        applyActionForMobileNumberPorting(uri)
            .then(() => {
                toastInfo('Action success.')

                // Update pending list until next fetch
                const transientData =
                    removeItemByProperty(data, { key: 'id', value: id })
                setData(transientData)
            })
            .catch(toastError)
    }

    const columns = useMemo(() => getColumns({ onAction: handleAction }), [handleAction])

    return (
        <Table
            columns={columns}
            data={data}
            sorting={sorting}
            setSorting={setSorting}
        />
    )
}

export default ViewMNPPendingRequests

import { FC } from 'react'
import { PortingStatus } from '@/models/PortingStatus.model'

interface StatusCellProps {
    status: PortingStatus
}

export const StatusCell: FC<StatusCellProps> = ({ status }) => {

    const getTextColor = (status: PortingStatus) => {
        switch (status) {
            case PortingStatus.ACCEPTED: return 'text-green-600'
            case PortingStatus.CANCELED: return 'text-grey-600'
            case PortingStatus.REJECTED: return 'text-red-600'
            case PortingStatus.PENDING: return 'text-blue-700'
            case PortingStatus.UNKNOWN: return 'text-yellow-700'
        }
    }
    return (
        <div className={`text-center ${getTextColor(status)} font-bold`}>{status}</div>
    )
}

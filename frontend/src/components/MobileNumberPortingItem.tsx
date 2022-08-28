import classNames from 'classnames'
import dayjs from 'dayjs'
import { FC } from 'react'
import { MobileNumberPorting } from '@/models/MobileNumberPorting.model'
import { PortingStatus } from '@/models/PortingStatus.model'

interface MobileNumberPortingItemProps {
    data?: MobileNumberPorting
    className?: string
}

export const MobileNumberPortingItem: FC<MobileNumberPortingItemProps> = ({ data, className }) => {

    if (data == undefined) return null

    const dateTimeFormat = 'YYYY-MM-DD HH:mm:ss'

    const formatStatus = (status: PortingStatus) => {
        switch (status) {
            case PortingStatus.ACCEPTED: return 'text-green-600'
            case PortingStatus.CANCELED: return 'text-grey-600'
            case PortingStatus.REJECTED: return 'text-red-600'
            case PortingStatus.PENDING: return 'text-blue-700'
            case PortingStatus.UNKNOWN: return 'text-yellow-700'
        }
    }

    return (
        <div className={classNames('flex flex-col gap-y-6', className)}>
            <div className='text-2xl text-slate-600'>
                #{data.mobileNumber}
            </div>
            <div className='grid grid-cols-2 gap-y-4 gap-x-20'>
                <div className='text-gray-500'>Donor Operator</div>
                <div className='text-lg'>{data.donorOperator}</div>

                <div className='text-gray-500'>Recipient Operator</div>
                <div className='text-lg'>{data.recipientOperator}</div>

                <div className='text-gray-500'>Created on</div>
                <div className='text-lg'>{dayjs(data.createdOn).format(dateTimeFormat)}</div>

                <div className='text-gray-500'>Updated on</div>
                <div className='text-lg'>{dayjs(data.updatedOn).format(dateTimeFormat)}</div>

                <div className='text-gray-500'>Status</div>
                <div className={`${formatStatus(data.status)} text-lg`}>{data.status}</div>
            </div>
        </div>
    )
}

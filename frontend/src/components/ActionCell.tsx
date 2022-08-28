import { CheckIcon, XMarkIcon } from '@heroicons/react/20/solid'
import { FC } from 'react'
import { MobileNumberPortingActions } from '@/models/MobileNumberPortingActions.model'

interface ActionCellProps {
    id: number
    actions?: MobileNumberPortingActions
    onAction?(id: number, uri: string): void
}

export const ActionCell: FC<ActionCellProps> = ({ id, actions, onAction }) => {

    if (id == undefined || actions == undefined)
        return null

    return (

        <div className='flex flex-row justify-evenly'>
            <button
                className='text-green-600'
                onClick={() => onAction?.(id, actions.accept)}
                type='button'
                title='Accept'>
                <CheckIcon className='h-5 w-5' />
            </button>
            <button
                className='text-red-600'
                onClick={() => onAction?.(id, actions.reject)}
                type='button'
                title='Reject'>
                <XMarkIcon className='h-5 w-5' />
            </button>
        </div>

    )
}

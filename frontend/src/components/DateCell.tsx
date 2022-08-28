import dayjs from 'dayjs'
import { FC } from 'react'

interface DateCellProps {
    date?: Date
    pattern: string
}

export const DateCell: FC<DateCellProps> = ({ date, pattern }) => {

    if (date == undefined) return null

    return (
        <>
            {dayjs(date).format(pattern)}
        </>
    )

}

import { FC, useEffect, useRef, useState } from 'react'
import { useParams } from 'react-router-dom'
import { MobileNumberPortingItem } from '@/components/MobileNumberPortingItem'
import { MobileNumberPorting } from '@/models/MobileNumberPorting.model'
import { getMobileNumberPorting } from '@/services/mobileNumberPortingService'
import { toastError } from '@/utils/toastHandler'

interface ViewMobileNumberProps {}

export const ViewMobileNumber: FC<ViewMobileNumberProps> = () => {

    const { mobileNumber } = useParams()
    const [mobileNumberPorting, setMobileNumberPorting] = useState<MobileNumberPorting>()
    const abortGetMNP = useRef<AbortController>()

    useEffect(() => {

        abortGetMNP.current?.abort()

        if (mobileNumber == undefined) return

        abortGetMNP.current = new AbortController()

        getMobileNumberPorting(mobileNumber, abortGetMNP.current.signal)
            .then(mobileNumberPortingResult => {
                setMobileNumberPorting(mobileNumberPortingResult)
            })
            .catch(toastError)

    }, [])

    return (
        <div className='h-full w-full flex'>
            <MobileNumberPortingItem
                data={mobileNumberPorting}
                className={'m-auto'}
            />
        </div>
    )
}

export default ViewMobileNumber

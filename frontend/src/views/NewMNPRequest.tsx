import { FC, useRef, useState } from 'react'
import { createMobileNumberPorting } from '@/services/mobileNumberPortingService'
import { toastError, toastInfo } from '@/utils/toastHandler'

interface NewMNPRequestProps {

}

export const NewMNPRequest: FC<NewMNPRequestProps> = () => {

    const [mobileNumber, setMobileNumber] = useState('')
    const abortLogin = useRef<AbortController>()


    const handleSubmit: React.FormEventHandler<HTMLFormElement> = (event) => {
        event.preventDefault()

        abortLogin.current?.abort()
        abortLogin.current = new AbortController()

        createMobileNumberPorting(mobileNumber, abortLogin.current.signal)
            .then(() => {
                toastInfo('Request created successfully.')
                setMobileNumber('')
            })
            .catch(toastError)
    }

    return (
        <section className="h-full w-full">
            <div className="container mx-auto py-12 h-full">
                <div className="flex justify-center items-center flex-wrap h-full g-6 text-gray-800">
                    <div className="md:w-8/12 lg:w-5/12 lg:ml-20">
                        <form onSubmit={handleSubmit}>
                            <div className="mb-6">
                                <label>Enter mobile number to port:
                                    <input
                                        required
                                        minLength={8}
                                        onInput={(evt) => evt.currentTarget.value = evt.currentTarget.value.replace(/[^0-9]/g, '')}
                                        type="tel"
                                        value={mobileNumber}
                                        className={'form-control block w-full px-4 py-2 ' +
                                            'text-xl font-normal text-gray-700 bg-white bg-clip-padding border border-solid ' +
                                            'border-gray-300 rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white ' +
                                            'focus:border-blue-600 focus:outline-none'}
                                        placeholder="Mobile number"
                                        onChange={evt => setMobileNumber(evt.target.value)}
                                    />
                                </label>
                            </div>

                            <button
                                type="submit"
                                className={'inline-block px-7 py-3 bg-blue-600 text-white font-medium text-sm ' +
                                    'leading-snug uppercase rounded shadow-md hover:bg-blue-700 hover:shadow-lg focus:bg-blue-700 ' +
                                    'focus:shadow-lg focus:outline-none focus:ring-0 active:bg-blue-800 active:shadow-lg transition ' +
                                    'duration-150 ease-in-out w-full'}
                                data-mdb-ripple="true"
                                data-mdb-ripple-color="light"
                            >
                                Request Port
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    )
}

export default NewMNPRequest

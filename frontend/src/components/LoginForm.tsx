import { FC, FormEventHandler, useState } from 'react'

interface LoginFormProps {
    onSubmit?(operatorName: string): void
}

export const LoginForm: FC<LoginFormProps> = ({ onSubmit }) => {


    const [operatorName, setOperatorName] = useState('')
    const [isValidOperatorName, setIsValidOperatorName] = useState(true)

    const handleSubmit: FormEventHandler<HTMLFormElement> = (event) => {
        event.preventDefault()
        if (validateOperatorName(operatorName))
            onSubmit?.(operatorName)
    }

    const validateOperatorName = (operatorName: string) => {

        if (operatorName == undefined || operatorName.trim() == '') {
            setIsValidOperatorName(false)
            return false
        }

        setIsValidOperatorName(true)
        return true
    }

    return (
        <section className="h-full w-full">
            <div className="container mx-auto py-12 h-full">
                <div className="flex justify-center items-center flex-wrap h-full g-6 text-gray-800">
                    <div className="md:w-8/12 lg:w-5/12 lg:ml-20">
                        <form onSubmit={handleSubmit}>
                            <div className="mb-6">
                                <label>Enter mobile operator name:
                                    <input
                                        name="operator-name"
                                        type="text"
                                        className={'form-control block w-full px-4 py-2 ' +
                                            'text-xl font-normal text-gray-700 bg-white bg-clip-padding border border-solid ' +
                                            'border-gray-300 rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white ' +
                                            'focus:border-blue-600 focus:outline-none'}
                                        placeholder="Operator Name"
                                        onChange={evt => setOperatorName(evt.target.value)}
                                    />
                                </label>
                                {
                                    !isValidOperatorName
                                        ? <span id='validation-error' className='text-red-500'>Operator name cannot be empty!</span>
                                        : null
                                }
                            </div>

                            <button
                                name="signin"
                                type="submit"
                                className={'inline-block px-7 py-3 bg-blue-600 text-white font-medium text-sm ' +
                                    'leading-snug uppercase rounded shadow-md hover:bg-blue-700 hover:shadow-lg focus:bg-blue-700 ' +
                                    'focus:shadow-lg focus:outline-none focus:ring-0 active:bg-blue-800 active:shadow-lg transition ' +
                                    'duration-150 ease-in-out w-full'}
                                data-mdb-ripple="true"
                                data-mdb-ripple-color="light"
                            >
                                Sign in
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    )
}

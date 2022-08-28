import toast from 'react-hot-toast'

export const toastError = (error: Error) => {

    if (error instanceof DOMException)
        return

    console.error(error)
    toast.error(error.message)
}


export const toastInfo = (msg: string) => {
    toast.success(msg)
}

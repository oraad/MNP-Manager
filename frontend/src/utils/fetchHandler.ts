import { plainToInstance, plainToClassFromExist } from 'class-transformer'
import { ErrorResponseDto } from '@/dtos/ErrorResponseDto.dto'
import { ClassConstructor } from '@/types/class'


export const statusHandler = async (response: Response) => {

    if (response.status >= 400) {
        const errorResponse = plainToInstance(ErrorResponseDto, await response.json())
        throw new RequestError(errorResponse.message ?? errorResponse.error)
    }

    return response
}

export const jsonParser =
    <T>(cls: ClassConstructor<T>) =>
        async (response: Response): Promise<T> =>
            plainToInstance(cls, await response.json())

export const jsonParserGeneric =
            <T>(clsObject: T) =>
                async (response: Response): Promise<T> =>
                    plainToClassFromExist(clsObject, await response.json())

export const errorHandler = (error: Error) => {
    if (error instanceof TypeError) {
        if (error.message.startsWith('Failed to fetch'))
            throw new RequestError('Unknown request error')
    }

    if (error instanceof DOMException) {
        console.log(error)
    }

    throw error
}

class RequestError extends Error {}

import { ErrorResponseDto } from '@/dtos/ErrorResponseDto.dto'
import { PageableDto } from '@/dtos/PageableDto.dto'
import { PortingStatusDto } from '@/dtos/PortingStatusDto.dto'
import httpStatus from 'http-status'
import { DefaultBodyType, ResponseComposition, rest, RestContext } from 'msw'
import { mobileNumberPortingDtos, mobileOperatorDtos } from './data'

const HOST_URL = import.meta.env.VITE_MNP_API_URL

interface SortOrder {
    property: string
    direction: 'desc' | 'asc'
}

const loginCredentialHandler = rest.post(`${HOST_URL}/auth/login`, async (req, res, ctx) => {

    const { operatorName } = await req.json() as { operatorName?: string }

    if (operatorName == undefined)
        return throwError(res, ctx)(400, 'Required request body is missing.')

    const mobileOperatorDto = mobileOperatorDtos.find(mobileOperatorDto =>
        mobileOperatorDto.operatorName == operatorName)

    if (mobileOperatorDto == undefined)
        return throwError(res, ctx)(400, 'Operator not found.')

    return res(ctx.status(200), ctx.json(mobileOperatorDto))
})

const loginOrganizationHeaderHandler = rest.post(`${HOST_URL}/auth`, (req, res, ctx) => {

    const organizationHeader = req.headers.get('organization')

    if (organizationHeader == undefined)
        return throwError(res, ctx)(400, 'Required request header `organization` is not present')

    const mobileOperatorDto = mobileOperatorDtos.find(mobileOperatorDto =>
        mobileOperatorDto.organizationHeader == organizationHeader)

    if (mobileOperatorDto == undefined)
        return throwError(res, ctx)(400, 'Operator not found.')

    return res(ctx.status(200), ctx.json(mobileOperatorDto))
})

const getAllMobileNumberPortingHandler = rest.get(`${HOST_URL}/mnp`, (req, res, ctx) => {

    const organizationHeader = req.headers.get('organization')

    if (organizationHeader == undefined)
        return throwError(res, ctx)(400, 'Required request header `organization` is not present')

    const { page, size, sort } = parsePageableRequest(req.url)

    const mobileOperatorDto = mobileOperatorDtos.find(mobileOperatorDto =>
        mobileOperatorDto.organizationHeader == organizationHeader)

    if (mobileOperatorDto == undefined)
        return throwError(res, ctx)(400, 'Operator not found.')

    const operatorName = mobileOperatorDto.operatorName

    const filteredMobileNumberPortingDtos =
        mobileNumberPortingDtos.filter(mobileNumberPortingDto =>
            mobileNumberPortingDto.status == PortingStatusDto.ACCEPTED
            || mobileNumberPortingDto.donorOperator == operatorName
            || mobileNumberPortingDto.recipientOperator == operatorName)

    const pageableResult = getPageableResult(filteredMobileNumberPortingDtos, page, size, sort)

    return res(ctx.status(200), ctx.json(pageableResult))

})

export const handlers = [
    loginCredentialHandler,
    loginOrganizationHeaderHandler,
    getAllMobileNumberPortingHandler
]

const getPageableResult =
    <T>(data: T[] = [], page = 1, size = 10, sort?: SortOrder[]): PageableDto<T> => {

        const startIdx = (page - 1) * size,
            endIdx = startIdx + size

        const totalElements = data.length
        const totalPages = Math.ceil(data.length / size)
        const content = data.slice(startIdx, endIdx)
        const isEmpty = content.length == 0
        const isFirst = startIdx == 0
        const isLast = endIdx >= content.length

        if (sort != undefined)
            (content as Record<string, unknown>[]).sort((a, b) =>
                sort.reduce((previousCompare: number, sortOrder) => {

                    const valueA = a[sortOrder.property]
                    const valueB = b[sortOrder.property]
                    const isReverse = sortOrder.direction == 'desc'

                    return previousCompare || comparePrimitives(valueA, valueB, isReverse)

                }, 0)
            )

        return {
            first: isFirst,
            last: isLast,
            content: content,
            empty: isEmpty,
            totalElements: totalElements,
            size: size,
            number: page,
            totalPages: totalPages,
            numberOfElements: content.length,
            sort: {
                empty: data.length == 0,
                sorted: true,
                unsorted: false
            }
        }
    }

const parsePageableRequest = (url: URL) => {

    const searchParams = url.searchParams
    const page = parseInt(searchParams.get('page') ?? '1')
    const size = parseInt(searchParams.get('size') ?? '10')

    const sortParams = searchParams.getAll('sort')

    const sort = sortParams.map(sortParam => {
        const [property, direction] = sortParam.split(',')
        return { property, direction } as SortOrder
    })

    return {
        page,
        size,
        sort
    }
}

const comparePrimitives = (a: unknown, b: unknown, isReverse?: boolean) => {

    if (typeof a == 'number' && typeof b == 'number')
        return (a - b) * (isReverse ? -1 : 1)

    if (typeof a == 'string' && typeof b == 'string')
        return a.localeCompare(b) * (isReverse ? -1 : 1)

    return 0
}

const errorHandler =
    (status: number, message: string): ErrorResponseDto => ({
        status,
        error: httpStatus[status] as string,
        message
    })


const throwError =
    (res: ResponseComposition<DefaultBodyType>, ctx: RestContext) =>
        (status: number, message: string) =>
            res(ctx.status(status), ctx.json(errorHandler(status, message)))


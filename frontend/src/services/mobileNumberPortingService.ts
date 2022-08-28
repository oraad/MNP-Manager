import { MobileNumberPortingDto } from '@/dtos/MobileNumberPortingDto.dto'
import { MobileNumberPortingWithActionsDto } from '@/dtos/MobileNumberPortingWithActionsDto.dto'
import { MobileOperatorDto } from '@/dtos/MobileOperatorDto.dto'
import { PageableDto } from '@/dtos/PageableDto.dto'
import { mapper } from '@/mappings/mapper'
import { MobileNumberPorting } from '@/models/MobileNumberPorting.model'
import { MobileNumberPortingWithActions } from '@/models/MobileNumberPortingWithActions.model'
import { MobileOperator } from '@/models/MobileOperator.model'
import { Pageable } from '@/models/Pageable.model'
import { useStore } from '@/store/store'
import { errorHandler, jsonParser, jsonParserGeneric, statusHandler } from '@/utils/fetchHandler'

const HOST_URL = import.meta.env.VITE_MNP_API_URL

const GET_METHOD = 'GET'
const POST_METHOD = 'POST'

export interface PageableAndSortingOptions {
    page?: number
    size?: number,
    sorting?: { id: string, desc: boolean }[]
}

const getRequestHeaders = () => {
    const organizationHeader = useStore.getState().auth.organizationHeader

    const requestHeaders: HeadersInit = new Headers()
    requestHeaders.append('Content-Type', 'application/json')
    requestHeaders.append('organization', organizationHeader ?? '')

    return requestHeaders
}

export const loginAgentByOperatorName =
    async (operatorName: string, signal?: AbortSignal): Promise<MobileOperator> => {

        const requestBody = JSON.stringify({ operatorName })

        return fetch(`${HOST_URL}/auth/login`, {
            method: POST_METHOD,
            headers: getRequestHeaders(),
            body: requestBody,
            signal
        })
            .then(statusHandler)
            .then(jsonParser(MobileOperatorDto))
            .then(mobileOperatorDto =>
                mapper.map(mobileOperatorDto, MobileOperatorDto, MobileOperator))
            .catch(errorHandler)
    }

export const loginAgentByOrganizationHeader =
    async (organizationHeader: string, signal?: AbortSignal):
        Promise<MobileOperator> => {

        const requestHeaders = getRequestHeaders()
        requestHeaders.set('organization', organizationHeader)

        return fetch(`${HOST_URL}/auth`, {
            method: POST_METHOD,
            headers: requestHeaders,
            signal
        })
            .then(statusHandler)
            .then(jsonParser(MobileOperatorDto))
            .then(mobileOperatorDto => mapper.map(mobileOperatorDto, MobileOperatorDto, MobileOperator))
            .catch(errorHandler)
    }

export const getAllMobileNumberPorting =
    async (opts?: PageableAndSortingOptions, signal?: AbortSignal):
        Promise<Pageable<MobileNumberPorting>> => {

        const queryParams = new URLSearchParams()
        if (opts?.page != undefined) {
            queryParams.append('page', opts.page.toString())
        }

        if (opts?.size != undefined) {
            queryParams.append('size', opts.size.toString())
        }

        if (opts?.sorting != undefined) {
            for (const sortingItem of opts.sorting) {
                const dtoProperty = sortingItem.id
                queryParams.append('sort',
                    `${dtoProperty},${sortingItem.desc ? 'desc' : 'asc'}`)
            }
        }

        return fetch(`${HOST_URL}/mnp?${queryParams.toString()}`, {
            method: GET_METHOD,
            headers: getRequestHeaders(),
            signal
        })
            .then(statusHandler)
            .then(jsonParserGeneric(new PageableDto(MobileNumberPortingDto)))
            .then(pageableDto =>
                mapper.map(pageableDto,
                    PageableDto<MobileNumberPortingDto>,
                    Pageable<MobileNumberPorting>
                )
            )
            .catch(errorHandler)
    }


export const getAllAcceptedMobileNumberPorting =
    async (opts?: PageableAndSortingOptions, signal?: AbortSignal):
        Promise<Pageable<MobileNumberPorting>> => {

        const queryParams = new URLSearchParams()
        if (opts?.page != undefined) {
            queryParams.append('page', opts.page.toString())
        }

        if (opts?.size != undefined) {
            queryParams.append('size', opts.size.toString())
        }

        if (opts?.sorting != undefined) {
            for (const sortingItem of opts.sorting) {
                const dtoProperty = sortingItem.id
                queryParams.append('sort',
                    `${dtoProperty},${sortingItem.desc ? 'desc' : 'asc'}`)
            }
        }

        return fetch(`${HOST_URL}/mnp/accepted?${queryParams.toString()}`, {
            method: GET_METHOD,
            headers: getRequestHeaders(),
            signal
        })
            .then(statusHandler)
            .then(jsonParserGeneric(new PageableDto(MobileNumberPortingDto)))
            .then(pageableDto =>
                mapper.map(pageableDto,
                    PageableDto<MobileNumberPortingDto>,
                    Pageable<MobileNumberPorting>
                )
            )
            .catch(errorHandler)
    }

export const getAllPendingMobileNumberPorting =
    async (opts?: PageableAndSortingOptions, signal?: AbortSignal):
        Promise<Pageable<MobileNumberPortingWithActions>> => {

        const queryParams = new URLSearchParams()
        if (opts?.page != undefined) {
            queryParams.append('page', opts.page.toString())
        }

        if (opts?.size != undefined) {
            queryParams.append('size', opts.size.toString())
        }

        if (opts?.sorting != undefined) {
            for (const sortingItem of opts.sorting) {
                const dtoProperty = sortingItem.id
                queryParams.append('sort',
                    `${dtoProperty},${sortingItem.desc ? 'desc' : 'asc'}`)
            }
        }

        return fetch(`${HOST_URL}/mnp/pending?${queryParams.toString()}`, {
            method: GET_METHOD,
            headers: getRequestHeaders(),
            signal
        })
            .then(statusHandler)
            .then(jsonParserGeneric(
                new PageableDto(MobileNumberPortingWithActionsDto)
            ))
            .then(pageableDto =>
                mapper.map(pageableDto,
                    PageableDto<MobileNumberPortingWithActionsDto>,
                    Pageable<MobileNumberPortingWithActions>
                )
            )
            .catch(errorHandler)
    }

export const getMobileNumberPorting =
    async (mobileNumber: string, signal?: AbortSignal):
        Promise<MobileNumberPorting> => {

        return fetch(`${HOST_URL}/mnp/${mobileNumber}`, {
            method: GET_METHOD,
            headers: getRequestHeaders(),
            signal
        })
            .then(statusHandler)
            .then(jsonParser(MobileNumberPortingDto))
            .then(mobileNumberPortingDto =>
                mapper.map(mobileNumberPortingDto, MobileNumberPortingDto, MobileNumberPorting))
            .catch(errorHandler)
    }

export const createMobileNumberPorting =
    async (mobileNumber: string, signal?: AbortSignal): Promise<void> => {

        return fetch(`${HOST_URL}/mnp/${mobileNumber}`, {
            method: POST_METHOD,
            headers: getRequestHeaders(),
            signal
        })
            .then(statusHandler)
            .then(resp => {
                if (resp.status != 201)
                    throw new Error(resp.statusText)
            })
            .catch(errorHandler)
    }

export const applyActionForMobileNumberPorting =
    async (uri: string, signal?: AbortSignal): Promise<void> => {

        return fetch(`${HOST_URL}/${uri}`, {
            method: POST_METHOD,
            headers: getRequestHeaders(),
            signal
        })
            .then(statusHandler)
            .then(resp => {
                if (!resp.ok)
                    throw new Error(resp.statusText)
            })
            .catch(errorHandler)
    }

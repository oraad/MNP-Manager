import { StateCreator } from 'zustand'

export type StoreSlice<T extends object, E extends object = T> =
    StateCreator<E extends T ? E : E & T, any, any>

// export type StoreSlice<T extends object> = GetState<T> | SetState<T> | StoreApi<T>

export type StateFromSlices<T extends [...any]> = T extends [infer F, ...(infer R)]
    ? F extends (...args: any) => object
    ? StateFromSlices<R> & ReturnType<F>
    : unknown
    : unknown

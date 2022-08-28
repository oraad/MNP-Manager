import { describe, expect, it } from 'vitest'
import { removeItemByProperty } from './arrayUtils'


const testArray = [
    {
        id: 1,
        name: 'A'
    },
    {
        id: 2,
        name: 'B'
    },
    {
        id: 3,
        name: 'B'
    },
    {
        id: 4,
        name: 'C'
    },
    {
        id: 5,
        name: 'C'
    }
]

const testArray2 = [
    {
        id: 1,
        name: () => 'A'
    },
    {
        id: 2,
        name: () => 'B'
    },
    {
        id: 3,
        name: () => 'B'
    },
    {
        id: 4,
        name: () => 'C'
    },
    {
        id: 5,
        name: () => 'C'
    }
]

describe('Test array utils', () => {

    it('should remove item by property', () => {

        const updatedArray =
            removeItemByProperty(testArray, { key: 'id', value: 3 })

        expect(updatedArray).not.toContain({ id: 3, name: 'B' })

        const updatedArray2 =
            removeItemByProperty(testArray, { key: 'name', value: 'B' })

        expect(updatedArray2).not.toContain({ id: 2, name: 'B' })
        expect(updatedArray2).not.toContain({ id: 3, name: 'B' })
    })

    it('should remove item by resolved property', () => {

        const updatedArray =
            removeItemByProperty(testArray2, { key: 'name', value: 'B' })

        expect(updatedArray).not.toContain({ id: 2, name: () => 'B' })
        expect(updatedArray).not.toContain({ id: 3, name: () => 'B' })
    })

    it('should not remove item for undefined property', () => {

        const updatedArray =
            removeItemByProperty(testArray, { key: 'test'as any, value: 'B' })

        expect(updatedArray).length(testArray.length)
    })
})

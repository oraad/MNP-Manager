interface Property<T> {
    key: keyof T
    value: unknown
}

export const removeItemByProperty =
    // eslint-disable-next-line @typescript-eslint/ban-types
    <T extends Object>
        (array: T[], property: Property<T>) => {

        const newArray = array.filter((item) => {

            if (item[property.key] == undefined) return true
            const value = item[property.key]
            const itemValue = value instanceof Function ? value() : value
            return itemValue != property.value

        })

        return newArray
    }

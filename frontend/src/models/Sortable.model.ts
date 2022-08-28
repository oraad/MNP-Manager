export class Sortable {
    empty!: boolean
    unsorted!: boolean
    sorted!: boolean

    static Empty() {
        const sortable = new Sortable()
        sortable.empty = true
        sortable.unsorted = true
        sortable.sorted = false
        return sortable
    }
}

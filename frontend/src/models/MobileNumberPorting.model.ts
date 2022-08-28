import { PortingStatus } from './PortingStatus.model'

export class MobileNumberPorting {

    id: number
    mobileNumber: string
    donorOperator: string
    recipientOperator: string
    status: PortingStatus
    createdOn: Date
    updatedOn?: Date

    constructor(
        id: number,
        mobileNumber: string, donorOperator: string, recipientOperator: string,
        status: PortingStatus, createdOn: Date | string, updatedOn?: Date | string
    ) {
        this.id = id
        this.mobileNumber = mobileNumber
        this.donorOperator = donorOperator
        this.recipientOperator = recipientOperator
        this.status = status
        this.createdOn = new Date(createdOn)
        this.updatedOn = updatedOn ? new Date(updatedOn) : undefined
    }

    // getId() {
    //     return this.id
    // }

    // getMobileNumber() {
    //     return this.mobileNumber
    // }

    // getDonorOperator() {
    //     return this.donorOperator
    // }

    // getRecipientOperator() {
    //     return this.recipientOperator
    // }

    // getStatus() {
    //     return this.status
    // }

    // getCreatedOn() {
    //     return this.createdOn
    // }

    // getUpdatedOn() {
    //     return this.updatedOn
    // }
}

import { MobileNumberPorting } from './MobileNumberPorting.model'
import { MobileNumberPortingActions } from './MobileNumberPortingActions.model'
import { PortingStatus } from './PortingStatus.model'

export class MobileNumberPortingWithActions extends MobileNumberPorting {

    actions?: MobileNumberPortingActions

    constructor(
        id: number, mobileNumber: string, donorOperator: string, recipientOperator: string,
        status: PortingStatus, createdOn: Date | string, updatedOn?: Date | string,
        actions?: MobileNumberPortingActions
    ) {
        super(id, mobileNumber, donorOperator, recipientOperator, status, createdOn, updatedOn)
        this.actions = actions
    }

    // getActions() {
    //     return this.actions
    // }

}

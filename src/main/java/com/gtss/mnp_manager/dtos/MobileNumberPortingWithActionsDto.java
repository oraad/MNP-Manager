package com.gtss.mnp_manager.dtos;

import static com.gtss.mnp_manager.models.PortingStatus.PENDING;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobileNumberPortingWithActionsDto extends MobileNumberPortingDto {

    private MobileNumberPortingActions actions;

    /**
     * 
     * @param baseUri
     * @param mobileNumber
     * 
     *        Generate actions uri for accepting or rejecting request
     */
    public void generateActions(String baseUri) {
        if (this.status == PENDING)
            this.actions =
                    new MobileNumberPortingActions(baseUri, this.getMobileNumber());
    }

    @Getter
    @Setter
    class MobileNumberPortingActions {

        private String accept;

        private String reject;

        public MobileNumberPortingActions(String baseUri, String mobileNumber) {
            String uri = baseUri + "/" + mobileNumber;
            this.accept = uri + "/accept";
            this.reject = uri + "/reject";
        }
    }

}

package com.smartcart.storemanagement.infrastructure.acl;

import com.smartcart.storemanagement.domain.model.valueobjects.LegalStatus;
import com.smartcart.storemanagement.domain.model.valueobjects.Ruc;
import com.smartcart.storemanagement.infrastructure.acl.dto.StoreLegalStatusDTO;
import com.smartcart.storemanagement.infrastructure.acl.dto.SunatResponse;
import org.springframework.stereotype.Component;

@Component
public class StoreManagementACL {

    public StoreLegalStatusDTO translateLegalStatus(SunatResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("Sunat response is required");
        }
        var ruc = new Ruc(response.ddpNumruc());
        var status = mapLegalStatus(response.descEstado(), response.descCondicion());
        return new StoreLegalStatusDTO(ruc.getNormalized(), status);
    }

    private LegalStatus mapLegalStatus(String estado, String condicion) {
        if (estado == null || condicion == null) {
            return LegalStatus.PENDING;
        }
        if ("ACTIVO".equalsIgnoreCase(estado) && "HABIDO".equalsIgnoreCase(condicion)) {
            return LegalStatus.VERIFIED;
        }
        return LegalStatus.REJECTED;
    }
}


package com.smartcart.storemanagement.infrastructure.acl.dto;

import com.smartcart.storemanagement.domain.model.valueobjects.LegalStatus;

public record StoreLegalStatusDTO(String ruc, LegalStatus status) {
}


package com.smartcart.verification.interfaces.rest.resources;

public record VerificationApplicationResponse(
        Long applicationId,
        String merchantId,
        String ruc,
        String companyName,
        String status
) {}

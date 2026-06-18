package com.smartcart.verification.domain.model.commands;

public record RegisterVerificationApplicationCommand(String merchantId, String ruc) {
}

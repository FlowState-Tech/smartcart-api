package com.smartcart.verification.domain.services;

import com.smartcart.verification.domain.model.valueobjects.Ruc;
import java.util.Optional;

public interface SunatService {
    /**
     * Consulta el servicio externo de SUNAT para verificar si el RUC está activo.
     * * @param ruc El Value Object Ruc que contiene los 11 dígitos validados.
     * @return Un Optional con la Razón Social (String) si el RUC es válido y está activo,
     * o un Optional vacío si el RUC no existe o está inactivo.
     */
    Optional<String> fetchCompanyNameIfActive(Ruc ruc);
}
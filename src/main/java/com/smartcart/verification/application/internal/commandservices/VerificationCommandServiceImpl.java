package com.smartcart.verification.application.internal.commandservices;

import com.smartcart.verification.domain.model.aggregates.VerificationApplication;
import com.smartcart.verification.domain.model.commands.RegisterVerificationApplicationCommand;
import com.smartcart.verification.domain.model.valueobjects.Ruc;
import com.smartcart.verification.domain.services.SunatService;
import com.smartcart.verification.infrastructure.persistence.jpa.repositories.VerificationApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VerificationCommandServiceImpl {

    private final VerificationApplicationRepository repository;
    private final SunatService sunatService;

    public VerificationCommandServiceImpl(VerificationApplicationRepository repository, SunatService sunatService) {
        this.repository = repository;
        this.sunatService = sunatService;
    }

    @Transactional
    public Optional<VerificationApplication> handle(RegisterVerificationApplicationCommand command) {
        var ruc = new Ruc(command.ruc());

        var application = new VerificationApplication(command.merchantId(), ruc);

        var companyNameOpt = sunatService.fetchCompanyNameIfActive(ruc);

        if (companyNameOpt.isPresent()) {
            application.approve(companyNameOpt.get());
        } else {
            application.reject();
        }

        var savedApplication = repository.save(application);
        return Optional.of(savedApplication);
    }
}
package com.smartcart.storemanagement.application.internal.queryservices;

import com.smartcart.iam.domain.model.aggregates.User;
import com.smartcart.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.smartcart.storemanagement.domain.model.aggregates.Store;
import com.smartcart.storemanagement.domain.model.queries.GetMerchantProfileQuery;
import com.smartcart.storemanagement.domain.services.MerchantProfileQueryService;
import com.smartcart.storemanagement.infrastructure.persistence.jpa.repositories.StoreRepository;
import com.smartcart.storemanagement.interfaces.rest.resources.MerchantProfileResponse;
import com.smartcart.storemanagement.interfaces.rest.resources.MerchantStoreResponse;
import com.smartcart.verification.domain.model.aggregates.VerificationApplication;
import com.smartcart.verification.domain.model.valueobjects.VerificationStatus;
import com.smartcart.verification.infrastructure.persistence.jpa.repositories.VerificationApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MerchantProfileQueryServiceImpl implements MerchantProfileQueryService {

    private final UserRepository userRepository;
    private final VerificationApplicationRepository verificationApplicationRepository;
    private final StoreRepository storeRepository;

    public MerchantProfileQueryServiceImpl(UserRepository userRepository,
                                            VerificationApplicationRepository verificationApplicationRepository,
                                            StoreRepository storeRepository) {
        this.userRepository = userRepository;
        this.verificationApplicationRepository = verificationApplicationRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    public Optional<MerchantProfileResponse> handle(GetMerchantProfileQuery query) {
        Optional<User> userOpt = userRepository.findByUsername(query.username());
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();
        String merchantId = user.getId().toString();

        List<VerificationApplication> verifications = verificationApplicationRepository.findByMerchantId(merchantId);

        VerificationApplication verificationOpt = verifications.stream()
                .filter(app -> app.getStatus() == VerificationStatus.VERIFIED)
                .findFirst()
                .orElse(verifications.isEmpty() ? null : verifications.getFirst());

        boolean isVerified = verificationOpt != null && verificationOpt.getStatus() == VerificationStatus.VERIFIED;

        Long applicationId = null;
        String companyName = null;
        String ruc = null;

        if (verificationOpt != null) {
            applicationId = verificationOpt.getId();
            companyName = verificationOpt.getCompanyName();
            ruc = verificationOpt.getRuc() != null ? verificationOpt.getRuc().getNormalized() : null;
        }

        List<MerchantStoreResponse> stores = new ArrayList<>(
                storeRepository.findByMerchantMerchantId(merchantId).stream()
                        .map(this::toStoreResponse)
                        .toList()
        );

        return Optional.of(new MerchantProfileResponse(
                user.getId(),
                user.getUsername(),
                isVerified,
                applicationId,
                companyName,
                ruc,
                Collections.unmodifiableList(stores)
        ));
    }

    private MerchantStoreResponse toStoreResponse(Store store) {
        return new MerchantStoreResponse(store.getId(), store.getName());
    }
}

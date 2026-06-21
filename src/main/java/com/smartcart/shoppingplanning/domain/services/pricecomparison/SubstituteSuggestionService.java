package com.smartcart.shoppingplanning.domain.services.pricecomparison;

import com.smartcart.shoppingplanning.domain.model.valueobjects.SubstituteSuggestion;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Política de Sugerencia de Sustitutos (reporte 2.6.5 — SubstituteSuggestionService).
 */
@Service
public class SubstituteSuggestionService {

    private final SubstituteSuggestionPolicy policy;

    public SubstituteSuggestionService(SubstituteSuggestionPolicy policy) {
        this.policy = policy;
    }

    public Optional<SubstituteSuggestion> suggestAlternative(Long storeId, String originalSku) {
        return policy.suggest(storeId, originalSku);
    }
}

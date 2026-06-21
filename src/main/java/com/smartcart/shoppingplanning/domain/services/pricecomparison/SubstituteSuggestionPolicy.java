package com.smartcart.shoppingplanning.domain.services.pricecomparison;

import com.smartcart.shoppingplanning.domain.model.valueobjects.SubstituteSuggestion;
import com.smartcart.shoppingplanning.infrastructure.acl.StoreCatalogACL;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Política de Sugerencia de Sustitutos (Event Storming — PriceComparison).
 */
@Component
public class SubstituteSuggestionPolicy {

    private static final String REASON =
            "Política de sustitutos: misma categoría → misma marca → menor precio";

    private final StoreCatalogACL catalogACL;

    public SubstituteSuggestionPolicy(StoreCatalogACL catalogACL) {
        this.catalogACL = catalogACL;
    }

    public Optional<SubstituteSuggestion> suggest(Long storeId, String originalSku) {
        if (catalogACL.isInStock(storeId, originalSku)) {
            return Optional.empty();
        }
        return catalogACL.findBestSubstitute(storeId, originalSku)
                .map(candidate -> new SubstituteSuggestion(
                        originalSku, candidate.sku(), candidate.name(), storeId, REASON));
    }
}

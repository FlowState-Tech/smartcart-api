package com.smartcart.shoppingplanning.infrastructure.acl;

import com.smartcart.storemanagement.domain.model.valueobjects.Sku;
import com.smartcart.shoppingplanning.domain.model.valueobjects.BarcodeLookupResult;
import com.smartcart.storemanagement.infrastructure.persistence.jpa.repositories.InventoryRepository;
import com.smartcart.storemanagement.infrastructure.persistence.jpa.repositories.StoreRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Optional;

/**
 * ACL — Customer/Supplier: Store Management (U) → Shopping Planning (D).
 * Traduce catálogo, inventario y precios al lenguaje de planificación de compras.
 * Reporte 2.5.2 y 2.6.5.4.
 */
@Component
public class StoreCatalogACL {

    private final InventoryRepository inventoryRepository;
    private final StoreRepository storeRepository;

    public StoreCatalogACL(InventoryRepository inventoryRepository, StoreRepository storeRepository) {
        this.inventoryRepository = inventoryRepository;
        this.storeRepository = storeRepository;
    }

    public Optional<BigDecimal> findPrice(Long storeId, String sku) {
        return inventoryRepository.findByStoreId(storeId)
                .flatMap(inv -> inv.findPriceItem(new Sku(sku))
                        .map(p -> p.getAmount().getAmount()));
    }

    public boolean isInStock(Long storeId, String sku) {
        return inventoryRepository.findByStoreId(storeId)
                .flatMap(inv -> inv.findStockPoint(new Sku(sku)))
                .map(s -> s.getQuantity() > 0)
                .orElse(false);
    }

    /**
     * Política de sustitutos: misma categoría → misma marca → menor precio.
     */
    public Optional<SubstituteCandidate> findBestSubstitute(Long storeId, String excludeSku) {
        var inventoryOpt = inventoryRepository.findByStoreId(storeId);
        if (inventoryOpt.isEmpty()) return Optional.empty();
        var inventory = inventoryOpt.get();
        var original = inventory.findProduct(new Sku(excludeSku)).orElse(null);
        Long categoryId = original != null ? original.getCategoryId() : null;
        String brand = original != null ? original.getBrand() : null;

        return inventory.getProducts().stream()
                .filter(p -> !p.getSku().getCode().equals(excludeSku))
                .filter(p -> isInStock(storeId, p.getSku().getCode()))
                .map(p -> {
                    int score = 0;
                    if (categoryId != null && categoryId.equals(p.getCategoryId())) score += 2;
                    if (brand != null && brand.equalsIgnoreCase(p.getBrand())) score += 1;
                    var price = findPrice(storeId, p.getSku().getCode()).orElse(BigDecimal.valueOf(Double.MAX_VALUE));
                    return new SubstituteCandidate(p.getSku().getCode(), p.getName(), score, price);
                })
                .sorted(Comparator.comparingInt(SubstituteCandidate::score).reversed()
                        .thenComparing(SubstituteCandidate::price))
                .findFirst();
    }

    public String storeName(Long storeId) {
        return storeRepository.findById(storeId).map(s -> s.getName()).orElse("Store " + storeId);
    }

    public java.util.List<BarcodeLookupResult> lookupBarcode(String barcode) {
        var results = new java.util.ArrayList<BarcodeLookupResult>();
        for (var store : storeRepository.findAll()) {
            var price = findPrice(store.getId(), barcode);
            price.ifPresent(p -> results.add(new BarcodeLookupResult(
                    barcode, catalogName(store.getId(), barcode), store.getId(), store.getName(), p)));
        }
        return results;
    }

    private String catalogName(Long storeId, String sku) {
        return inventoryRepository.findByStoreId(storeId)
                .flatMap(inv -> inv.findProduct(new Sku(sku)).map(p -> p.getName()))
                .orElse("Producto " + sku);
    }

    public record SubstituteCandidate(String sku, String name, int score, BigDecimal price) {}
}

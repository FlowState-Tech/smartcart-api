package com.smartcart.shoppingplanning.domain.services;

import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingList;
import com.smartcart.shoppingplanning.domain.model.entities.ShoppingListItem;
import com.smartcart.shoppingplanning.domain.model.valueobjects.Budget;
import com.smartcart.shoppingplanning.infrastructure.acl.StoreCatalogACL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComparisonEngineServiceTest {

    @Mock
    private StoreCatalogACL catalogACL;

    @InjectMocks
    private ComparisonEngineService comparisonEngine;

    @Test
    void flagsStoreExceedingBudget() {
        var list = new ShoppingList(1L, "Test");
        list.addItem(new ShoppingListItem("SKU1", "Product", BigDecimal.ONE, "und"));
        when(catalogACL.findPrice(eq(10L), eq("SKU1"))).thenReturn(Optional.of(new BigDecimal("100.00")));
        when(catalogACL.storeName(anyLong())).thenReturn("Store");

        var results = comparisonEngine.compare(list, List.of(10L), new Budget(new BigDecimal("50.00"), "PEN"));
        assertFalse(results.getFirst().withinBudget());
    }

    @Test
    void marksWithinBudgetWhenBelowLimit() {
        var list = new ShoppingList(1L, "Test");
        list.addItem(new ShoppingListItem("SKU1", "Product", BigDecimal.ONE, "und"));
        when(catalogACL.findPrice(eq(10L), eq("SKU1"))).thenReturn(Optional.of(new BigDecimal("30.00")));
        when(catalogACL.storeName(anyLong())).thenReturn("Store");

        var results = comparisonEngine.compare(list, List.of(10L), new Budget(new BigDecimal("50.00"), "PEN"));
        assertTrue(results.getFirst().withinBudget());
    }
}

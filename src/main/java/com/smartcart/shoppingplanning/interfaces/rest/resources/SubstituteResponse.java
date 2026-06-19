package com.smartcart.shoppingplanning.interfaces.rest.resources;
public record SubstituteResponse(String originalSku, String substituteSku, String substituteName,
                                 Long storeId, String reason) {}

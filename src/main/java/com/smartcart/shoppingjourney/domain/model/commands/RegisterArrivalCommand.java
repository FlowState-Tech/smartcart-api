package com.smartcart.shoppingjourney.domain.model.commands;

public record RegisterArrivalCommand(String routeId, double latitude, double longitude) {}

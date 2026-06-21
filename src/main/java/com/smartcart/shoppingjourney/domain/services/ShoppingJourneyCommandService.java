package com.smartcart.shoppingjourney.domain.services;

import com.smartcart.shoppingjourney.domain.model.aggregates.ShoppingRoute;
import com.smartcart.shoppingjourney.domain.model.commands.CreateRouteCommand;
import com.smartcart.shoppingjourney.domain.model.commands.DefineResidenceCommand;
import com.smartcart.shoppingjourney.domain.model.commands.FinishJourneyCommand;
import com.smartcart.shoppingjourney.domain.model.commands.OptimizeRouteCommand;
import com.smartcart.shoppingjourney.domain.model.commands.RegisterArrivalCommand;
import com.smartcart.shoppingjourney.domain.model.commands.RequestPathCommand;
import com.smartcart.shoppingjourney.domain.model.commands.SelectDestinationCommand;
import com.smartcart.shoppingjourney.domain.model.commands.StartNavigationCommand;

public interface ShoppingJourneyCommandService {
    ShoppingRoute handle(CreateRouteCommand command);
    ShoppingRoute handle(DefineResidenceCommand command);
    ShoppingRoute handle(SelectDestinationCommand command);
    ShoppingRoute handle(OptimizeRouteCommand command);
    ShoppingRoute handle(RequestPathCommand command);
    ShoppingRoute handle(StartNavigationCommand command);
    ShoppingRoute handle(RegisterArrivalCommand command);
    ShoppingRoute handle(FinishJourneyCommand command);
}

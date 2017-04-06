package net.vatri.inventory.controllers;

import net.vatri.inventory.App;
import net.vatri.inventory.services.InventoryService;
import net.vatri.inventory.services.InventoryServiceHibernate;

public class BaseController{
    InventoryService inventoryService = new InventoryServiceHibernate(App.getInstance().getSessionFactory());
}
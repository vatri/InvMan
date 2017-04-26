package net.vatri.inventory.libs;

import net.vatri.inventory.App;
import net.vatri.inventory.services.InventoryService;
import net.vatri.inventory.services.InventoryServiceHibernate;

public abstract class BaseController {
    public InventoryService inventoryService = new InventoryServiceHibernate(App.getInstance().getSessionFactory());
}
package com.b07.inventory;

import java.io.Serializable;
import java.util.HashMap;

public class InventoryImpl implements Inventory, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6003332276034489623L;
  private HashMap<Item, Integer> itemMap;
  private transient int totalItems;

  @Override
  public HashMap<Item, Integer> getItemMap() {
    // TODO Auto-generated method stub
    return this.itemMap;
  }

  @Override
  public void setItemMap(HashMap<Item, Integer> itemMap) {
    // TODO Auto-generated method stub
    this.itemMap = itemMap;
  }

  @Override
  public void updateMap(Item item, Integer value) {
    // TODO Auto-generated method stub
    this.itemMap.put(item, value);
  }

  @Override
  public int getTotalItems() {
    // TODO Auto-generated method stub
    return this.totalItems;
  }

  @Override
  public void setTotalItems(int total) {
    // TODO Auto-generated method stub
    this.totalItems = total;
  }

}

package com.b07.inventory;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemImpl implements Item, Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -6663699000960732305L;
  private int itemId;
  private String itemName;
  private BigDecimal itemPrice;

  @Override
  public int getId() {
    // TODO Auto-generated method stub
    return this.itemId;
  }

  @Override
  public void setId(int id) {
    // TODO Auto-generated method stub
    this.itemId = id;
  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return this.itemName;
  }

  @Override
  public void setName(String name) {
    // TODO Auto-generated method stub
    this.itemName = name;
  }

  @Override
  public BigDecimal getPrice() {
    // TODO Auto-generated method stub
    return this.itemPrice;
  }

  @Override
  public void setPrice(BigDecimal price) {
    // TODO Auto-generated method stub
    this.itemPrice = price;
  }

  @Override
  public boolean equals(Object item) {
    // TODO Auto-generated method stub
    if (!item.getClass().isInstance(this)) {
      return false;
    }
    Item isItem = (Item) item;
    return isItem.getId() == this.itemId;
  }

}

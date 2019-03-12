package com.b07.store;

import com.b07.inventory.Item;
import com.b07.users.User;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

public class SaleImpl implements Sale, Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = -3234075891509588228L;
  private transient int id;
  private User userOfSale;
  private BigDecimal totalPrice;
  private HashMap<Item, Integer> items;
  
  @Override
  public int getId() {
    // TODO Auto-generated method stub
    return this.id;
  }

  @Override
  public void setId(int id) {
    // TODO Auto-generated method stub
    this.id = id;
  }

  @Override
  public User getUser() {
    // TODO Auto-generated method stub
    return this.userOfSale;
  }

  @Override
  public void setUser(User user) {
    // TODO Auto-generated method stub
    this.userOfSale = user;
  }

  @Override
  public BigDecimal getTotalPrice() {
    // TODO Auto-generated method stub
    return this.totalPrice;
  }

  @Override
  public void setTotalPrice(BigDecimal price) {
    // TODO Auto-generated method stub
    this.totalPrice = price;
  }

  @Override
  public HashMap<Item, Integer> getItemMap() {
    // TODO Auto-generated method stub
    return this.items;
  }

  @Override
  public void setItemMap(HashMap<Item, Integer> itemMap) {
    // TODO Auto-generated method stub
    this.items = itemMap;

  }
}

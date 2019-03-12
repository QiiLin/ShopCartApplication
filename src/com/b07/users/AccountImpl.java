package com.b07.users;

import java.io.Serializable;
import com.b07.store.ShoppingCart;

public class AccountImpl implements Account, Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -1130994316205425292L;
  private int id;
  private User user;
  private int userId;
  private ShoppingCart shoppingCart;
  private boolean active;
  private boolean approve;

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public void setId(int id) {
    this.id = id;

  }

  @Override
  public int getUserId() {
    return this.userId;
  }

  @Override
  public void setUserId(int id) {
    this.userId = id;

  }

  @Override
  public User getUser() {
    return this.user;
  }

  @Override
  public void setUser(User user) {
    this.user = user;

  }

  @Override
  public ShoppingCart getShoppingCart() {
    return this.shoppingCart;
  }

  @Override
  public void setShoppingCart(ShoppingCart shoppingCart) {
    this.shoppingCart = shoppingCart;
  }

  @Override
  public void setActive(boolean active) {
    // TODO Auto-generated method stub
    this.active = active;
  }

  @Override
  public boolean getActive() {
    // TODO Auto-generated method stub
    return active;
  }

  @Override
  public void setApprove(boolean approve) {
    // TODO Auto-generated method stub
    this.approve = approve;
  }

  @Override
  public boolean getApprove() {
    // TODO Auto-generated method stub
    return this.approve;
  }

}

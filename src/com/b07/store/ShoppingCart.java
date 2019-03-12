package com.b07.store;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidItemException;
import com.b07.exceptions.InvalidTotalPriceException;
import com.b07.inventory.Item;
import com.b07.users.Account;
import com.b07.users.Customer;

public class ShoppingCart implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = -3333881851467856011L;
  private HashMap<Item, Integer> items = new HashMap<Item, Integer>();
  private transient Customer customer;
  private transient BigDecimal total = new BigDecimal(0);
  private transient final BigDecimal TAXRATE = new BigDecimal(1.13);

  /**
   * Create a instance of ShoppingCartobjec with a input customer.
   * @param customer inpputed customer
   * @throws InvalidCustomerException throw if customer are invalid
   */
  public ShoppingCart(Customer customer) {
    this.customer = customer;
  }
  
  public ShoppingCart() {
    
  }
  /**
   * Method add the quantity of item to the items list. Update the total accordingly.
   * @param item the item need to be add
   * @param quantity the quantity of item that need to be add
   * @throws InvalidItemException throw if item are invalid
   * @throws InvalidItemQuantityException throw if quantity are negative
   */
  public void addItem(Item item, int quantity)
      throws InvalidIdException, InvalidItemException {
    if (quantity > 0) {
      if (item != null) {
        if (this.items.containsKey(item)) {
          int currentQuantity = this.items.get(item);
          int newQuantity = currentQuantity + quantity;
          this.items.put(item, newQuantity);
        } else {
          this.items.put(item, quantity);
        }
        BigDecimal quantityOfItem = new BigDecimal(quantity);
        BigDecimal totalPrice = item.getPrice().multiply(quantityOfItem);
        this.total = total.add(totalPrice);
      } else {
        throw new InvalidIdException();
      }
    } else {
      throw new InvalidItemException();
    }
  }
  
  public int getItemQuantity(Item item) {
    return this.items.get(item);
  }

  /**
   * this Remove the quantity given of the item from items. If the number 
   * becomes zero, remove itentirely from the items list. Update the totalaccordingly
   * @param item item that need to be remove
   * @param quantity quanitu that need to be add
   * @throws InvalidItemQuantityException throw if needed
   * @throws InsufficientCartItemQuantityException throw if needed
   * @throws InvalidItemException throw if needed
   */
  public void removeItem(Item item, int quantity) throws InvalidItemException, InvalidIdException {
    if (quantity > 0) {
      if (item != null && this.items.containsKey(item)) {
        int currentQuantity = this.items.get(item);
        if (currentQuantity >= quantity) {
          if (currentQuantity == quantity) {
            this.items.remove(item);
          } else {
            int newQuantity = currentQuantity - quantity;
            this.items.put(item, newQuantity);
          }
          BigDecimal quantityOfItem = new BigDecimal(quantity);
          BigDecimal totalPrice = item.getPrice().multiply(quantityOfItem);
          this.total = total.add(totalPrice);
        } else {
          throw new InvalidItemException();
        }
      } else {
        throw new InvalidItemException();
      }
    } else {
      throw new InvalidItemException();
    }
  }

  public List<Item> getItems() {
    List<Item> itemList = new ArrayList<Item>(this.items.keySet());
    return itemList;
  }

  public Customer getCustomer() {
    return this.customer;
  }
  
  /**
   * Method to get total of cart.
   * @return int of total value of cart
   */
  public BigDecimal getTotal() {
    this.total = new BigDecimal(0);
    List<Item> allItem = this.getItems();
    for (Item item : allItem) {
      BigDecimal itemPrice = item.getPrice();
      BigDecimal itemQuantity = new BigDecimal(this.items.get(item));
      BigDecimal currentItemTotalPrice = itemPrice.multiply(itemQuantity);
      this.total = total.add(currentItemTotalPrice);
    }
    return this.total;
  }

  public BigDecimal getTaxRate() {
    return this.TAXRATE;
  }

  /**
   * this method check the Take in a shopping cart, validate it has an
   * associated customer. If it does, calculate the
   * total after tax, and submit the purchase to the
   * database. If there are enough of each requested
   * item in inventory, update the required tables,
   * and clear the shopping cart out, returning true.
   * Return false if the operation fails, or if there are
   * insufficient items available.
   * @param shoppingCart a fulled cart object
   * @return true if it checked out false, otherwise
   */
  public boolean checkOut(ShoppingCart shoppingCart) {
    boolean checkedOut = true;
    if (shoppingCart.customer != null && shoppingCart.customer instanceof Customer) {
      DatabaseInsertHelper dataInsertHelper = new DatabaseInsertHelper();
      int cartCustomer = shoppingCart.getCustomer().getId();
      BigDecimal cartPrice = shoppingCart.getTotal();
      HashMap<Item, Integer> currentCartItems = new HashMap<Item, Integer>();
      boolean enoughItem = true;
      List<Item> allItem = shoppingCart.getItems();  
      for (Item currentItem : allItem) {
        Integer currentItemQuantity = 0;
        boolean itemsEmpty = false;
        while (!itemsEmpty) {
          try {
            shoppingCart.removeItem(currentItem, 1);
          } catch (InvalidIdException | InvalidItemException e) {
            // TODO Auto-generated catch block
            itemsEmpty = true;
          }
          currentItemQuantity = currentItemQuantity + 1;
        }
        currentCartItems.put(currentItem, currentItemQuantity - 1);
      }
      try {
        HashMap<Item, Integer> inventoryItems = DatabaseSelectHelper.getInventory().getItemMap();
        Set<Item> itemSet = inventoryItems.keySet();
        int numberOfItems = allItem.size();
        int counter = 0;
        while (counter < numberOfItems) {
          Item item = allItem.get(counter);
          Item inventoryItemKey = null;
          for (Item inventoryItem : itemSet) {
            if (inventoryItem.getId() == item.getId()) {
              inventoryItemKey = inventoryItem;
            }
          }
          if (inventoryItemKey != null
              && (currentCartItems.get(item) > inventoryItems.get(inventoryItemKey))) {
            enoughItem = false;
            counter = numberOfItems;
            for (Item items : allItem) {
              Integer currentItemQuantity = currentCartItems.get(items);
              shoppingCart.addItem(items, currentItemQuantity);
            }
          }
          counter = counter + 1;
        }
        if (enoughItem) {
          int saleId = dataInsertHelper.insertSale(cartCustomer, cartPrice);
          for (Item cartItem : allItem) {
            Item inventoryItemKey = null;
            for (Item inventoryItem : itemSet) {
              if (inventoryItem.getId() == cartItem.getId()) {
                inventoryItemKey = inventoryItem;
              }
            }
            int cartItemId = cartItem.getId();
            int cartItemQuantity = currentCartItems.get(cartItem);
            if (inventoryItemKey != null) {
              int currentItemQuantity = inventoryItems.get(inventoryItemKey) - cartItemQuantity;
              dataInsertHelper.insertItemizedSale(saleId, cartItemId, cartItemQuantity);
              DatabaseUpdateHelper.updateInventoryQuantity(currentItemQuantity, cartItemId);
            } else {
              checkedOut = false;
            }
          }
        }
      } catch (DatabaseInsertException | SQLException | 
          InvalidIdException 
          | InvalidItemException | InvalidTotalPriceException e) {
        // TODO Auto-generated catch block
        checkedOut = false;
      }
    }
    return checkedOut;
  }

  public void clearCart() {
    this.items.clear();;
    this.total = new BigDecimal(0);
  }
  
  public HashMap<Item, Integer> getInformation() {
    return this.items;
  }
}

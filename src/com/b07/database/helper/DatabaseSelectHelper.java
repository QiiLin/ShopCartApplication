package com.b07.database.helper;

import com.b07.database.DatabaseSelector;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidItemException;
import com.b07.inventory.Inventory;
import com.b07.inventory.InventoryImpl;
import com.b07.inventory.Item;
import com.b07.inventory.ItemImpl;
import com.b07.store.Sale;
import com.b07.store.SaleImpl;
import com.b07.store.SalesLog;
import com.b07.store.SalesLogImpl;
import com.b07.store.ShoppingCart;
import com.b07.users.Account;
import com.b07.users.AccountImpl;
import com.b07.users.Admin;
import com.b07.users.Customer;
import com.b07.users.Employee;
import com.b07.users.Roles;
import com.b07.users.User;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * TODO: Complete the below methods to be able to get information out of the database. TODO: The
 * given code is there to aide you in building your methods. You don't have TODO: to keep the exact
 * code that is given (for example, DELETE the System.out.println())
 */
public class DatabaseSelectHelper extends DatabaseSelector {

  /**
   * This method is to get all the roleId in the database.
   * 
   * @return ids a list of integer where each is a role id in database
   * @throws SQLException if SQL fail to read
   */
  public static List<Integer> getRoleIds() throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getRoles(connection);
    List<Integer> ids = new ArrayList<>();
    while (results.next()) {
      ids.add(results.getInt("ID"));
    }
    results.close();
    connection.close();
    return ids;
  }


  /**
   * This method is to validate the give roleId is in database.
   * 
   * @param roleId the int represents the id for its role
   * @return true if the roleId exists in database, false otherwise
   * @throws SQLException if SQL fails to read
   */
  public static boolean roleIdCheck(int roleId) throws SQLException {
    List<Integer> ids = getRoleIds();
    return ids.contains(roleId);
  }


  /**
   * The method gets the role name of the role id.
   * 
   * @param roleId the id we need to check if it is in the database
   * @return String the value of roleId
   * @throws SQLException if SQL failed to read
   * @throws InvalidIdException throw if the role id is not in database
   */
  public static String getRoleName(int roleId) throws SQLException, InvalidIdException {
    if (roleIdCheck(roleId)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      String role = DatabaseSelector.getRole(roleId, connection);
      connection.close();
      return role;
    } else {
      throw new InvalidIdException();
    }
  }

  /**
   * This method is to get the rolId related to userId.
   * 
   * @param userId the id we need to check if it is in database
   * @return int value to represent the roleId
   * @throws SQLException throws if SQL failed to read
   * @throws InvalidIdException throws if userId is not in database
   */
  public static int getUserRoleId(int userId) throws SQLException, InvalidIdException {
    if (isValidUserId(userId)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      int roleId = DatabaseSelector.getUserRole(userId, connection);
      connection.close();
      return roleId;
    } else {
      throw new InvalidIdException();
    }
  }

  /**
   * This method is to get all the users of the input role id.
   * 
   * @param roleId the role id we need to check if it is in database
   * @return List of Integer to represent the UserId relate to roleId
   * @throws SQLException throw if the SQL fail to work
   * @throws InvalidRoleIdException throw if the roleId is not in database.
   */
  public static List<Integer> getUsersByRole(int roleId) throws SQLException, InvalidIdException {
    if (roleIdCheck(roleId)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      ResultSet results = DatabaseSelector.getUsersByRole(roleId, connection);
      List<Integer> userIds = new ArrayList<>();
      while (results.next()) {
        userIds.add(results.getInt("USERID"));
      }
      results.close();
      connection.close();
      return userIds;
    } else {
      throw new InvalidIdException();
    }
  }

  /**
   * This method is to get user object for the userId.
   * 
   * @param userId check if it is in database
   * @return User if this operation complete
   * @throws SQLException if SQL fail to read
   * @throws InvalidIdException throw if the userId is not in database
   */
  public static List<User> getUsersDetails() throws SQLException, InvalidIdException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getUsersDetails(connection);
    List<User> users = new ArrayList<>();
    while (results.next()) {
      int userId = results.getInt("ID");
      User currentUser = getUserDetails(userId);
      users.add(currentUser);
    }
    results.close();
    connection.close();
    return users;
  }


  /**
   * This method is to validate the given user id, it is valid when it exists in database.
   * 
   * @param userId the int represents the id of its user
   * @return true if it exists in database, false otherwise
   */
  public static boolean isValidUserId(int userId) {
    try {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      ResultSet results = DatabaseSelector.getUsersDetails(connection);
      List<Integer> users = new ArrayList<>();
      while (results.next()) {
        int currentuserId = results.getInt("ID");
        users.add(currentuserId);
      }
      results.close();
      connection.close();
      for (int id : users) {
        if (id == userId) {
          return true;
        }
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      return false;
    }
    return false;
  }

  /**
   * This method is to get user object for the userId.
   * 
   * @param userId check if it is in database
   * @return User if this operation complete
   * @throws SQLException if SQL fail to read
   * @throws InvalidIdException throw if the userId is not in database
   */
  public static User getUserDetails(int userId) throws SQLException, InvalidIdException {
    if (isValidUserId(userId)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      ResultSet results = DatabaseSelector.getUserDetails(userId, connection);
      int userRoleId = getUserRoleId(userId);
      String roleString = getRoleName(userRoleId);
      int userAge = results.getInt("AGE");
      String userName = results.getString("NAME");
      String userAddress = results.getString("ADDRESS");
      User currentUser;
      if (roleString.equals(Roles.ADMIN.name())) {
        currentUser = new Admin(userId, userName, userAge, userAddress);
      } else if (roleString.equals(Roles.EMPLOYEE.name())) {
        currentUser = new Employee(userId, userName, userAge, userAddress);
      } else if (roleString.equals(Roles.CUSTOMER.name())) {
        currentUser = new Customer(userId, userName, userAge, userAddress);
      } else {
        currentUser = null;
      }
      results.close();
      connection.close();
      return currentUser;
    } else {
      throw new InvalidIdException();
    }
  }

  /**
   * This method is to get the password for the given userId.
   * 
   * @param userId the int we need to check in database
   * @return String if we get password out of database
   * @throws SQLException throws if SQL fails to read
   * @throws InvalidIdException throw if the userId is not in database
   */
  public static String getPassword(int userId) throws SQLException, InvalidIdException {
    if (isValidUserId(userId)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      String password = DatabaseSelector.getPassword(userId, connection);
      connection.close();
      return password;
    } else {
      throw new InvalidIdException();
    }

  }

  /**
   * This method is to get all item from database.
   * 
   * @return List of all Item in the database
   * @throws SQLException if SQL failed to read
   * @throws InvalidIdException if the itemId is not in database
   */
  public static List<Item> getAllItems() throws SQLException, InvalidIdException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getAllItems(connection);
    List<Item> items = new ArrayList<>();
    while (results.next()) {
      int itemId = results.getInt("ID");
      Item currentItem = getItem(itemId);
      items.add(currentItem);
    }
    results.close();
    connection.close();
    return items;
  }

  /**
   * This method is to validate the given itemId is in database
   * 
   * @param itemId the int represents the id of its correspond item
   * @return true if it exists in database , false otherwise
   * @throws SQLException if SQL fails to read
   * @throws InvalidIdException if the itemId is not valid(exist in database)
   */
  public static boolean itemIdCheck(int itemId) throws SQLException, InvalidIdException {
    try {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      ResultSet results = DatabaseSelector.getAllItems(connection);
      List<Integer> items = new ArrayList<Integer>();
      while (results.next()) {
        int id = results.getInt("ID");
        items.add(id);
      }
      results.close();
      connection.close();
      for (Integer item : items) {
        if (item == itemId) {
          return true;
        }
      }
      return false;
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      return false;
    }
  }

  /**
   * This method is to get Item from its id.
   * 
   * @param itemId the int we need to check if it is in database
   * @return Item if we found the Item in the database
   * @throws SQLException if SQL failed to read
   * @throws InvalidIdException if itemIde is wrong
   */
  public static Item getItem(int itemId) throws SQLException, InvalidIdException {
    if (itemIdCheck(itemId)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      ResultSet results = DatabaseSelector.getItem(itemId, connection);
      Item currentItem = new ItemImpl();
      String itemName = results.getString("NAME");
      BigDecimal itemPrice = results.getBigDecimal("PRICE");
      currentItem.setId(itemId);
      currentItem.setName(itemName);
      currentItem.setPrice(itemPrice);
      results.close();
      connection.close();
      return currentItem;
    } else {
      throw new InvalidIdException();
    }
  }

  /**
   * This method is to get inventory of the database.
   * 
   * @return Inventory with data from database
   * @throws SQLException if SQL failed to read
   * @throws InvalidIdException throw if itemId is not in database
   */
  public static Inventory getInventory() throws SQLException, InvalidIdException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getInventory(connection);
    Inventory storeInventory = new InventoryImpl();
    HashMap<Item, Integer> itemMap = new HashMap<Item, Integer>();
    storeInventory.setItemMap(itemMap);
    storeInventory.setTotalItems(0);
    int totalQuanity = 0;
    while (results.next()) {
      int itemId = results.getInt("ITEMID");
      int currentItemQuanity = getInventoryQuantity(itemId);
      Item currentItem = getItem(itemId);
      storeInventory.updateMap(currentItem, (Integer) currentItemQuanity);
      totalQuanity = totalQuanity + currentItemQuanity;
    }
    storeInventory.setTotalItems(totalQuanity);
    results.close();
    connection.close();
    return storeInventory;
  }

  /**
   * This method is to get the quantity of given itemId.
   * 
   * @param itemId the int we need to check if it is in database
   * @return int value represents the quantity of the itemId
   * @throws SQLException throw if the SQL failed
   * @throws InvalidIdException throw if the itemId is not in database
   */
  public static int getInventoryQuantity(int itemId) throws SQLException, InvalidIdException {
    if (itemIdCheck(itemId)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      int quantity = DatabaseSelector.getInventoryQuantity(itemId, connection);
      connection.close();
      return quantity;
    } else {
      throw new InvalidIdException();
    }

  }

  /**
   * This method is to get a sale log with all the sale for database.
   * 
   * @return SaleLog contain all the sale info form database
   * @throws InvalidIdException throw if the saleLog is not in database
   * @throws SQLException throw if the saleId is not in database
   */
  public static SalesLog getSales() throws InvalidIdException, SQLException {
    SalesLog salesLog = new SalesLogImpl();
    getItemizedSales(salesLog);
    return salesLog;
  }

  /**
   * This method is to get Sale by the given saleId.
   * 
   * @param saleId the id we need to check.
   * @return sale object with info from sale id
   * @throws SQLException throw if the saleId is not in database
   * @throws InvalidIdException if the saleId is not in database
   */
  public static Sale getSaleById(int saleId)
      throws SQLException, InvalidIdException, InvalidIdException {
    if (DatabaseInsertHelper.salesIdCheck(saleId)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      ResultSet results = DatabaseSelector.getSaleById(saleId, connection);
      Sale currentSale = new SaleImpl();
      int saleUserId = results.getInt("USERID");
      User saleUser = getUserDetails(saleUserId);
      currentSale.setUser(saleUser);
      getItemizedSaleById(saleId, currentSale);
      results.close();
      connection.close();
      return currentSale;
    } else {
      throw new InvalidIdException();
    }
  }

  /**
   * This method is to get all the sale of this userId.
   * 
   * @param userId we need to check it
   * @return list of all the sale object
   * @throws SQLException throw if the saleId is not in database
   * @throws InvalidIdException throw if the userId is not in database
   */
  public static List<Sale> getSalesToUser(int userId) throws SQLException, InvalidIdException {
    if (isValidUserId(userId)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      ResultSet results = DatabaseSelectHelper.getSalesToUser(userId, connection);
      List<Sale> sales = new ArrayList<>();
      while (results.next()) {
        int saleId = results.getInt("ID");
        Sale currentSale = getSaleById(saleId);
        sales.add(currentSale);
      }
      results.close();
      connection.close();
      return sales;
    } else {
      throw new InvalidIdException();
    }
  }

  /**
   * This method is to mutate the saleId and sale object.
   * 
   * @param saleId check it in database
   * @param sale check it is right one sale
   * @throws SQLException if the saleId is not in database
   * @throws InvalidIdException if the saleId is not in database
   */
  public static void getItemizedSaleById(int saleId, Sale sale)
      throws SQLException, InvalidIdException {
    if (DatabaseInsertHelper.salesIdCheck(saleId)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      ResultSet results = DatabaseSelector.getItemizedSaleById(saleId, connection);
      HashMap<Item, Integer> items = new HashMap<Item, Integer>();
      BigDecimal totalPrice = new BigDecimal(0);
      while (results.next()) {
        int itemId = results.getInt("ITEMID");
        Item currentItem = getItem(itemId);
        int saleQuantity = results.getInt("QUANTITY");
        items.put(currentItem, saleQuantity);
        BigDecimal currentQuantity = new BigDecimal(saleQuantity);
        BigDecimal itemPrice = currentItem.getPrice();
        BigDecimal currentTotalPrice = currentQuantity.multiply(itemPrice);
        totalPrice = totalPrice.add(currentTotalPrice);
      }
      sale.setId(saleId);
      sale.setItemMap(items);
      sale.setTotalPrice(totalPrice);
      results.close();
      connection.close();
    } else {
      throw new InvalidIdException();
    }
  }

  /**
   * This method is to mutate the saleLog object's in inner field.
   * 
   * @param salesLog CHeck if it is saleLog Object
   * @throws SQLException throw if the saleId is not in database
   * @throws InvalidIdException throw if the saleId is not in database
   */
  public static void getItemizedSales(SalesLog salesLog) throws SQLException, InvalidIdException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getItemizedSales(connection);
    int numberOfsale = 0;
    BigDecimal totalSale = new BigDecimal(0);
    List<Sale> saleList = new ArrayList<Sale>();
    boolean emptyTable = true;
    while (results.next()) {
      int saleId = results.getInt("SALEID");
      Sale currentSale = getSaleById(saleId);
      salesLog.updateSaleItemMap(currentSale, currentSale.getItemMap());
      salesLog.updateSalePriceMap(currentSale, currentSale.getTotalPrice());
      emptyTable = false;
      numberOfsale = numberOfsale + 1;
      totalSale = totalSale.add(currentSale.getTotalPrice());
      if (!saleList.contains(currentSale)) {
        saleList.add(currentSale);
      }
    }
    if (emptyTable) {
      HashMap<Sale, BigDecimal> salesPriceMap = new HashMap<Sale, BigDecimal>();;
      HashMap<Sale, HashMap<Item, Integer>> salesItemMap =
          new HashMap<Sale, HashMap<Item, Integer>>();
      salesLog.setSaleItemMap(salesItemMap);
      salesLog.setSalePriceMap(salesPriceMap);
    }
    salesLog.setTotalSales(totalSale);
    salesLog.setTotalNumberOfSale(numberOfsale);
    salesLog.setSaleList(saleList);
    results.close();
    connection.close();
  }


  /*
   * 
   * PHASE 2 NEW METHODS START
   * 
   */


  /**
   * This method is to get the accounts assigned to a given user.
   * 
   * @param userId the id of the user.
   * @return a result set containing the id's of the accounts.
   * @throws SQLException if SQL fails to read
   * @throws InvalidIdException if the given userId is not valid(exist in database)
   * @throws InvalidItemException if the quantity of the item or itself is not valid
   */
  public static List<Account> getUserAccounts(int userId)
      throws SQLException, InvalidIdException, InvalidItemException {
    boolean judge = isValidUserId(userId);
    List<Account> userAccounts = new ArrayList<Account>();
    if (judge) {
      List<Account> userActiveAccounts = getUserActiveAccounts(userId);
      userActiveAccounts.addAll(getUserInactiveAccounts(userId));
      userAccounts = userActiveAccounts;
      return userAccounts;
    } else {
      throw new InvalidIdException();
    }
  }


  /**
   * This method is to get a list of integers those represents all AccountIds for the given userId.
   * 
   * @param userId the int represents the user's id
   * @return a list of integers those are the accountIds of the users
   * @throws SQLException if SQL fails to read
   * @throws InvalidIdException if the given userId is not valid(exist in database)
   */
  public static List<Integer> getUserAccountIds(int userId)
      throws SQLException, InvalidIdException {
    boolean judge = isValidUserId(userId);
    ResultSet resultSet;
    List<Integer> userAccounts = new ArrayList<Integer>();
    if (judge) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      resultSet = DatabaseSelector.getUserAccounts(userId, connection);
      while (resultSet.next()) {
        int accountId = resultSet.getInt("ID");
        userAccounts.add(accountId);
      }
      resultSet.close();
      connection.close();
      return userAccounts;
    } else {
      throw new InvalidIdException();
    }
  }

  /**
   * This method is to get the details of a given account.
   * 
   * @param accountId the ID of the account.
   * @return Account with the details associated to it.
   * @throws InvalidItemException if the info related to Item are wrong.
   * @throws InvalidIdException if the inputId is invalid
   * @throws SQLException if SQL fails to read
   */
  public static Account getAccountDetails(int accountId)
      throws SQLException, InvalidIdException, InvalidItemException {
    if (DatabaseInsertHelper.accountIdCheck(accountId)) {
      Account account = new AccountImpl();
      account.setId(accountId);
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      ResultSet results = DatabaseSelector.getAccountDetails(accountId, connection);
      ShoppingCart shoppingCart = new ShoppingCart();
      while (results.next()) {
        int itemId = results.getInt("ITEMID");
        int quantity = results.getInt("QUANTITY");
        Item item = DatabaseSelectHelper.getItem(itemId);
        shoppingCart.addItem(item, quantity);
      }
      account.setShoppingCart(shoppingCart);
      results.close();
      connection.close();
      return account;
    } else {
      throw new InvalidIdException();
    }
  }

  /**
   * This method will verified the inputed userId and call getAccountDetails method from its parent
   * class to form a list of InactiveAccount of the given userId.
   * 
   * @param userId the given userId that need to be check and use for find its inactive account.
   * @return a List of userid's inactive account.
   * @throws InvalidIdException if the inputed Id are invalid(DNE in database)
   * @throws SQLException if SQL fails to read
   * @throws InvalidItemException if the item is invalid
   */
  public static List<Account> getUserInactiveAccounts(int userId)
      throws InvalidIdException, SQLException, InvalidItemException {
    if (isValidUserId(userId)) {
      List<Account> listOfInactive = new ArrayList<Account>();
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      ResultSet results = DatabaseSelector.getUserInactiveAccounts(userId, connection);
      while (results.next()) {
        int accountId = results.getInt("ID");
        Account userAccount = getAccountDetails(accountId);
        userAccount.setUser(getUserDetails(userId));
        userAccount.setUserId(userId);
        userAccount.setActive(false);
        listOfInactive.add(userAccount);
      }
      results.close();
      connection.close();
      return listOfInactive;
    } else {
      throw new InvalidIdException();
    }
  }

  /**
   * This method will verified the inputed userId and call getAccountDetails method from its parent
   * class to form a list of activeAccount of the given userId.
   * 
   * @param userId the given userId that need to be check and use for find its active account.
   * @return a List of userid's active account.
   * @throws InvalidIdException if the inputed Id are invalid
   * @throws SQLException if there is something about SQL command.
   * @throws InvalidItemException if the quantity of the item or itself is not valid
   */
  public static List<Account> getUserActiveAccounts(int userId)
      throws SQLException, InvalidIdException, InvalidItemException {
    if (isValidUserId(userId)) {
      List<Account> listOfActive = new ArrayList<Account>();
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      ResultSet results = DatabaseSelector.getUserActiveAccounts(userId, connection);
      while (results.next()) {
        int accountId = results.getInt("ID");
        Account userAccount = getAccountDetails(accountId);
        userAccount.setUser(getUserDetails(userId));
        userAccount.setUserId(userId);
        userAccount.setActive(true);
        listOfActive.add(userAccount);
      }
      results.close();
      connection.close();
      return listOfActive;
    } else {
      throw new InvalidIdException();
    }
  }

  /**
   * This method is to get the status of approval of the given accountId.
   * 
   * @param accountId the int that represents the id of its account
   * @return true if it is approved
   * @throws SQLException if SQL fails to read
   * @throws InvalidIdException if the given accountId is not valid(exist in database)
   * @throws InvalidItemException if the quantity of the item or itself is not valid
   */
  public static List<Account> getUserApproveAccounts(int userId)
      throws SQLException, InvalidIdException, InvalidItemException {
    List<Account> accountList = new ArrayList<Account>();
    if (isValidUserId(userId)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      ResultSet results = DatabaseSelector.getUserApproveAccounts(userId, connection);
      while (results.next()) {
        int accountId = results.getInt("ID");
        int activeValue = results.getInt("ACTIVE");
        int approveValue = results.getInt("APPROVE");
        Account account = getAccountDetails(accountId);
        account.setUserId(userId);
        if (activeValue == 0) {
          account.setActive(false);
        } else {
          account.setActive(true);
        }
        if (approveValue == 0) {
          account.setApprove(false);
        } else {
          account.setApprove(true);
        }
        accountList.add(account);
      }
      connection.close();
      results.close();
      return accountList;
    } else {
      throw new InvalidIdException();
    }
  }

  /**
   * This method is to get all accounts from database.
   * 
   * @return a list of Accounts objects that stored in database
   * @throws SQLException if SQL fails to read
   * @throws InvalidIdException if accountId or userId is not valid(stored in database)
   * @throws InvalidItemException if the quantity of the item or itself is not valid
   */
  public static List<Account> getAllAccounts()
      throws SQLException, InvalidIdException, InvalidItemException {
    List<Account> accountList = new ArrayList<Account>();
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getAllAccount(connection);
    while (results.next()) {
      int accountId = results.getInt("ID");
      int userId = results.getInt("USERID");
      int activeValue = results.getInt("ACTIVE");
      int approveValue = results.getInt("APPROVE");
      Account account = getAccountDetails(accountId);
      account.setUserId(userId);
      if (activeValue == 0) {
        account.setActive(false);
      } else {
        account.setActive(true);
      }
      if (approveValue == 0) {
        account.setApprove(false);
      } else {
        account.setApprove(true);
      }
      accountList.add(account);
    }
    results.close();
    connection.close();
    return accountList;
  }
}

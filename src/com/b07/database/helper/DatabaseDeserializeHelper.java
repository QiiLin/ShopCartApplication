package com.b07.database.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.b07.exceptions.ConnectionFailedException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InvalidAddressException;
import com.b07.exceptions.InvalidAgeException;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidItemException;
import com.b07.exceptions.InvalidTotalPriceException;
import com.b07.exceptions.InvalidUserNameException;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.store.Sale;
import com.b07.store.SalesLog;
import com.b07.users.Account;
import com.b07.users.User;

public class DatabaseDeserializeHelper {
/**
 * This method is to help deserialize list of Account and User object 
 * @param connection the link made to database
 * @return true if this operation is success, false otherwise
 */
  public static boolean deserializeHelper(Connection connection) {
    boolean restored = false;
    try {
      FileInputStream fileIn = new FileInputStream("C:/Users/Qi/Desktop/database_copy.ser");
      ObjectInputStream in = new ObjectInputStream(fileIn);
      List<Account> userAccount = new ArrayList<Account>();
      List<User> userList = new ArrayList<User>();
      int size = in.readInt();
      for (int i = 0; i < size; i++) {
        User user = (User) in.readObject();
        userList.add(user);
      }
      int accountSize = in.readInt();
      for (int i = 0; i < accountSize; i++) {
        Account account = (Account) in.readObject();
        userAccount.add(account);
      }
      int itemSize = in.readInt();
      List<Item> items = new ArrayList<Item>();
      for (int i = 0; i < itemSize; i++) {
        Item item = (Item) in.readObject();
        items.add(item);
      }
      HashMap<Integer, String> roleNames = new HashMap<Integer, String>();
      int roleSize = in.readInt();
      for (int i = 0; i < roleSize; i++) {
        Integer roleId = (Integer) in.readObject();
        String roleName = (String) in.readObject();
        roleNames.put(roleId, roleName);
      }
      HashMap<User, String> userPassword = new HashMap<User, String>();
      int userSize = in.readInt();
      for (int i = 0; i < userSize; i++) {
        User user = (User) in.readObject();
        String password = (String) in.readObject();
        userPassword.put(user, password);
      }
      List<Sale> saleList = new ArrayList<Sale>();
      int saleListSize = in.readInt();
      for (int i = 0; i < saleListSize; i++) {
        Sale sale = (Sale) in.readObject();
        saleList.add(sale);
      }
      Inventory inventory = (Inventory) in.readObject();
      connection.close();
      DatabaseDriverHelper.reInitialize();
      boolean restoreUser = restoreUser(userPassword, roleNames, userList);
      boolean restoreInventoryAndItem = restoreInventoryAndItem(inventory, items);
      boolean restoreSales = restoreSale(saleList);
      boolean restoreAccount = restoreAccounts(userAccount);
      in.close();
      fileIn.close();
      restored = restoreUser && restoreInventoryAndItem && restoreSales && restoreAccount;
    } catch (IOException i) {
      i.printStackTrace();
      System.out.println("Something went wrong on the deserializeation");
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      System.out.println("Something went wrong on the declear class");
    } catch (ConnectionFailedException e) {
      // TODO Auto-generated catch block
      System.out.println("Something went wrong on the connection");
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return restored;
  }
/**
 * This method is to help restore the given list of Accounts by taking each account's id, approve status,
 * activities and items to make new objects and insert to database.
 * @param userAccounts the list of Accounts corresponds to each users
 * @return true if this operation success
 */
  private static boolean restoreAccounts(List<Account> userAccounts) {
    // TODO Auto-generated method stub

    try {
      for (Account account : userAccounts) {
        int accountId =
            DatabaseInsertHelper.insertAccount(account.getUserId(), account.getActive(), account.getApprove());
        HashMap<Item, Integer> itemInCart = account.getShoppingCart().getInformation();
        for (Item item : itemInCart.keySet()) {
          DatabaseInsertHelper.insertAccountLine(accountId, item.getId(), itemInCart.get(item));
        }
      }
      return true;
    } catch (DatabaseInsertException | SQLException | InvalidIdException
         | InvalidItemException e) {
      System.out.println("Due to some error from the data in ser file, opeation failed");
    }

    return false;
  }

  
  /**
   * This method is to restore a list of Sales by re-inserting each of them to database
   * @param saleList the list of Sale needs to be restored
   * @return true if this operation success
   */
  private static boolean restoreSale(List<Sale> saleList) {
    // TODO Auto-generated method stub
    try {
      DatabaseInsertHelper insertHelper = new DatabaseInsertHelper();
      for (Sale sale : saleList) {
        int saleId = insertHelper.insertSale(sale.getUser().getId(), sale.getTotalPrice());
        HashMap<Item, Integer> saleItems = sale.getItemMap();
        for (Item item : saleItems.keySet()) {
          insertHelper.insertItemizedSale(saleId, item.getId(), saleItems.get(item));
        }
      }
      return true;
    } catch (DatabaseInsertException | SQLException | InvalidIdException
        | InvalidTotalPriceException | InvalidItemException e) {
      System.out.println("Due to some error from the data in ser file, opeation failed");
    }
    return false;
  }

  /**
   * This method is to help restore all the items in given inventory.
   * @param currentInventory the inventory wants to get restored
   * @param items the list of items in the inventory
   * @return true if the operation is success
   */
  private static boolean restoreInventoryAndItem(Inventory currentInventory, List<Item> items) {
    DatabaseInsertHelper insertHelper = new DatabaseInsertHelper();
    try {
      for (Item item : items) {
        insertHelper.insertItem(item.getName(), item.getPrice());
      }
      HashMap<Item, Integer> inventory = currentInventory.getItemMap();
      for (Item item : inventory.keySet()) {
        insertHelper.insertInventory(item.getId(), inventory.get(item));
      }
      return true;
    } catch (SQLException | DatabaseInsertException | InvalidIdException | InvalidItemException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * This method is to restore the given list of users by taking its userId match to password,
   * roleName match to roleId.
   * @param userPassword the HashMap matches user to its password
   * @param roleNames the hashMap matches roleId to its roleName
   * @param userList the list of users needs to be restored
   * @return
   */
  private static boolean restoreUser(HashMap<User, String> userPassword,
      HashMap<Integer, String> roleNames, List<User> userList) {
    try {
      for (User user : userList) {
        String password = userPassword.get(user);
        int userId;
        userId = DatabaseInsertHelper.insertNewUser(user.getName(), user.getAge(),
            user.getAddress(), "0");
        String roleName = roleNames.get(user.getRoleId());
        int roleId = DatabaseInsertHelper.insertRole(roleName);
        DatabaseInsertHelper.insertUserRole(userId, roleId);
        DatabaseUpdateHelper.updateUserPassword(password, user.getId());
      }
      return true;
    } catch (SQLException | DatabaseInsertException | InvalidAddressException | InvalidAgeException
        | InvalidUserNameException | InvalidIdException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }
}

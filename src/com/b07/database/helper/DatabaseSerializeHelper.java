package com.b07.database.helper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

public class DatabaseSerializeHelper {
  
  /**
   * This method is to help converting User and Account object into a sequence of bytes
   * @return 
   */
  public static boolean serializeHelper() {
    try {
      FileOutputStream fileOut = new FileOutputStream("C:/Users/Qi/Desktop/database_copy.ser");
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      List<User> userList = DatabaseSelectHelper.getUsersDetails();
      getUserRoles(userList);
      List<Account> accountList = DatabaseSelectHelper.getAllAccounts();
      int size = userList.size();
      out.writeInt(size);
      for (User user : userList) {
        out.writeObject(user);
      }
      out.writeInt(accountList.size());
      for (Account account : accountList) {
        out.writeObject(account);
      }
      List<Item> allItem = DatabaseSelectHelper.getAllItems();
      out.writeInt(allItem.size());
      for (Item item : allItem) {
        out.writeObject(item);
      }
      HashMap<Integer, String> roleNames = getRoleNames();
      out.writeInt(roleNames.size());
      for (Integer roleId : roleNames.keySet()) {
        out.writeObject(roleId);
        out.writeObject(roleNames.get(roleId));
      }
      HashMap<User, String> userPassword = getUserPassword(userList);
      out.writeInt(userPassword.size());
      for (User user : userList) {
        out.writeObject(user);
        out.writeObject(userPassword.get(user));
      }
      List<Sale> saleList = getsaleList(userList);
      out.writeInt(saleList.size());
      for (Sale sale : saleList) {
        out.writeObject(sale);
      }
      Inventory currentInventory = DatabaseSelectHelper.getInventory();
      out.writeObject(currentInventory);
      out.close();
      fileOut.close();
      System.out.println("Serialized data is saved in C:/Users/Qi/Desktop/database_copy.ser");
      return true;
    } catch (IOException | SQLException | InvalidIdException | InvalidItemException e) {
      return false;
    }
  }

 /**
  * This method is to set all user's roleId in the given list of User 
  * @param userList the list of user needs to be set their roleId 
  * @throws SQLException if SQL failes to read or write
  * @throws InvalidIdException if the roleId is not valid(exist in database)
  */
  private static void getUserRoles(List<User> userList) throws SQLException, InvalidIdException {
    // TODO Auto-generated method stub
      for (User user : userList) {
        int roleId = DatabaseSelectHelper.getUserRoleId(user.getId());
        user.setRoleId(roleId);
      }
  }

  /**
   * This method is to get user's password matches to itself.
   * @param userList the list of user needs to be matching
   * @return a hashMap that contains all users with relation User to password
   */
  private static HashMap<User, String> getUserPassword(List<User> userList) {
    HashMap<User, String> userPassword = new HashMap<User, String>();
    try {
      for (User currentUser : userList) {
        String password = null;
        password = DatabaseSelectHelper.getPassword(currentUser.getId());
        userPassword.put(currentUser, password);
      }
    } catch (SQLException | InvalidIdException e) {
      // TODO Auto-generated catch block
      System.out.println("Some internal error");
    }
    return userPassword;
  }

  /**
   * This method is to get user's roleId matches to roleName.
   * @param userList the list of user needs to be matching
   * @return a hashMap that contains all users with relation roleId to roleName
   */
  private static HashMap<Integer, String> getRoleNames() {
    List<Integer> roleIds;
    HashMap<Integer, String> roleNames = new HashMap<Integer, String>();
    try {
      roleIds = DatabaseSelectHelper.getRoleIds();
      for (Integer roleId : roleIds) {
        String roleName = DatabaseSelectHelper.getRoleName(roleId);
        roleNames.put(roleId, roleName);
      }
      return roleNames;
    } catch (SQLException | InvalidIdException e) {
      // TODO Auto-generated catch block
      System.out.println("Some internal error");
    }
    return roleNames;
  }

  /**
   * This method is to get a list of sales of each user.
   * @param userList the list of Users that will get provide info about their sales
   * @return a list of sale of all given users
   */
  private static List<Sale> getsaleList(List<User> userList) {
    List<Sale> saleIds = new ArrayList<Sale>();
    try {
      for (User user : userList) {
        saleIds.addAll(DatabaseSelectHelper.getSalesToUser(user.getId()));
      }
    } catch (SQLException | InvalidIdException e) {
      // TODO Auto-generated catch block
      return saleIds;
    }
    return saleIds;
  }
}

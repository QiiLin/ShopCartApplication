package com.b07.database.helper;

import com.b07.database.DatabaseInserter;
import com.b07.database.DatabaseSelector;
import com.b07.database.helper.DatabaseDriverHelper;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InvalidAddressException;
import com.b07.exceptions.InvalidAgeException;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidItemException;
import com.b07.exceptions.InvalidTotalPriceException;
import com.b07.exceptions.InvalidUserNameException;
import com.b07.inventory.Item;
import com.b07.inventory.ItemTypes;
import com.b07.store.Sale;
import com.b07.users.Account;
import com.b07.users.Roles;
import com.b07.users.User;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DatabaseInsertHelper extends DatabaseInserter {

  /**
   * This method test about whether or not the name is a type of Role ENUM.
   * 
   * @param name String that we need to check
   * @return boolean value: true if name is a type of Roles ENUM, false otherwise.
   */
  public static boolean isInRoles(String name) {
    // if name is not null
    if (name != null) {
      // loop through a Roles values once
      for (Roles currentRole : Roles.values()) {
        // if the name equals to the currentRole.
        if (currentRole.name().equals(name)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * This method will validate whether or not the input is valid, throw exception if the input
   * is not in the correct format. If the inputed name is in correct format, 
   * then call the insertRole method to add data to database;
   * 
   * @param name input String we need to check before insertRole is called
   * @return a int value that represents the id value of the inputed role name
   * @throws InvalidIdException throw if the given roleName is in database
   * @throws DatabaseInsertException if the database is fail to be insert
   * @throws SQLException throw it if the SQL has some error on reading or writing database.
   */
  public static int insertRole(String name)
      throws InvalidIdException, DatabaseInsertException, SQLException {

    // if name is a roles type enum
    if (isInRoles(name)) {
      // start connection with database
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      // get roleId for the location of name data in database
      int roleId = DatabaseInserter.insertRole(name, connection);
      connection.close();
      return roleId;
    } else {
      throw new InvalidIdException();
    }
  }

  /**
   * This method is to check whether address is under the length limit(100).
   * 
   * @param address the string represents address need to be checking length
   * @return true if address is under limit, false otherwise
   */
  public static boolean addressLimitCheck(String address) {
    boolean withInLimit = false;
    // if address is not null
    if (address != null) {
      // if address is less than 101
      if (address.length() <= 100) {
        withInLimit = true;
      }
    }
    return withInLimit;
  }

  /**
   * This method checks the given userName is in correct format(Firstname Lastname).
   * 
   * @param userName the name we need to check format with it.
   * @return true if userName is in right format, false otherwise
   */
  public static boolean userNameFormatCheck(String userName) {
    if (userName == null) {
      return false;
    }
    // splite userName into Array of String that contains all the word in it
    String[] splitedName = userName.split("\\s+");
    // By add up the first index and second index with a space between it, we form a good formatName
    String goodFormatNameCopy = splitedName[0] + " " + splitedName[1];
    // if there are only 2 word in userName and goodCopy version is the same as userName
    if (splitedName.length == 2 && goodFormatNameCopy.equals(userName)) {
      // loop through each char in each word of userName
      for (String currentWord : splitedName) {
        char firstCharacter = currentWord.charAt(0);
        // if the firstCharacter is upper case and is alphabetic
        if (Character.isUpperCase(firstCharacter) && Character.isAlphabetic(firstCharacter)) {
          // we get sub string of it from index 1
          char[] charList = currentWord.substring(1).toCharArray();
          // loop through all elelemnt in the charlIst
          for (char currentChar : charList) {
            // if it is not lowcase or it is not alphabetic
            if (Character.isUpperCase(currentChar) || !(Character.isAlphabetic(currentChar))) {
              return false;
            }
          }
        } else {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  /**
   * This method is to check the age is valid(above zero).
   * 
   * @param age the int we need to check validity
   * @return true of age is above zero, false otherwise.
   */
  public static boolean ageCheck(int age) {
    boolean valid = false;
    if (age > 0) {
      valid = true;
    }
    return valid;

  }

  /**
   * This method will verified the whether or not the input is correct, throw exception if the input
   * is not correct.If the inputed param are all correct, then call the insertNewUser method to add
   * data to database.
   * 
   * @param name String we need to check name format
   * @param age int we need to check above 0
   * @param address String we need check it is under 101 length
   * @param password a password come long there data
   * @return int value to represent the new generated user's id
   * @throws SQLException throw it if SQL fail to read the book
   * @throws DatabaseInsertException throw if the Database fail to insert data
   * @throws InvalidAddressException throw if address is not in right form
   * @throws InvalidAgeException throw if the age is below zero
   * @throws InvalidUserNameException throw if the usre Name is not in right format
   */
  public static int insertNewUser(String name, int age, String address, String password)
      throws SQLException, DatabaseInsertException, InvalidAddressException, InvalidAgeException,
      InvalidUserNameException {
    // Check if age is valid
    if (ageCheck(age)) {
      // Check if address is valid
      if (addressLimitCheck(address)) {
        // Check if name is valid
        if (userNameFormatCheck(name)) {
          // If nothing is invalid then make the connection and insert this new user and get its id
          Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
          int userId = DatabaseInserter.insertNewUser(name, age, address, password, connection);
          connection.close();
          return userId;
        } else {
          throw new InvalidUserNameException();
        }
      } else {
        throw new InvalidAddressException();
      }
    } else {
      throw new InvalidAgeException();
    }
  }

  /**
   * This method will check the inputed param exist, if they do, then call the method inserUserRole
   * to insert the relationship between user Id and role Id.
   * 
   * @param userId the userId we need to check whether it exists, true if it does, false otherwise
   * @param roleId the roleId we need to check whether it exists, true if it does, false otherwise
   * @return int value to representation their data location in databse
   * @throws DatabaseInsertException throw if the method failed to insert
   * @throws SQLException throw if the SQL failed to wrong database
   * @throws InvalidIdException throw if the Role id or userId is not in database
   */
  public static int insertUserRole(int userId, int roleId)
      throws DatabaseInsertException, SQLException, InvalidIdException {
    // Check both id are valid(exist) in database
    if (DatabaseSelectHelper.roleIdCheck(roleId)) {
      if (DatabaseSelectHelper.isValidUserId(userId)) {
        // If they are valid then make the connection and complete the process
        Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        int userRoleId = DatabaseInserter.insertUserRole(userId, roleId, connection);
        connection.close();
        return userRoleId;
      } else {
        throw new InvalidIdException();
      }
    } else {
      throw new InvalidIdException();
    }

  }

  /**
   * This method verify the given name is in itemType ENUM.
   * 
   * @param name the String vale we need to check
   * @return true if the name is a itmeType ENUM, false otherwise
   */
  public static boolean isInItemType(String name) {
    if (name != null) {
      List<Item> itemList;
      try {
        // get all items and check whether the item is in it
        itemList = DatabaseSelectHelper.getAllItems();
        for (Item item : itemList) {
          if (item.getName().equals(name)) {
            return true;
          }
        }
        for (ItemTypes  current : ItemTypes.values()) {
          if (current.name().equals(name)) {
            return true;
          }
        }
      // If any exception happens, this operation fails
      } catch (SQLException | InvalidIdException e) {

        return false;
      }
    }
    return false;
  }

  /**
   * This will check item price is valid(greater than 0).
   * 
   * @param price the BigDeicmal we need to check
   * @return true if the price is right form, false otherwise
   */
  public boolean validItemPriceCheck(BigDecimal price) {
    boolean itemNameCheck = false;
    if (price != null) {
      // set the least it can be is 0
      BigDecimal bottomValue = new BigDecimal(0);
      if (price.compareTo(bottomValue) == 1) {
        itemNameCheck = true;
      }
    }
    return itemNameCheck;
  }

  /**
   * This method is to insert the given item with correspond price to database.
   * 
   * @param name the String represent the item's name.
   * @param price the BigDecimal that need check greater than 0
   * @return a int value to represent the id record in database
   * @throws SQLException when SQL operation failed
   * @throws DatabaseInsertException when insertion failed
   * @throws InvalidItemException when item name is not found as ENUM
   */
  public int insertItem(String name, BigDecimal price)
      throws SQLException, DatabaseInsertException, InvalidItemException {
    // Check if the price is set legally 
    if (validItemPriceCheck(price)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      price = price.setScale(2, RoundingMode.HALF_UP);
      int itemId = DatabaseInserter.insertItem(name, price, connection);
      connection.close();
      return itemId;
    } else {
      throw new InvalidItemException();
    }

  }

  /**
   * this method will check all the inputed parm first then insert the item to database
   * 
   * @param itemId the id value we need to check if it is in the database.
   * @param quantity the int value we need to check it is bigger or equal to zero
   * @return int value to represent the inserted record in database
   * @throws DatabaseInsertException throw if it fail to insert
   * @throws SQLException throw if the SQL operation fails
   * @throws InvalidItemException if the quantity is not a valid value(>= 0)
   * @throws InvalidIdException throw if itemId is not correct
   */
  public int insertInventory(int itemId, int quantity)
      throws DatabaseInsertException, SQLException, InvalidIdException, InvalidItemException {
    if (itemQuantityCheck(quantity)) {
      if (DatabaseSelectHelper.itemIdCheck(itemId)) {
        // If both quantity and itemId are valid then make the connection and insert this item to database
        Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        int inventoryId = DatabaseInserter.insertInventory(itemId, quantity, connection);
        connection.close();
        return inventoryId;
      } else {
        throw new InvalidIdException();
      }
    } else {
      throw new InvalidItemException();
    }
  }

  /**
   * this method will check total price to be bigger than 0
   * 
   * @param totalPrice the value we need to check
   * @return true if totalPrice is valid(>=0), false otherwise
   */
  private boolean totalPriceCheck(BigDecimal totalPrice) {
    BigDecimal zero = new BigDecimal(0);
    if (totalPrice.compareTo(zero) != -1) {
      return true;
    }
    return false;
  }

  /**
   * This method inserts sale to database if all it param checked and passed.
   * 
   * @param userId int Id we to check if it is in database
   * @param totalPrice in value we need to check.
   * @return true if the operation executed, false otherwise
   * @throws DatabaseInsertException throw if the quantity is incorrect
   * @throws SQLException throw if the quantity is incorrect
   * @throws InvalidIdException throw if the userId does not correct(exists)
   * @throws InvalidTotalPriceException throw if the total price is not valid(>=0)
   */
  public int insertSale(int userId, BigDecimal totalPrice) throws DatabaseInsertException,
      SQLException, InvalidIdException, InvalidTotalPriceException {
    // TODO Implement this method as stated on the assignment sheet
    // hint: You should be using these three lines in your final code
    if (totalPriceCheck(totalPrice)) {
      if (DatabaseSelectHelper.isValidUserId(userId)) {
        Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        int saleId = DatabaseInserter.insertSale(userId, totalPrice, connection);
        connection.close();
        return saleId;
      } else {
        throw new InvalidIdException();
      }
    } else {
      throw new InvalidTotalPriceException();
    }
  }

  /**
   * This method check the itemQuantity is bigger or equal to zero.
   * 
   * @param quantity int value we need to check
   * @return true if it is bigger than or equal to zero.
   */
  private boolean itemQuantityCheck(int quantity) {
    boolean valid = false;
    if (quantity >= 0) {
      valid = true;
    }
    return valid;
  }

  /**
   * This method will take quantity input and verify it is bigger than zero.
   * 
   * @param quantity the int value we need to check
   * @return true if it pass the check ,false otherwise
   */
  private boolean itemSaleQuantityCheck(int quantity) {
    boolean valid = false;
    if (quantity > 0) {
      valid = true;
    }
    return valid;
  }

  /**
   * This method is Check saleId in database.
   * 
   * @param saleId the id value we need to check
   * @return true if it works, false otherwise
   */
  public static boolean salesIdCheck(int saleId) {
    try {
      List<User> listOfUser = DatabaseSelectHelper.getUsersDetails();
      List<Sale> saleIds = new ArrayList<Sale>(); 
      for (User user:listOfUser) {
        saleIds.addAll(DatabaseSelectHelper.getSalesToUser(user.getId()));
      }
      for (Sale sale : saleIds) {
        if (sale.getId() == saleId) {
          return true;
        }
      }
    } catch (SQLException | InvalidIdException e) {
      // TODO Auto-generated catch block
      return false;
    }
    return false;
  }

  /**
   * This method is to insert sale items and quantity to the saleId.
   * 
   * @param saleId the id we need to check if it is valid(exist in database)
   * @param itemId the id we need to check if it is valid(exist in database)
   * @param quantity the id we need to check if it is positive
   * @return int value to represent record of this insertion in database
   * @throws DatabaseInsertException if it failed to insert in database
   * @throws SQLException if SQL failed to read
   * @throws InvalidItemException throw if the quantity of this Item is not valid
   * @throws InvalidIdException throw if failed to insert
   */
  public int insertItemizedSale(int saleId, int itemId, int quantity)
      throws DatabaseInsertException, SQLException, InvalidItemException, InvalidIdException {
    if (itemSaleQuantityCheck(quantity)) {
      if (DatabaseSelectHelper.itemIdCheck(itemId)) {
        if (salesIdCheck(saleId)) {
          Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
          int itemizedId =
              DatabaseInserter.insertItemizedSale(saleId, itemId, quantity, connection);
          connection.close();
          return itemizedId;
        } else {
          throw new InvalidIdException();
        }
      } else {
        throw new InvalidIdException();
      }
    } else {
      throw new InvalidItemException();
    }

  }

  /*
   * 
   * PHASE 2 NEW METHODS START
   * 
   */

/**
 * This method is to insert an account with status of activity to database for given user by its userId.
 * @param userId the id represents id for the user
 * @param active the boolean represents the status of activity
 * @return a int represent the account after complete this process
 * @throws SQLException if SQL fails to read
 * @throws DatabaseInsertException if the insert operation failed in database
 * @throws InvalidIdException if the given userId is not valid(exist in database)
 */
  public static int insertAccount(int userId, boolean active, boolean approve)
      throws SQLException, DatabaseInsertException, InvalidIdException {
    // Check whether userId is valid
    boolean judge = DatabaseSelectHelper.isValidUserId(userId);
    // if judge is true means the userId is valid(in database)
    if (judge) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      int accountId = DatabaseInserter.insertAccount(userId, active, approve, connection);
      connection.close();
      return accountId;
    } else {
      throw new InvalidIdException();
    }

  }

  /**
   * This method is to check whether or not the given accountId is valid(in database).
   * 
   * @param accountId the int id for account
   * @return true if the id is in database, false otherwise
   * @throws SQLException if SQL failed to read or write database
   * @throws InvalidIdException if the given accountId is not valid
   */
  public static boolean accountIdCheck(int accountId)
      throws SQLException, InvalidIdException {
    try {
      List<User> listOfUser = DatabaseSelectHelper.getUsersDetails();
      List<Integer> accountIds = new ArrayList<Integer>(); 
      for (User user:listOfUser) {
        accountIds.addAll(DatabaseSelectHelper.getUserAccountIds(user.getId()));
      }
      if (accountIds.contains(accountId)) {
        return true;
      } else {
        return false;
      }
    } catch (SQLException | InvalidIdException e) {
      // TODO Auto-generated catch block
      return false;
    }
  }

/**
 * This method is to insert an item to the given accountId with quantity.
 * 
 * @param accountId the int represents the id for its account 
 * @param itemId the int represents the id for the given item
 * @param quantity the int represents quantity
 * @return a int represents the record if this operation is executed  
 * @throws SQLException if SQL fail to read
 * @throws InvalidIdException if the given accountId is not valid(exist in database)
 * @throws DatabaseInsertException if it fails to insert in database
 * @throws InvalidItemException if the Item quantity is not valid
 */
  public static int insertAccountLine(int accountId, int itemId, int quantity)
      throws SQLException, InvalidIdException, DatabaseInsertException,
       InvalidItemException {
    boolean accountIdJudge = accountIdCheck(accountId);
    boolean itemIdJudge = DatabaseSelectHelper.itemIdCheck(itemId);
    boolean quantityJudge = false;
    if (itemIdJudge) {
      int itemQuantity = DatabaseSelectHelper.getInventoryQuantity(itemId);
      if (itemQuantity >= quantity) {
        quantityJudge = true;
      }
    }
    if (accountIdJudge && itemIdJudge && quantityJudge) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      int lineId = DatabaseInserter.insertAccountLine(accountId, itemId, quantity, connection);
      connection.close();
      return lineId;
    } else if (accountIdJudge == false || itemIdJudge == false) {
      throw new InvalidIdException();
    } else {
      throw new InvalidItemException();
    }

  }
}

package com.b07.database.helper;

import com.b07.database.DatabaseUpdater;
import com.b07.exceptions.InvalidAddressException;
import com.b07.exceptions.InvalidAgeException;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidItemException;
import com.b07.exceptions.InvalidUserNameException;
import com.b07.users.Roles;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DatabaseUpdateHelper extends DatabaseUpdater {

  /**
   * This method is to validate the given price.
   * @param price the BigDecimal represents price
   * @return true if the price is set correctly(greater than 0), false otherwise
   */
  private static boolean vaildItemPriceCheck(BigDecimal price) {
    boolean itemNameCheck = false;
    if (price != null) {
      BigDecimal bottomValue = new BigDecimal(0);
      if (price.compareTo(bottomValue) == 1) {
        itemNameCheck = true;
      }
    }
    return itemNameCheck;
  }

  /**
   * This method is to update the role name of the inputed roleId.
   * 
   * @param name the role name we need to check
   * @param id the role we need to check it in database
   * @return true if the update complete, false otherwise
   * @throws SQLException throw if SQL fails to read
   * @throws InvalidIdException throw if the given roleId is not valid(exist in database)
   */
  public static boolean updateRoleName(String name, int id)
      throws SQLException, InvalidIdException {
    if (DatabaseInsertHelper.isInRoles(name)) {
      if (DatabaseSelectHelper.roleIdCheck(id)) {
        Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        boolean complete = DatabaseUpdater.updateRoleName(name, id, connection);
        connection.close();
        return complete;
      } else {
        throw new InvalidIdException();
      }
    } else {
      throw new InvalidIdException();
    }
  }

  /**
   * This method is to update UserName to the userId
   * 
   * @param name string value we need check.
   * @param userId int value we need check
   * @return true if the operation complete, false otherwise
   * @throws SQLException throw if SQL failed to read
   * @throws InvalidIdException if userId is not valid(exist in database)
   * @throws InvalidUserNameException if the given name is not valid(in correct format)
   */
  public static boolean updateUserName(String name, int userId)
      throws SQLException, InvalidUserNameException, InvalidIdException {
    if (DatabaseSelectHelper.isValidUserId(userId)) {
      if (DatabaseInsertHelper.userNameFormatCheck(name)) {
        Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        boolean complete = DatabaseUpdater.updateUserName(name, userId, connection);
        connection.close();
        return complete;
      } else {
        throw new InvalidUserNameException();
      }
    } else {
      throw new InvalidIdException();
    }
  }

  /**
   * This method is to update the UserAge.
   * 
   * @param age the int represents age value we need to check
   * @param userId the int represents the id for its user value we need to check
   * @return true if the operation completed, false otherwise
   * @throws InvalidAgeException if age is not valid(positive)
   * @throws SQLException if SQL fails to read
   * @throws InvalidIdException if userId is not valid(exist in database)
   */
  public static boolean updateUserAge(int age, int userId)
      throws InvalidAgeException, SQLException, InvalidIdException {
    if (DatabaseInsertHelper.ageCheck(age)) {
      if (DatabaseSelectHelper.isValidUserId(userId)) {
        Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        boolean complete = DatabaseUpdater.updateUserAge(age, userId, connection);
        connection.close();
        return complete;
      } else {
        throw new InvalidIdException();
      }
    } else {
      throw new InvalidAgeException();
    }
  }

  /**
   * This method is update the users address.
   * 
   * @param address the String we need to check length
   * @param userId id we need check if it in database
   * @return true if the update is complete, false, otherwise
   * @throws InvalidIdException if the userId is not valid(exist in database)
   * @throws SQLException SQL fails to read
   * @throws InvalidAddressException the address is not in correct format
   */
  public static boolean updateUserAddress(String address, int userId)
      throws InvalidIdException, SQLException, InvalidAddressException {
    if (DatabaseInsertHelper.addressLimitCheck(address)) {
      if (DatabaseSelectHelper.isValidUserId(userId)) {
        Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        boolean complete = DatabaseUpdater.updateUserAddress(address, userId, connection);
        connection.close();
        return complete;
      } else {
        throw new InvalidIdException();
      }
    } else {
      throw new InvalidAddressException();
    }
  }

  /**
   * This updateUserRole method is to update the user's role with inputed param.
   * 
   * @param roleId the int id we need to check if it is database
   * @param userId the int id we need to check if it is database
   * @return true of the update completed
   * @throws InvalidIdException if roleId or userId is not valid(exist in database)
   * @throws SQLException throw if SQL fails to read
   */
  public static boolean updateUserRole(int roleId, int userId)
      throws InvalidIdException, SQLException {
    if (DatabaseSelectHelper.roleIdCheck(roleId)) {
      if (DatabaseSelectHelper.isValidUserId(userId)) {
        Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        boolean complete = DatabaseUpdater.updateUserRole(roleId, userId, connection);
        connection.close();
        return complete;
      } else {
        throw new InvalidIdException();
      }
    } else {
      throw new InvalidIdException();
    }
  }


  /**
   * This method is to update the name of item id in database.
   * 
   * @param name the name must be itemType ENUM
   * @param itemId the id must exist in the database
   * @return true of the update complete, false otherwise
   * @throws InvalidItemException if the quantity of the item or itself is not valid
   * @throws InvalidIdException if itemId is not valid(exist in database)
   * @throws SQLException throw if SQL failed to read
   */
  public static boolean updateItemName(String name, int itemId)
      throws InvalidItemException, InvalidIdException, SQLException {
    if (DatabaseInsertHelper.isInItemType(name)) {
      if (DatabaseSelectHelper.itemIdCheck(itemId)) {
        Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        boolean complete = DatabaseUpdater.updateItemName(name, itemId, connection);
        connection.close();
        return complete;
      } else {
        throw new InvalidIdException();
      }
    } else {
      throw new InvalidItemException();
    }
  }

  /**
   * This method is to update the item price to an item Id.
   * 
   * @param price the in price that need to be check
   * @param itemId the id item must be in the current database
   * @return true if the update completed, false otherwise
   * @throws SQLException if SQL fails to read
   * @throws InvalidIdException if itemId is not valid(exist in database)
   * @throws InvalidItemException if item is not valid
   */
  public static boolean updateItemPrice(BigDecimal price, int itemId)
      throws SQLException, InvalidIdException, InvalidItemException {
    if (vaildItemPriceCheck(price)) {
      if (DatabaseSelectHelper.itemIdCheck(itemId)) {
        Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        boolean complete = DatabaseUpdater.updateItemPrice(price, itemId, connection);
        connection.close();
        return complete;
      } else {
        throw new InvalidIdException();
      }
    } else {
      throw new InvalidItemException();
    }
  }

  /**
   * This method is to validate the given quantity by whether it is positive.
   * @param quantity the int represents quantity of items
   * @return true if given quantity is greater or equal to 0
   */
  private static boolean itemQuantityCheck(int quantity) {
    boolean valid = false;
    if (quantity >= 0) {
      valid = true;
    }
    return valid;
  }

  /**
   * This method is to update the item quantity of item id in inventory.
   * 
   * @param quantity the quantity must be greater or equal to zero
   * @param itemId the the id for its item must be exist in the database
   * @return true if the update complete, false otherwise
   * @throws InvalidIdException if the itemId is not valid(exist in database)
   * @throws InvalidItemException if the quantity of the item or itself is not valid
   * @throws SQLException if SQL failed to read
   */
  public static boolean updateInventoryQuantity(int quantity, int itemId)
      throws InvalidIdException, InvalidItemException, SQLException {
    if (itemQuantityCheck(quantity)) {
      if (DatabaseSelectHelper.itemIdCheck(itemId)) {
        Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        boolean complete = DatabaseUpdater.updateInventoryQuantity(quantity, itemId, connection);
        connection.close();
        return complete;
      } else {
        throw new InvalidIdException();
      }
    } else {
      throw new InvalidItemException();
    }
  }

  /**
   * This method is to check the input data and call the method to update the state of the given
   * accountId.
   * 
   * @param accountId the given account Id that we need to check and update its state
   * @param active the state of the given account Id
   * @return true if the operation success, false otherwise.
   * @throws InvalidItemException if the item is invalid
   * @throws InvalidIdException if the accountId is not valid(exsit in database)
   * @throws SQLException if SQL fails to read
   */
  public static boolean updateAccountStatus(int accountId, boolean active)
      throws SQLException, InvalidIdException, InvalidItemException {
    boolean result = false;
    if (DatabaseInsertHelper.accountIdCheck(accountId)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      result = DatabaseUpdater.updateAccountStatus(accountId, active, connection);
      connection.close();
      return result;
    } else {
      throw new InvalidIdException();
    }
  }
  
  /**
   * This method is to update the given user's password
   * @param password the String represents the new password
   * @param id the userId represents it user's id 
   * @return true if this operation success
   * @throws SQLException if SQL fails to read
   * @throws InvalidIdException if id is not valid(exist in database)
   */
  public static boolean updateUserPassword(String password, int id) throws SQLException, InvalidIdException {
    if (DatabaseSelectHelper.isValidUserId(id)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      boolean result = DatabaseUpdater.updateUserPassword(password, id, connection);
      connection.close();
      return result;
    } else {
      throw new InvalidIdException();
    }
  }
  
  /**
   * This method is to update the latest approval status for the given accountId
   * @param accountId the int represents the id for this account
   * @param approve the latest status need to be update to be
   * @return true if this operation success
   * @throws SQLException if SQL fails to read
   * @throws InvalidIdException if the accountId is not valid(exist in database)
   * @throws InvalidItemException if the quantity of the item or itself is not valid
   */
  public static boolean updateAccountApprove(int accountId, boolean approve)
      throws SQLException, InvalidIdException, InvalidItemException {
    boolean result = false;
    if (DatabaseInsertHelper.accountIdCheck(accountId)) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      result = DatabaseUpdater.updateAccountApprove(accountId, approve, connection);
      connection.close();
      return result;
    } else {
      throw new InvalidIdException();
    }
  }
}

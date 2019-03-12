package com.b07.store;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InvalidAddressException;
import com.b07.exceptions.InvalidAgeException;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidItemException;
import com.b07.exceptions.InvalidUserNameException;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.users.Employee;
import com.b07.users.Roles;
import java.sql.SQLException;
import java.util.HashMap;

public class EmployeeInterface {
  private Employee currentEmployee;
  private Inventory inventory;
  
  /**
   * Create a EmployeeInteface class instance with employee set and inventory set.
   * @param currentEmployee pass in employee object
   * @param inventory pass in Inventory Object
   * @throws InvalidInventoryException throw if Inventory is not valid
   * @throws InvalidEmployeeException throw if the employee is not valid
   */
  public EmployeeInterface(Employee currentEmployee, Inventory inventory) {
    this.setCurrentEmployee(currentEmployee);
    this.inventory = inventory;
  }
  
  /**
   * Create a EmployeeInteface class instance with employee set and inventory set.
   * @param inventory pass in Inventory Object
   * @throws InvalidInventoryException throw if Inventory is not valid
   */
  public EmployeeInterface(Inventory inventory) {
    this.inventory = inventory;
  }
  
  /**
   * This method set the currentEmployee Object of Interface.
   * @param employee employee object that need to set
   * @throws InvalidEmployeeException throw if Employee is not valid
   */
  public void setCurrentEmployee(Employee employee) {
    this.currentEmployee = employee;
  }

  /**
   * This method check current Employee.
   * @return true if it has, false otherwise.
   */
  public boolean hasCurrentEmployee() {
    boolean employeeExist = true;
    if (currentEmployee == null) {
      employeeExist = false;
    }
    return employeeExist;
  }
  
  /**
   * The method restock the InVENTORY object in this field.
   * @param item item object that will be place in here
   * @param quantity the quantity of item inputted
   * @return true if the restock is done, false, otherwise
   * @throws InvalidItemQuantityException throw if Quantity is less than zero
   * @throws InvalidItemException throw if the item is not valid 
   */
  public boolean restockInventory(Item item, int quantity)
      throws InvalidItemException {
    boolean update = true;
    if (item != null) {
      if (quantity > 0) {
        HashMap<Item, Integer> items = this.inventory.getItemMap();
        int totalQuantity = quantity;
        if (items.containsKey(item)) {
          totalQuantity = items.get(item) + totalQuantity;
          this.inventory.updateMap(item, totalQuantity);
          try {
            DatabaseUpdateHelper.updateInventoryQuantity(item.getId(), totalQuantity);
          } catch (SQLException
              | InvalidIdException e) {
            // TODO Auto-generated catch block
            update = false;
          }
        } else {
          DatabaseInsertHelper insertHelper = new DatabaseInsertHelper();
          try {
            insertHelper.insertInventory(item.getId(), quantity);
          } catch (DatabaseInsertException | SQLException
              | InvalidIdException e) {
            // TODO Auto-generated catch block
            update = false;
          }
        }
      } else {
        update = false;
      }
    } else {
      update = false;
    }
    return update;
  }

  /** 
   * This method create a Customer info in the database .
   * @param name the name of user
   * @param age  the age of usrer
   * @param address the addess of user
   * @param password the password of usre
   * @return true if the creation is done, false otherwise
   * @throws InvalidRolesException throw if the role Id is not in database
   * @throws DatabaseInsertException throw if the role Id is not in database
   * @throws SQLException throw if the role Id is not in database
   * @throws InvalidAddressException throw if the role Id is not in database
   * @throws InvalidAgeException throw if the role Id is not in database
   * @throws InvalidUserNameException throw if the role Id is not in database
   * @throws InvalidRoleIdException throw if the role Id is not in database
   * @throws InvalidUserIdException throw if the role Id is not in database
   * @throws InvalidPasswordException throw if the role Id is not in database
   */
  public static int createCustomer(String name, int age, String address, String password)
      throws InvalidIdException, DatabaseInsertException, SQLException, InvalidAddressException,
      InvalidAgeException, InvalidUserNameException, InvalidIdException {
    int roleId = DatabaseInsertHelper.insertRole(Roles.CUSTOMER.name());
    int userId = DatabaseInsertHelper.insertNewUser(name, age, address, password);
    DatabaseInsertHelper.insertUserRole(userId, roleId);
    return userId;
  }
  
  /** 
   * This method create a Employee info in the database .
   * @param name the name of user
   * @param age  the age of usrer
   * @param address the addess of user
   * @param password the password of usre
   * @return true if the creation is done, false otherwise
   * @throws InvalidRolesException throw if the role Id is not in database
   * @throws DatabaseInsertException throw if the role Id is not in database
   * @throws SQLException throw if the role Id is not in database
   * @throws InvalidAddressException throw if the role Id is not in database
   * @throws InvalidAgeException throw if the role Id is not in database
   * @throws InvalidUserNameException throw if the role Id is not in database
   * @throws InvalidRoleIdException throw if the role Id is not in database
   * @throws InvalidUserIdException throw if the role Id is not in database
   * @throws InvalidPasswordException throw if the role Id is not in database
   */
  public int createEmployee(String name, int age, String address, String password)
      throws InvalidIdException, DatabaseInsertException, SQLException, InvalidAddressException,
      InvalidAgeException, InvalidUserNameException, InvalidIdException {
    int roleId = DatabaseInsertHelper.insertRole(Roles.EMPLOYEE.name());
    int userId = DatabaseInsertHelper.insertNewUser(name, age, address, password);
    DatabaseInsertHelper.insertUserRole(userId, roleId);
    return userId;
  }

}


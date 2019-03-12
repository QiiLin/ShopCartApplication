package com.b07.store;

import com.b07.database.helper.DatabaseDeserializeHelper;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseSerializeHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
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
import com.b07.inventory.ItemImpl;
import com.b07.users.Account;
import com.b07.users.AccountImpl;
import com.b07.users.Admin;
import com.b07.users.Customer;
import com.b07.users.Employee;
import com.b07.users.Roles;
import com.b07.users.User;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SalesApplication {
  /**
   * This is the main method to run your entire program! Follow the "Pulling it together"
   * instructions to finish this off.
   * 
   * @param argv used.
   */
  public static void main(String[] argv) {
    Connection connection = DatabaseDriverExtender.connectOrCreateDataBase();
    for (String command : argv) {
      try {
        // TODO Check what is in argv
        // If it is -1
        if (connection == null) {
          System.out.print("NOOO");
        }
        boolean firstRun = true;;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        if (command.equals("-1")) {
          /*
           * TODO This is for the first run only! Add this code:
           * DatabaseDriverExtender.initialize(connection); Then add code to create your first
           * account, an administrator with a password Once this is done, create an employee account
           * as well.
           * 
           */
          if (firstRun) {
            DatabaseDriverExtender.initialize(connection);
            System.out.println("Connection initialized, now create your administrator account:");
            firstRun = false;
          }
          boolean counterOfFirst = false;
          while (!counterOfFirst) {
            counterOfFirst = firstInitiationAdminHelper(bufferedReader);
            if (counterOfFirst) {
              System.out.println("Next part:");
            } else {
              System.out.println("FirstInitiation of admin, plz try again");
            }
          }
          counterOfFirst = false;
          while (!counterOfFirst) {
            counterOfFirst = firstInitiationEmployeeHelper(bufferedReader);
            if (counterOfFirst) {
              System.out.println("Everything is set, session end");
            } else {
              System.out.println("FirstInitiation of employee, plz try again");
            }
          }
        }
        if (command.equals("1")) {
          /*
           * TODO In admin mode, the user must first login with a valid admin account This will
           * allow the user to promote employees to admins. Currently, this is all an admin can do.
           */
          boolean counterOfSecond = false;
          while (!counterOfSecond) {
            System.out.println("In admin mode, please login with an admin acount:");
            System.out.println("In admin mode, please enter your userId");
            String inputedIdString = bufferedReader.readLine();
            System.out.println("In admin mode, please enter your password");
            String inputedPasswordString = bufferedReader.readLine();
            counterOfSecond =
                loginHelper(inputedIdString, inputedPasswordString, Roles.ADMIN.name());
            if (counterOfSecond) {
              String currentAdminSelection = "";
              while (!currentAdminSelection.equals("0")) {
                System.out.println("Here is an operation menu:");
                System.out.println("1 - Promote Employee");
                System.out.println("2 - View ");
                System.out.println("3 - veiw Stock Level");
                System.out.println("4 - add new Item to Inventory");
                System.out.println("5 - display a list of all inactive/active accounts");
                System.out.println("6 - serialize the current database ");
                System.out.println("7 - deserialize the given database and store it in database ");
                System.out.println("0 - Exit");
                System.out.println("Enter your selection");
                currentAdminSelection = bufferedReader.readLine();
                if (currentAdminSelection.equals("1")) {
                  promoteEmployeeHelper(bufferedReader, inputedIdString);
                } else if (currentAdminSelection.equals("2")) {
                  if (viewBooksHelper()) {
                    System.out.println("Completed");
                  } else {
                    System.out.println("Due to some error, it failed");
                  }
                } else if (currentAdminSelection.equals("3")) {
                  if (viewStockLevel()) {
                    System.out.println("Completed");
                  } else {
                    System.out.println("Due to some error, it failed");
                  }
                } else if (currentAdminSelection.equals("4")) {
                  if (addNewItem(bufferedReader)) {
                    System.out.println("Completed");
                  } else {
                    System.out.println("Due to some error, it failed");
                  }
                } else if (currentAdminSelection.equals("5")) {
                  if (displayHelper(bufferedReader)) {
                    System.out.println("Operation Completed");
                  } else {
                    System.out.println("Operation failed");
                  }
                } else if (currentAdminSelection.equals("6")) {
                  if (DatabaseSerializeHelper.serializeHelper()) {
                    System.out.println("Operation Completed");
                  } else {
                    System.out.println("Operation failed");
                  }
                } else if (currentAdminSelection.equals("7")) {
                  if (DatabaseDeserializeHelper.deserializeHelper(connection)) {
                    connection = DatabaseDriverExtender.connectOrCreateDataBase();
                    System.out.println("Operation Completed");
                  } else {
                    System.out.println("Operation failed");
                  }
                }
              }
            }
          }
        }
        if (!(command.equals("1") || command.equals("-1"))) {
          // If anything else - including nothing
          /*
           * TODO Create a context menu, where the user is prompted with: 1 - Employee Login 2 -
           * Customer Login 0 - Exit Enter Selection:
           */
          String currentSelection = "";
          while (!currentSelection.equals("0")) {
            System.out.println("Here is an operation menu:");
            System.out.println("1 - Employee Login");
            System.out.println("2 - Customer Login");
            System.out.println("3 - Register for Customer");
            System.out.println("0 - Exit");
            System.out.println("Enter your selection");
            currentSelection = bufferedReader.readLine();
            if (currentSelection.equals("1")) {

              // If the user entered 1
              /*
               * TODO Create a context menu for the Employee interface Prompt the employee for their
               * id and password Attempt to authenticate them. If the Id is not that of an employee
               * or password is incorrect, end the session. If the Id is an employee, and the
               * password is correct, create an EmployeeInterface object then give them the
               * following options: 1. authenticate new employee 2. Make new User 3. Make new
               * account 4. Make new Employee 5. Restock Inventory 6. Exit
               * 
               * Continue to loop through as appropriate, ending once you get an exit code (6)
               */
              System.out.println("Welcome to Employee Interface");
              System.out.println("Please Login your Employee account:");
              System.out.println("Please enter your employee id");
              String inputedEmployeeIdString = bufferedReader.readLine();
              System.out.println("Please enter your employee password");
              String inputedPasswordString = bufferedReader.readLine();
              if (loginHelper(inputedEmployeeIdString, inputedPasswordString,
                  Roles.EMPLOYEE.name())) {
                Inventory storeInventory = DatabaseSelectHelper.getInventory();
                Employee currentEmployee = (Employee) getUserObjectHelper(inputedEmployeeIdString);
                EmployeeInterface employeeInterface =
                    new EmployeeInterface(currentEmployee, storeInventory);
                String currentEmployeeSelection = "";
                while (!currentEmployeeSelection.equals("7")) {
                  System.out.println("Here is an operation menu for Employee interface:");
                  System.out.println("1. authenticate new employee");
                  System.out.println("2. Make new User");
                  System.out.println("3. Make new account");
                  System.out.println("4. Make new Employee");
                  System.out.println("5. Restock Inventory");
                  System.out.println("6. Approve all accounts");
                  System.out.println("7. Exit");
                  System.out.println("Enter your selection");
                  currentEmployeeSelection = bufferedReader.readLine();
                  if (currentEmployeeSelection.equals("1")) {
                    if (authenticateNewEmployeeHelper(bufferedReader, employeeInterface)) {
                      System.out.println("Operation 1 successed!");
                    } else {
                      System.out.println("Operation 1 failed!");
                    }
                  } else if (currentEmployeeSelection.equals("2")) {
                    if (makeNewCustomerHelper(bufferedReader, employeeInterface)) {
                      System.out.println("Operation 2 successed!");
                    } else {
                      System.out.println("Operation 2 failed!");
                    }
                  } else if (currentEmployeeSelection.equals("3")) {
                    System.out.println(
                        "Please enter the userId of a customer that you want to create an account for: ");
                    String userId = bufferedReader.readLine();
                    if (checkIdIsCustomerHelper(userId)) {
                      if (registerAccountHelper(userId) != null) {
                        System.out.println("Operation 3 successed!");
                      } else {
                        System.out.println("The customer already has an account");
                      }
                    } else {
                      System.out.println("Operation 3 failed!");
                    }
                  } else if (currentEmployeeSelection.equals("4")) {
                    if (makeNewEmployeeHelper(bufferedReader, employeeInterface)) {
                      System.out.println("Operation 4 successed!");
                    } else {
                      System.out.println("Operation 4 failed!");
                    }
                  } else if (currentEmployeeSelection.equals("5")) {
                    if (restockHelper(bufferedReader, employeeInterface)) {
                      System.out.println("Operation 5 successed!");
                    } else {
                      System.out.println("Operation 5 failed!");
                    }
                  } else if (currentEmployeeSelection.equals("6")) {
                    if (approveAllAccount()) {
                      System.out.println("Operation 6 successed!");
                    } else {
                      System.out.println("Operation 6 failed!");
                    }
                  }
                }
              } else {
                System.out.println("Sorry, you entered the wrong password, the session ended");
              }
            } else if (currentSelection.equals("2")) {
              // If the user entered 2
              /*
               * TODO create a context menu for the customer Shopping cart Prompt the customer for
               * their id and password Attempt to authenticate them If the authentication fails or
               * they are not a customer, repeat If they get authenticated and are a customer, give
               * them this menu: 1. List current items in cart 2. Add a quantity of an item to the
               * cart 3. Check total price of items in the cart 4. Remove a quantity of an item from
               * the cart 5. check out 6. Exit
               * 
               * When checking out, be sure to display the customers total, and ask them if they
               * wish to continue shopping for a new order
               * 
               * For each of these, loop through and continue prompting for the information needed
               * Continue showing the context menu, until the user gives a 6 as input.
               */
              // If the user entered 0
              /*
               * TODO Exit condition
               */
              // If the user entered anything else:
              /*
               * TODO Re-prompt the user
               */
              System.out.println("Welcome to customer Shopping cart");
              System.out.println("Please Login your customer account:");
              System.out.println("Please enter your customer id");
              String inputedCustomerIdString = bufferedReader.readLine();
              System.out.println("Please enter your customer password");
              String inputedPasswordString = bufferedReader.readLine();
              if (loginHelper(inputedCustomerIdString, inputedPasswordString,
                  Roles.CUSTOMER.name())) {
                Customer currentCustomer = (Customer) getUserObjectHelper(inputedCustomerIdString);
                ShoppingCart shoppingCart = new ShoppingCart(currentCustomer);
                String currentCustomerSelection = "";
                String activeAccountIdString = null;
                while (!currentCustomerSelection.equals("8")) {
                  System.out.println("Here is an operation menu for Employee interface:");
                  System.out.println("1. List current items in cart ");
                  System.out.println("2. Add a quantity of an item to the cart");
                  System.out.println("3. Check total price of items in the cart");
                  System.out.println("4. Remove a quantity of an item from the cart");
                  System.out.println("5. CheckOut");
                  System.out.println("6. Registger for an account to store current cart");
                  System.out.println("7. Restore previous shopping cart items");
                  System.out.println("8. Exit");
                  System.out.println("Enter your selection");
                  currentCustomerSelection = bufferedReader.readLine();
                  if (currentCustomerSelection.equals("1")) {
                    List<Item> allItem = shoppingCart.getItems();
                    System.out.println("Item in the cart:");
                    HashMap<Item, Integer> itemQuantity = shoppingCart.getInformation();
                    for (Item currentItem : allItem) {
                      System.out.print(currentItem.getName());
                      System.out.println(itemQuantity.get(currentItem));
                    }
                  } else if (currentCustomerSelection.equals("2")) {
                    if (addItemHelper(bufferedReader, shoppingCart)) {
                      System.out.println("Operation 2 successed!");
                    } else {
                      System.out.println("Operation 2 failed!");
                    }
                  } else if (currentCustomerSelection.equals("3")) {
                    BigDecimal totalPrice = shoppingCart.getTotal();
                    System.out.println("Current total of the shoppingCart");
                    System.out.println(String.valueOf(totalPrice));
                  } else if (currentCustomerSelection.equals("4")) {
                    if (removeItemHelper(bufferedReader, shoppingCart)) {
                      System.out.println("Operation 4 successed!");
                    } else {
                      System.out.println("Operation 4 failed!");
                    }

                  } else if (currentCustomerSelection.equals("5")) {
                    if (checkOutHelper(bufferedReader, shoppingCart, activeAccountIdString)) {
                      System.out.println("Operation 5 successed!");
                    } else {
                      System.out.println("Operation 5 failed!");
                    }
                  } else if (currentCustomerSelection.equals("6")) {
                    activeAccountIdString = registerAccountHelper(inputedCustomerIdString);
                    if (activeAccountIdString != null) {
                      if (associateShoppingCartToAccountHelper(activeAccountIdString,
                          shoppingCart)) {
                        System.out.println("Operation 6 successed, currentCart has bee!");
                      } else {
                        System.out.println("Operation 6 failed!");
                      }
                    } else {
                      System.out.println("Operation 6 failed!");
                    }
                  } else if (currentCustomerSelection.equals("7")) {
                    activeAccountIdString = restorePrevShoppingCartHelper(bufferedReader,
                        inputedCustomerIdString, shoppingCart);
                    if (activeAccountIdString != null) {
                      System.out.println("Operation 7 successed!");
                    } else {
                      System.out.println("Operation 7 failed!");
                    }
                  }
                }
              }
            } else if (currentSelection.equals("3")) {
              if (makeNewCustomerHelper(bufferedReader)) {
                System.out.println("Operation successed!");
              } else {
                System.out.println("Operation failed!");
              }
            }
          }

        }
      } catch (SQLException e) {
        // TODO Improve this!
        System.out.println("An SQL error occurs, so it failed");
      } catch (IOException e) {
        // TODO Auto-generated catch block
        System.out.println("An BufferReader error occurs. so it failed");
      } catch (ConnectionFailedException e) {
        // TODO Auto-generated catch block
        System.out.println("Connection of database failed");
      } catch (InvalidIdException e) {
        // TODO Auto-generated catch block
        System.out.println("Invalid inputed id");
      } finally {
        try {
          connection.close();
        } catch (Exception e) {
          System.out.println("Looks like it was closed already :)");
        }
      }
    }
  }

  /**
   * This method is to approve all accounts stored in database.
   * 
   * @return true if this operation success, false otherwise
   */
  private static boolean approveAllAccount() {
    // TODO Auto-generated method stub
    List<Account> userAccounts;
    boolean result = true;
    try {
      userAccounts = DatabaseSelectHelper.getAllAccounts();
      System.out.println(
          "Here is a list of your approve active account id, which one you want to restore?");
      for (Account currentAccount : userAccounts) {
        if (currentAccount.getActive()) {
          if (!currentAccount.getApprove()) {
            int currentaccountId = currentAccount.getId();
            result = result && DatabaseUpdateHelper.updateAccountApprove(currentaccountId, true);
          }
        }
      }
      return result;
    } catch (SQLException | InvalidIdException | InvalidItemException e) {
      // TODO Auto-generated catch block
      return false;
    }
  }

  /**
   * This method is to help asking and inserting new items to database
   * 
   * @param bufferedReader to take input
   * @return true if the operation success, false otherwise
   */
  private static boolean addNewItem(BufferedReader bufferedReader) {
    // TODO Auto-generated method stub
    System.out.println("What kind of items you want to add?");
    String itemName;
    try {
      itemName = bufferedReader.readLine();
      System.out.println("What is the item price?");
      String itemPriceString = bufferedReader.readLine();
      int itemPrices = Integer.parseInt(itemPriceString);
      BigDecimal itemPrice = new BigDecimal(itemPrices);
      DatabaseInsertHelper insertHelper = new DatabaseInsertHelper();
      int itemId = insertHelper.insertItem(itemName, itemPrice);
      System.out.println("Add completed, here is the itemId: " + String.valueOf(itemId));
      return true;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("Some error on the bufferedReader!");
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      System.out.println("Some error on the SQL!");
    } catch (DatabaseInsertException e) {
      // TODO Auto-generated catch block
      System.out.println("Some error on the insertion!");
    } catch (InvalidItemException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid input item data!");
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid input item data!");
    }
    return false;
  }

  /**
   * This method is to display how many each item sold in inventory in database
   * 
   * @return true if this operation success, false otherwise
   */
  private static boolean viewStockLevel() {
    try {
      Inventory currentInventory = DatabaseSelectHelper.getInventory();
      System.out.println("Inventory list :");
      HashMap<Item, Integer> stockLevel = currentInventory.getItemMap();
      for (Item item : stockLevel.keySet()) {
        System.out.print(String.valueOf(item.getId()) + "  ");
        System.out.print(String.valueOf(item.getName()) + "  ");
        System.out.println(String.valueOf(stockLevel.get(item)));
        System.out.println("-------------------------------------");
      }
      return true;
    } catch (SQLException | InvalidIdException e) {
      // TODO Auto-generated catch block
      System.out.println("Some SQL error occur, view failed");
    }
    return false;
  }

  /**
   * This method is to help asking customer to checking out with corresponding shoppingCart.
   * 
   * @param bufferedReader to take input
   * @param shoppingCart the target that needs to be checking out
   * @param activeAccountString the string that represents the accountId
   * @return true if this operation success, false otherwise
   */
  private static boolean checkOutHelper(BufferedReader bufferedReader, ShoppingCart shoppingCart,
      String activeAccountString) {
    BigDecimal totalPrice = shoppingCart.getTotal();
    System.out.println("Here is Check amount:");
    System.out.println("WithoutTax: " + String.valueOf(totalPrice));
    BigDecimal afterTaxAmount = totalPrice.multiply(shoppingCart.getTaxRate());
    System.out.println("WithTax: " + String.valueOf(afterTaxAmount));
    System.out.println("Start checkOut the shoppingCart:");
    System.out.println("Do you wish to continue shopping for a new order? 'Y'"
        + " means yes, otherInput means no");
    try {
      String customerResponse = bufferedReader.readLine();
      if (customerResponse.equals("Y")) {
        if (activeAccountString != null) {
          int accountId = Integer.parseInt(activeAccountString);
          DatabaseUpdateHelper.updateAccountStatus(accountId, false);
        }
        System.out.println("Start checkOut the shoppingCart:");
        boolean checkOut = shoppingCart.checkOut(shoppingCart);
        shoppingCart.clearCart();
        if (!checkOut) {
          System.out.println("Failed to Check out.)");
        } else {
          System.out.println("Check out completely!)");
        }
      } else {
        System.out.println("Continue shopping!(Didn't checkout current)");
      }
      return true;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("BufferedReader failed!");
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      System.out.println("SQL related error failed!");
    } catch (InvalidItemException | InvalidIdException e) {
      // TODO Auto-generated catch block
      System.out.println("Internal errors!");
    }
    return false;
  }

  /**
   * This method is to help asking and removing valid item from the given shoppingCart
   * 
   * @param bufferedReader to take input
   * @param shoppingCart the target need to be remove from
   * @return true if this operation success, false otherwise
   */
  private static boolean removeItemHelper(BufferedReader bufferedReader,
      ShoppingCart shoppingCart) {
    System.out.println("Start remove items from the Cart:");
    System.out.println("Please enter the removeing item's id?");
    try {
      String removeItemIdString = bufferedReader.readLine();
      int removeItemId = Integer.parseInt(removeItemIdString);
      Item itemObjects = new ItemImpl();
      System.out.println("Please enter the qantity of item you want to remove");
      String removeItemIdQuantityString = bufferedReader.readLine();
      int removeItemIdQuantity = Integer.parseInt(removeItemIdQuantityString);
      itemObjects.setId(removeItemId);
      shoppingCart.removeItem(itemObjects, removeItemIdQuantity);
      System.out.println("Removing operation complete");
      return true;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("BufferedReader failed!");
    } catch (InvalidItemException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid qantity!");
    } catch (InvalidIdException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid itemId!");
    }
    return false;
  }

  /**
   * This method is to help asking and putting items to the shopping cart.
   * 
   * @param bufferedReader to take in the input
   * @param shoppingCart to take in the given item by its id
   * @return true if this operation success
   */
  private static boolean addItemHelper(BufferedReader bufferedReader, ShoppingCart shoppingCart) {
    System.out.println("Start add items to the Cart:");
    System.out.println("Please enter the adding item's id?");
    try {
      String itemIdString = bufferedReader.readLine();
      int itemId = Integer.parseInt(itemIdString);
      List<Item> itemList = DatabaseSelectHelper.getAllItems();
      if (itemList.isEmpty()) {
        System.out.println("Such Item does not exist in the Store");
      } else {
        for (Item item : itemList) {
          if (item.getId() == itemId) {
            Item itemObject = DatabaseSelectHelper.getItem(itemId);
            System.out.println("Please enter the qantity of item you want to add");
            String itemIdQuantity = bufferedReader.readLine();
            int itemQuantity = Integer.parseInt(itemIdQuantity);
            shoppingCart.addItem(itemObject, itemQuantity);
            System.out.println("Adding operation complete");
            return true;
          }
        }
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("BufferedReader failed!");
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      System.out.println("Something is wrong with SQL command");
    } catch (InvalidIdException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid inputed Id");
    } catch (InvalidItemException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid quantity");
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid quantity");
    }
    return false;
  }

  /**
   * This method is to help asking and creating new EMPLOYEE object for the first time when system
   * runs. After creating the employee, its correspond accountId will be output.
   * 
   * @param bufferedReader to take input
   * @return true if this operation success,false otherwise
   */
  private static boolean firstInitiationEmployeeHelper(BufferedReader bufferedReader) {
    System.out.println("Now please create your employee acount:");
    System.out.println("Set Employee name, please enter your name");
    System.out.println("Make sure you enter the right format? ie: Firstnamemiddlename Lastname");
    String employeeName;
    try {
      employeeName = bufferedReader.readLine();
      System.out.println("Set Employee age, please enter your age?");
      String employeeAgeString = bufferedReader.readLine();
      int employeeAge = Integer.parseInt(employeeAgeString);
      System.out.println("Set Employee address, please enter your address?");
      String employeeAddress = bufferedReader.readLine();
      System.out.println("Set Employee password, please enter your password?");
      String employeePassword = bufferedReader.readLine();
      int employeeId = DatabaseInsertHelper.insertNewUser(employeeName, employeeAge,
          employeeAddress, employeePassword);
      int employeeRoleId = DatabaseInsertHelper.insertRole(Roles.EMPLOYEE.name());
      DatabaseInsertHelper.insertUserRole(employeeId, employeeRoleId);
      String employeeStringId = String.valueOf(employeeId);
      System.out.println(
          "Employee created, please remember your employee account id:" + employeeStringId);
      return true;
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      System.out.println("Something is wrong with SQL command");
    } catch (DatabaseInsertException e) {
      // TODO Auto-generated catch block
      System.out.println("Failed to insert this info!");
    } catch (InvalidAddressException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid address");
    } catch (InvalidAgeException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid age");
    } catch (InvalidUserNameException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid UserName");
    } catch (InvalidIdException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.out.println("Invalid Id inputed");
    } catch (IOException e) {
      System.out.println("Something is wrong with BufferedReader");
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid Inputed age!");
    } catch (ArrayIndexOutOfBoundsException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid Length of name!");
    }
    return false;
  }

  /**
   * This method is to help asking and create ADMIN for the first time this system runs.
   * 
   * @param bufferedReader to take input
   * @return true if this operation success , false otherwise
   */
  private static boolean firstInitiationAdminHelper(BufferedReader bufferedReader) {
    try {
      System.out.println("Now create your administrator account:");
      System.out.println("Set Administrator name, please enter your name?");
      System.out.println("Make sure you enter the right format? ie: Firstnamemiddlename Lastname");
      String adminName = bufferedReader.readLine();
      System.out.println("Set Administrator age, please enter your age?");
      String adminAgeString = bufferedReader.readLine();
      int adminAge = Integer.parseInt(adminAgeString);
      System.out.println("Set Administrator address, please enter your address?");
      String adminAddress = bufferedReader.readLine();
      System.out.println("Set Administrator password, please enter your password?");
      String adminPassword = bufferedReader.readLine();
      int adminId =
          DatabaseInsertHelper.insertNewUser(adminName, adminAge, adminAddress, adminPassword);
      int adminRoleId = DatabaseInsertHelper.insertRole(Roles.ADMIN.name());
      DatabaseInsertHelper.insertUserRole(adminId, adminRoleId);
      String adminStringId = String.valueOf(adminId);
      System.out.println("Admin created, please remember your admin account id:" + adminStringId);
      System.out.println("Administrator account set!");
      return true;
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      System.out.println("Something is wrong with SQL command");
    } catch (DatabaseInsertException e) {
      // TODO Auto-generated catch block
      System.out.println("Failed to insert this info!");
    } catch (InvalidAddressException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid address");
    } catch (InvalidAgeException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid age");
    } catch (InvalidUserNameException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid UserName");
    } catch (InvalidIdException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.out.println("Invalid Id inputed");
    } catch (IOException e) {
      System.out.println("Something is wrong with BufferedReader");
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid Inputed age!");
    } catch (ArrayIndexOutOfBoundsException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid Length of name!");
    }
    return false;
  }

  /**
   * This method is to help asking and promoting a EMPLOYEE to a ADMIN with a admin's help to
   * database.
   * 
   * @param bufferedReader to take input
   * @param adminId the string that represents the ADMIN's id
   * @return true if this operation success, false otherwise
   */
  private static boolean promoteEmployeeHelper(BufferedReader bufferedReader, String adminId) {
    boolean promoted;
    System.out.println("In admin mode, Please enter the id of the employee you want to promote?");
    try {
      String inputEmployeeIdString = bufferedReader.readLine();
      int inputEmployeeId = Integer.parseInt(inputEmployeeIdString);
      int roleIdOfEmployee = DatabaseSelectHelper.getUserRoleId(inputEmployeeId);
      String roleName = DatabaseSelectHelper.getRoleName(roleIdOfEmployee);
      if (roleName.equals(Roles.EMPLOYEE.name())) {
        User promoteEmployee = DatabaseSelectHelper.getUserDetails(inputEmployeeId);
        User currentAdmin = getUserObjectHelper(adminId);
        promoted = ((Admin) currentAdmin).promoteEmployee((Employee) promoteEmployee);
        if (!promoted) {
          System.out.println("We have fail to promote the given employee to Admin.");
        } else {
          System.out.println("We have promoted the employee.");
        }
        return true;
      } else {
        System.out.println("Wrong Employee id");
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("Something is wrong with BufferedReader!");
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      System.out.println("Something is wrong with SQL!");
    } catch (InvalidIdException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid Id ");;
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid Inputed id!");
    }
    return false;

  }

  /**
   * This method is to help asking and authenticating the new employee, in order to authenticate a
   * new one, it has to be with another employee help.
   * 
   * @param bufferedReader to take input
   * @param employeeInterface to take in the given employee
   * @return true if this operation success, false otherwise
   */
  private static boolean authenticateNewEmployeeHelper(BufferedReader bufferedReader,
      EmployeeInterface employeeInterface) {
    System.out.println("To authenticate the current EmployeeInterface to another Employee:");
    System.out.println("Please login another Employee:");
    System.out.println("Please enter your employee id");
    try {
      String inputeEmployeeIdString = bufferedReader.readLine();
      System.out.println("Please enter your employee password");
      String inputeEmployeePasswordString = bufferedReader.readLine();
      if (loginHelper(inputeEmployeeIdString, inputeEmployeePasswordString,
          Roles.EMPLOYEE.name())) {
        Employee anotherEmployee = (Employee) getUserObjectHelper(inputeEmployeeIdString);
        employeeInterface.setCurrentEmployee(anotherEmployee);
        System.out.println("Authentication Completed");
        return true;
      } else {
        System.out.println("Sorry, you entered the wrong id or password, the operation failed");
        return true;
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("Something is wrong with BufferedReader!");
    }
    return false;
  }

  /**
   * This method is to help asking creating and inserting a new CUSTOMER with one employee's help to
   * database
   * 
   * @param bufferedReader to take input
   * @param employeeInterface to help creating the customer object
   * @return true if this operation success, false otherwise
   */
  private static boolean makeNewCustomerHelper(BufferedReader bufferedReader,
      EmployeeInterface employeeInterface) {
    System.out.println("Set Customer name, please enter your name?");
    System.out.println("Make sure you enter the right format? ie: Firstnamemiddlename Lastname");
    try {
      String customerName = bufferedReader.readLine();
      System.out.println("Set Customer age, please enter your age?");
      String customerAgeString = bufferedReader.readLine();
      int customerAge = Integer.parseInt(customerAgeString);
      System.out.println("Set Customer address, please enter your address?");
      String customerAddress = bufferedReader.readLine();
      System.out.println("Set Customer password, please enter your password?");
      String customerPassword = bufferedReader.readLine();
      int customerId = employeeInterface.createCustomer(customerName, customerAge, customerAddress,
          customerPassword);
      System.out.println("Set completed, here is your customer Id:" + String.valueOf(customerId));
      return true;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("Something is wrong with BufferedReader!");
    } catch (InvalidIdException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid id!");
    } catch (DatabaseInsertException e) {
      // TODO Auto-generated catch block
      System.out.println("Failed to insert data!");
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      System.out.println("Something is wrong with SQL command!");
    } catch (InvalidAddressException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid address!");
    } catch (InvalidAgeException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid age!");
    } catch (InvalidUserNameException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid Inputed name!");
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid Inputed age!");
    } catch (ArrayIndexOutOfBoundsException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid Length of name!");
    }
    return false;
  }

  /**
   * This method is to help asking and restock one type of Item that is in the inventory in database
   * 
   * @param bufferedReader to take input
   * @param employeeInterface the employee to help make this operation
   * @return true if this operation success, false otherwise
   */
  private static boolean restockHelper(BufferedReader bufferedReader,
      EmployeeInterface employeeInterface) {
    System.out.println("Start restock Inventory");
    boolean result = false;
    try {
      System.out.println("Please enter the itemId that you want to restock?");
      String existItemIdString = bufferedReader.readLine();
      int existItemId = Integer.parseInt(existItemIdString);
      Item existItem = DatabaseSelectHelper.getItem(existItemId);
      System.out.println("How many item you want to put in Inventory?");
      String itemQuantityString = bufferedReader.readLine();
      int itemQuantity = Integer.parseInt(itemQuantityString);
      result = employeeInterface.restockInventory(existItem, itemQuantity);
      if (result) {
        System.out.println("Restock Completed with itemid:" + existItemIdString);
      } else {
        System.out.println("Failed Restock Completed with itemid:" + existItemIdString);
      }
      return result;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("Something is wrong with BufferedReader!");
    } catch (InvalidItemException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid item info inputed!");
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      System.out.println("Something is wrong with SQL command!");
    } catch (InvalidIdException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid id inputed");
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid price inputed");
    }
    return false;
  }

  /**
   * This method is to help asking creating and inserting a new EMPLOYEE with one employee's help to
   * database
   * 
   * @param bufferedReader to take input
   * @param employeeInterface to help creating the customer object
   * @return true if this operation success, false otherwise
   */
  private static boolean makeNewEmployeeHelper(BufferedReader bufferedReader,
      EmployeeInterface employeeInterface) {
    System.out.println("Set Employee name, please enter your name?");
    System.out.println("Make sure you enter the right format? ie: Firstnamemiddlename Lastname");
    try {
      String employeeName = bufferedReader.readLine();
      System.out.println("Set Employee age, please enter your age?");
      String employeeAgeString = bufferedReader.readLine();
      int employeeAge = Integer.parseInt(employeeAgeString);
      System.out.println("Set Employee address, please enter your address?");
      String employeeAddress = bufferedReader.readLine();
      System.out.println("Set Employee password, please enter your password?");
      String employeePassword = bufferedReader.readLine();
      int employeeSetId = employeeInterface.createEmployee(employeeName, employeeAge,
          employeeAddress, employeePassword);
      System.out
          .println("Set completed, here is your Employee Id:" + String.valueOf(employeeSetId));
      return true;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("Something is wrong with BufferedReader!");
    } catch (InvalidIdException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid inputed Id!");
    } catch (DatabaseInsertException e) {
      // TODO Auto-generated catch block
      System.out.println("Failed to insert data!");
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      System.out.println("Something is wrong with SQL command!");
    } catch (InvalidAddressException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid address inputed!");
    } catch (InvalidAgeException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid age inputed!");
    } catch (InvalidUserNameException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid name inputed!");
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid name inputed!");
    }
    return false;
  }

  /**
   * This method is to display each purchases from every CUSTOMER stored in database with
   * corresponding total purchase prices and also the salelog which contains how many quantity are
   * each Item is sold with corresponding name.
   * 
   * @return true if this operation success, false otherwise
   */
  private static boolean viewBooksHelper() {
    SalesLog saleLog;
    try {
      saleLog = DatabaseSelectHelper.getSales();
    } catch (InvalidIdException | SQLException e) {
      e.printStackTrace();
      return false;
    }
    ArrayList<Sale> saleList = (ArrayList<Sale>) saleLog.getSaleList();
    HashMap<Item, Integer> allItemQuantity = new HashMap<Item, Integer>();
    for (Sale currentSale : saleList) {
      ArrayList<Item> item = new ArrayList<Item>(currentSale.getItemMap().keySet());
      for (Item currentItem : item) {
        allItemQuantity.put(currentItem, 0);
      }
    }
    for (Sale currentSale : saleList) {
      System.out.println("Customer: " + currentSale.getUser().getName());
      System.out.println("Purchase Number: " + String.valueOf(currentSale.getId()));
      System.out.println("Total Purchase Price: " + String.valueOf(currentSale.getTotalPrice()));
      HashMap<Item, Integer> itemQuantity = currentSale.getItemMap();
      ArrayList<Item> item = new ArrayList<Item>(currentSale.getItemMap().keySet());
      boolean firstLine = true;
      for (Item currentItem : item) {
        if (firstLine) {
          System.out.print("Itemized Breakdown: ");
          firstLine = false;
        }
        System.out
            .println(currentItem.getName() + ": " + String.valueOf(itemQuantity.get(currentItem)));
        int itemsQuantity = allItemQuantity.get(currentItem) + itemQuantity.get(currentItem);
        allItemQuantity.put(currentItem, itemsQuantity);
      }
      System.out.println("----------------------------------------------------------------------");
    }
    ArrayList<Item> itemLists = new ArrayList<Item>(allItemQuantity.keySet());
    for (Item item : itemLists) {
      System.out.println("Number " + item.getName() + "Sold: " + allItemQuantity.get(item));
    }
    System.out.println(saleLog.getTotalSales());
    return true;
  }

  /**
   * This method is to help asking and authenticating the given user by taking userId and
   * corresponding accountRole.
   * 
   * @param userId the String that represents the userId
   * @param userPassword the String that represents its password
   * @param accountRole the String that presents its role
   * @return true if this operation success, false otherwise
   */
  private static boolean loginHelper(String userId, String userPassword, String accountRole) {
    try {
      int inputedId = Integer.parseInt(userId);
      int roleOfId = DatabaseSelectHelper.getUserRoleId(inputedId);
      String roleNameOfId = DatabaseSelectHelper.getRoleName(roleOfId);
      if (roleNameOfId.equals(accountRole)) {
        User currentAdmin = DatabaseSelectHelper.getUserDetails(inputedId);
        if (currentAdmin.authenticate(userPassword)) {
          return true;
        } else {
          System.out.println("Invalid password!");
          return false;
        }
      } else {
        System.out.println("Invalid Id!");
        return false;
      }
    } catch (SQLException | InvalidIdException | NumberFormatException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid User Id input");
      return false;
    }
  }

  /**
   * This method is to get the User object base on give userId.
   * 
   * @param userId the string represents the userId
   * @return User if there is such userId matches to it
   */
  private static User getUserObjectHelper(String userId) {
    try {
      int inputedId = Integer.parseInt(userId);
      User currentAdmin = DatabaseSelectHelper.getUserDetails(inputedId);
      return currentAdmin;
    } catch (SQLException | InvalidIdException e) {
      return null;
    }
  }

  /**
   * This method is to help asking and creating an Account for the given customer
   * 
   * @param inputedCustomerIdString the string that represents the customer's Id
   * @return a string that represents the accountId for this customer if this operation success,
   *         null otherwise
   */
  private static String registerAccountHelper(String inputedCustomerIdString) {
    try {
      int inputedCustomerId = Integer.parseInt(inputedCustomerIdString);
      int accountId = DatabaseInsertHelper.insertAccount(inputedCustomerId, true, false);
      String accountIdString = String.valueOf(accountId);
      System.out.println("You new account id " + accountIdString);
      return accountIdString;
    } catch (SQLException | DatabaseInsertException | InvalidIdException e) {
      return null;
    } catch (NumberFormatException e) {
      System.out.println("Invalid Input id");
      return null;
    }
  }

  /**
   * This method is to make relation between accoutId to given shoppingCart.
   * 
   * @param accountIdString the string represents the accountId
   * @param shoppingCart the target to be connecting with
   * @return true if this operation success, false otherwise
   */
  private static boolean associateShoppingCartToAccountHelper(String accountIdString,
      ShoppingCart shoppingCart) {
    try {
      int accountId = Integer.parseInt(accountIdString);
      Account account = DatabaseSelectHelper.getAccountDetails(accountId);
      account.setShoppingCart(shoppingCart);
      List<Item> itemsInShoppingCart = shoppingCart.getItems();

      for (Item item : itemsInShoppingCart) {
        int itemId = item.getId();
        int quantity = shoppingCart.getItemQuantity(item);
        DatabaseInsertHelper.insertAccountLine(accountId, itemId, quantity);
      }
      return true;
    } catch (SQLException | InvalidIdException | DatabaseInsertException | InvalidItemException e) {
      return false;
    }
  }

  private static String restorePrevShoppingCartHelper(BufferedReader bufferedReader,
      String inputedCustomerIdString, ShoppingCart shoppingCart) {
    try {
      int inputedCustomerId = Integer.parseInt(inputedCustomerIdString);
      List<Account> userAccounts = DatabaseSelectHelper.getUserApproveAccounts(inputedCustomerId);
      System.out.println(
          "Here is a list of your approve active account id, which one you want to restore?");
      for (Account currentAccount : userAccounts) {
          int currentaccountId = currentAccount.getId();
          System.out.println(currentaccountId);
        
      }
      String inputResponse = bufferedReader.readLine();
      int counter = 0;
      Account activeAccount = null;
      while (counter < userAccounts.size()) {
        Account possibleActiveAccount = userAccounts.get(counter);
        if (possibleActiveAccount.getId() == Integer.parseInt(inputResponse)) {
          activeAccount = possibleActiveAccount;
          counter = userAccounts.size();
        }
        counter = counter + 1;
      }
      if (activeAccount != null) {
        shoppingCart.clearCart();
        ShoppingCart prevShoppingCart = activeAccount.getShoppingCart();
        List<Item> prevItems = prevShoppingCart.getItems();
        for (Item item : prevItems) {
          int prevQuantity = prevShoppingCart.getItemQuantity(item);
          shoppingCart.addItem(item, prevQuantity);
        }
        return String.valueOf(activeAccount.getId());
      } else {
        System.out.println("Invalid inputed account id!");
      }
    } catch (SQLException | InvalidIdException | InvalidItemException e) {
      System.out.println("Due to some internal error, it failed.");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("Something is wrong with bufferedReader.");
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid name inputed!");
    }
    return null;
  }

  /**
   * This method is to validate whether the given userId is a customer.
   * 
   * @param userIdString the string represents the user'id
   * @return true if the given userId is a customer, false otherwise
   */
  private static boolean checkIdIsCustomerHelper(String userIdString) {
    try {
      int userId = Integer.parseInt(userIdString);
      int roleId = DatabaseSelectHelper.getUserRoleId(userId);
      String roleName = DatabaseSelectHelper.getRoleName(roleId);
      if (roleName.equals(Roles.CUSTOMER.toString())) {
        return true;
      } else {
        return false;
      }
    } catch (SQLException | InvalidIdException e) {
      return false;
    } catch (NumberFormatException e) {
      System.out.println("Invalid Input id");
      return false;
    }
  }

  /**
   * This method is to help asking and display active accountId and inactive accountId.
   * 
   * @param bufferedReader to take input
   * @return true if this operation success, false otherwise
   */
  private static boolean displayHelper(BufferedReader bufferedReader) {
    System.out.println("Please enter the Customer Id that you want to display?");
    String userIdString;
    try {
      userIdString = bufferedReader.readLine();
      if (checkIdIsCustomerHelper(userIdString)) {
        int userId = Integer.parseInt(userIdString);
        List<Account> listOfAccount;
        listOfAccount = DatabaseSelectHelper.getUserActiveAccounts(userId);
        System.out.println("Here is a list of active account of customer id: " + userIdString);
        int count = 1;
        for (Account account : listOfAccount) {
          System.out.println(String.valueOf(count) + String.valueOf(account.getId()));
          count = count + 1;
        }
        count = 1;
        List<Account> listOfInactiveAccount = DatabaseSelectHelper.getUserInactiveAccounts(userId);
        System.out.println("Here is a list of inactive account of customer id: " + userIdString);
        for (Account account : listOfInactiveAccount) {
          System.out.println(String.valueOf(count) + String.valueOf(account.getId()));
          count = count + 1;
        }
        return true;
      } else {
        System.out.println("Invalid Id inputed");
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("Somethign is wrong with the bufferedreader");
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      System.out.println("Somethign is wrong with the SQL");
    } catch (InvalidIdException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid Id inputed");
    } catch (InvalidItemException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid item info");
    }
    return false;
  }

  /**
   * This method is to help asking, creating and inserting new CUSTOMER to database.
   * 
   * @param bufferedReader to take input
   * @return true if this operation success, false otherwise
   */
  private static boolean makeNewCustomerHelper(BufferedReader bufferedReader) {
    System.out.println("Set Customer name, please enter your name?");
    System.out.println("Make sure you enter the right format? ie: Firstnamemiddlename Lastname");
    try {
      String customerName = bufferedReader.readLine();
      System.out.println("Set Customer age, please enter your age?");
      String customerAgeString = bufferedReader.readLine();
      int customerAge = Integer.parseInt(customerAgeString);
      System.out.println("Set Customer address, please enter your address?");
      String customerAddress = bufferedReader.readLine();
      System.out.println("Set Customer password, please enter your password?");
      String customerPassword = bufferedReader.readLine();
      int customerId = EmployeeInterface.createCustomer(customerName, customerAge, customerAddress,
          customerPassword);
      System.out.println("Set completed, here is your customer Id:" + String.valueOf(customerId));
      return true;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("Something is wrong with BufferedReader!");
    } catch (InvalidIdException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid id!");
    } catch (DatabaseInsertException e) {
      // TODO Auto-generated catch block
      System.out.println("Failed to insert data!");
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      System.out.println("Something is wrong with SQL command!");
    } catch (InvalidAddressException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid address!");
    } catch (InvalidAgeException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid age!");
    } catch (InvalidUserNameException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid Inputed name!");
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid Inputed age!");
    } catch (ArrayIndexOutOfBoundsException e) {
      // TODO Auto-generated catch block
      System.out.println("Invalid Length of name!");
    }
    return false;
  }
}

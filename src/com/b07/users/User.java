    package com.b07.users;

import java.sql.SQLException;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.InvalidIdException;
import com.b07.security.PasswordHelpers;

public abstract class User {
  // TODO: Complete this class based on UML provided on the assignment sheet.
  private transient int id;
  private transient String name;
  private transient int age;
  private transient String address;
  private transient int roleId;
  private transient boolean authenticated;

  public String getAddress() {
    return this.address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public int getRoleId() {
    return roleId;
  }

  /**
   * This authenticate method will take inputed password and compare it to the password if this user
   * from database.
   * 
   * @param password A String that user inputted
   * @return true if the password are right
   * @throws SQLException throw if sql fail to write and read
   * @throws InvalidUserIdException throw if userId is not in the database
   * @throws InvalidRoleIdException throw if the userRole is not in the database
   */
  public final boolean authenticate(String password) throws SQLException, InvalidIdException {
    String usersPassWord = DatabaseSelectHelper.getPassword(this.getId());
    boolean judge = PasswordHelpers.comparePassword(usersPassWord, password);
    return judge;
  }
}

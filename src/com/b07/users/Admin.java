package com.b07.users;

import java.io.Serializable;
import java.sql.SQLException;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.InvalidIdException;

public class Admin extends User implements Serializable {
  private static final long serialVersionUID = -6155729456551227298L;
  private int id;
  private String name;
  private int age;
  private String address;
  private int roleId;
  private transient boolean authenticated;

  /**
   * Create a Admin instance with related info define.
   * 
   * @param id userid of the user
   * @param name name of the user
   * @param age age of the user
   * @param address address of the user
   */
  public Admin(int id, String name, int age, String address) {
    super.setId(id);
    this.id = id;
    super.setAge(age);
    this.age = age;
    super.setName(name);
    this.name = name;
    super.setAddress(address);
    this.address = address;
  }

  /**
   * Create a Admin instance with related info define.
   * 
   * @param id userid of the user
   * @param name name of the user
   * @param age age of the user
   * @param address address of the user
   * @param authenticated the boolean that shows user are logined
   */
  public Admin(int id, String name, int age, String address, boolean authenticated) {
    super.setId(id);
    this.id = id;
    super.setAge(age);
    this.age = age;
    super.setName(name);
    this.name = name;
    super.setAddress(address);
    this.address = address;
    this.authenticated = authenticated;
  }

  /**
   * This method promote the Employee into admin, through reset its role name.
   * 
   * @param employee an inputed employee that will be destory its existen as employee in database
   * @return true of the promote is done, false otherwis
   */
  public boolean promoteEmployee(Employee employee) {
    boolean promoted = true;
    boolean prePromoted;
    try {
      int employeeRoleId = DatabaseSelectHelper.getUserRoleId(employee.getId());
      prePromoted = DatabaseUpdateHelper.updateRoleName(Roles.ADMIN.name(), employeeRoleId);
      if (prePromoted) {
        promoted = DatabaseUpdateHelper.updateUserRole(employeeRoleId, employee.getId());
      } else {
        promoted = false;
      }
    } catch (SQLException | InvalidIdException e) {
      // TODO Auto-generated catch block
      promoted = false;
    }

    return promoted;
  }

  @Override
  public String getAddress() {
    return this.address;
  }

  @Override
  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int getAge() {
    return age;
  }

  @Override
  public void setAge(int age) {
    this.age = age;
  }

  @Override
  public int getRoleId() {
    return this.roleId;
  }
  
  @Override
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
}

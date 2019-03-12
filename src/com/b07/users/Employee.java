package com.b07.users;

import java.io.Serializable;

public class Employee extends User implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 2302470981911788569L;
  private int id;
  private String name;
  private int age;
  private String address;
  private int roleId;
  private transient boolean authenticated;

  /**
   * Create a Employee instance with related info define.
   * 
   * @param id userid of the user
   * @param name name of the user
   * @param age age of the user
   * @param address address of the user
   */
  public Employee(int id, String name, int age, String address) {
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
   * Create a Employee instance with related info define.
   * 
   * @param id userid of the user
   * @param name name of the user
   * @param age age of the user
   * @param address address of the user
   * @param authenticated the boolean that shows user are logined
   */
  public Employee(int id, String name, int age, String address, boolean authenticated) {
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

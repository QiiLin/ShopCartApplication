package com.b07.database.helper;

import com.b07.database.DatabaseDriver;
import com.b07.exceptions.ConnectionFailedException;
import java.sql.Connection;


public class DatabaseDriverHelper extends DatabaseDriver {

  protected static Connection connectOrCreateDataBase() {
    return DatabaseDriver.connectOrCreateDataBase();
  }
  
  protected static Connection reInitialize() throws ConnectionFailedException {
    return DatabaseDriver.reInitialize();
  }
}

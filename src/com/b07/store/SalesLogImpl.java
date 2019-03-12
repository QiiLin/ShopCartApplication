package com.b07.store;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import com.b07.inventory.Item;

public class SalesLogImpl implements SalesLog{
  
  private HashMap<Sale, BigDecimal> salesPriceMap;
  private int numberOfSales;
  private HashMap<Sale, HashMap<Item, Integer>> salesItemMap;
  private BigDecimal totalSale;
  private List<Sale> salesList;
  
  @Override
  public HashMap<Sale, BigDecimal> getSalePriceMap() {
    // TODO Auto-generated method stub
    return this.salesPriceMap;
  }
  
  @Override
  public void setSalePriceMap(HashMap<Sale, BigDecimal> saleMap) {
    // TODO Auto-generated method stub
    this.salesPriceMap = saleMap;
    
  }
  
  @Override
  public void updateSalePriceMap(Sale sale, BigDecimal price) {
    // TODO Auto-generated method stub
    this.salesPriceMap.put(sale, price);
  }
  
  @Override
  public BigDecimal getTotalSales() {
    // TODO Auto-generated method stub
    return this.totalSale;
  }
  
  @Override
  public void setTotalSales(BigDecimal total) {
    // TODO Auto-generated method stub
    this.totalSale = total;
  }

  @Override
  public HashMap<Sale, HashMap<Item, Integer>> getSaleItemMap() {
    // TODO Auto-generated method stub
    return this.salesItemMap;
  }

  @Override
  public void setSaleItemMap(HashMap<Sale, HashMap<Item, Integer>> saleItemsMap) {
    // TODO Auto-generated method stub
    this.salesItemMap = saleItemsMap;
  }

  @Override
  public void updateSaleItemMap(Sale sale, HashMap<Item, Integer> saleItem) {
    // TODO Auto-generated method stub
    salesItemMap.put(sale, saleItem);
  }

  @Override
  public void setTotalNumberOfSale(int totalNumber) {
    // TODO Auto-generated method stub
    this.numberOfSales = totalNumber;
  }

  @Override
  public int getTotalNumberOfSale() {
    // TODO Auto-generated method stub
    return this.numberOfSales;
  }

  @Override
  public List<Sale> getSaleList() {
    // TODO Auto-generated method stub
    return this.salesList;
  }

  @Override
  public void setSaleList(List<Sale> saleList) {
    // TODO Auto-generated method stub
    this.salesList = saleList;
  }


}

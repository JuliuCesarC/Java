package com.cesar.aula03.account;

import java.util.*;

public class CreditCard {
  private double cardLimit;
  private double balance;
  private Map<String, Double> purchases = new HashMap<>();

  public CreditCard(double cardLimit) {
    this.cardLimit = cardLimit;
    this.balance = cardLimit;
  }

  public double getCardLimit() {
    return cardLimit;
  }

  public double getBalance() {
    return balance;
  }

  public Map getPurchases() {
    return purchases;
  }
  
  public Map getSortedPurchases() {
    List<Map.Entry<String, Double>> listSorting = new ArrayList<>(purchases.entrySet());

    Collections.sort(listSorting, Comparator.comparingDouble(Map.Entry::getValue));

    Map<String, Double> sortedMap = new LinkedHashMap<>();
    for (Map.Entry<String, Double> item : listSorting) {
      sortedMap.put(item.getKey(), item.getValue());
    }
    
    return sortedMap;
  }
  
  public boolean buy(String purchase, double value) {
    if(value > balance){
      System.out.println("\nCompra não aprovada. Limite do cartão excedido.");
      return false;
    } else {
      purchases.put(purchase, value);
      balance -= value;
      System.out.println("Compra efetuada com sucesso.");
    return true;
    }
  }
}

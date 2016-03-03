package com.github.sormuras.stash.benchmark.bank.stash;

import java.util.ArrayList;

public class Account {

  private int balance = 0;
  private ArrayList<Integer> transferValues = new ArrayList<Integer>();

  public Integer getBalance() {
    return balance;
  }

  public ArrayList<Integer> getTransferValues() {
    return transferValues;
  }

  public void transfer(int value) {
    transferValues.add(value);
    balance += value;
  }

}

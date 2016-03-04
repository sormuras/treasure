package com.github.sormuras.stash.benchmark.bank.stash;

import java.util.LinkedHashMap;
import java.util.Map;

import com.github.sormuras.stash.benchmark.bank.Status;

public class DefaultBank implements Bank {

  private static final long serialVersionUID = -452262500786749842L;

  private final Map<Integer, Account> accounts = new LinkedHashMap<>();

  @Override
  public Integer[] create(Integer numberOfAccounts) {
    for (int i = 0; i < numberOfAccounts; i++) {
      accounts.put(Integer.valueOf(i), new Account());
    }
    return accounts.keySet().toArray(new Integer[numberOfAccounts]);
  }

  @Override
  public Status status(Integer id) {
    Account account = accounts.get(id);
    return new Status(account.getBalance(), account.getTransferValues());
  }

  @Override
  public void transfer(Integer from, Integer to, int amount) {
    accounts.get(from).transfer(-amount);
    accounts.get(to).transfer(amount);
  }

}

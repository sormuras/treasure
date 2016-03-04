package com.github.sormuras.stash.benchmark.bank.prevayler;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.prevayler.Query;

import com.github.sormuras.stash.benchmark.bank.Status;

public final class GetAccountStatusQuery implements Query<Map<Integer, PrevaylerAccount>, Status> {

  private static final long serialVersionUID = 1L;

  private final Integer id;

  public GetAccountStatusQuery(Integer id) {
    this.id = id;
  }

  @Override
  public Status query(Map<Integer, PrevaylerAccount> accounts, Date time) {
    PrevaylerAccount account = accounts.get(id);
    ArrayList<Integer> transferValues = account.getTransferValues();
    return new Status(account.getBalance(), toArray(transferValues));
  }

  private int[] toArray(ArrayList<Integer> transferValues) {
    int[] array = new int[transferValues.size()];
    for (int i = 0; i < transferValues.size(); i++)
      array[i] = transferValues.get(i);
    return array;
  }

}

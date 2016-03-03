package com.github.sormuras.stash.benchmark.bank;

import java.io.Serializable;
import java.util.List;

public class Status implements Serializable {

  private static final long serialVersionUID = 1L;

  public final int balance;

  public final int[] transferredAmounts;

  public Status(int balance, int[] transferredAmounts) {
    this.balance = balance;
    this.transferredAmounts = transferredAmounts;
  }

  public Status(int balance, List<Integer> transferredAmounts) {
    this(balance, toArray(transferredAmounts));
  }

  private static int[] toArray(List<Integer> values) {
    int[] array = new int[values.size()];
    for (int i = 0; i < values.size(); i++) {
      array[i] = values.get(i);
    }
    return array;
  }
}

package com.github.sormuras.stash.benchmark.bank.prevayler;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Map;

import org.prevayler.Transaction;

public final class TransferTransaction implements Transaction<Map<Integer, PrevaylerAccount>> {

  private static final long serialVersionUID = 1L;

  final Integer from;
  final Integer to;
  final int amount;

  public TransferTransaction(ByteBuffer source) {
    this.from = Integer.valueOf(source.getInt());
    this.to = Integer.valueOf(source.getInt());
    this.amount = source.getInt();
  }

  public TransferTransaction(Integer from, Integer to, int amount) {
    this.from = from;
    this.to = to;
    this.amount = amount;
  }

  @Override
  public void executeOn(Map<Integer, PrevaylerAccount> accounts, Date time) {
    accounts.get(from).transfer(-amount);
    accounts.get(to).transfer(amount);
  }

}

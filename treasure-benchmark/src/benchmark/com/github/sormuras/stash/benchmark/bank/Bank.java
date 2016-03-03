package com.github.sormuras.stash.benchmark.bank;

import java.io.Serializable;

public interface Bank<A> extends Serializable, AutoCloseable {

  void close();

  A[] createAccounts(Integer numberOfAccounts);

  Status getAccountStatus(A id);

  void transfer(A from, A to, int amount);

}

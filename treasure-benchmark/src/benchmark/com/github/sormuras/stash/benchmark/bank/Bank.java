package com.github.sormuras.stash.benchmark.bank;

import java.io.Serializable;

public interface Bank<A> extends Serializable {

  A[] create(Integer numberOfAccounts);

  Status status(A id);

  void transfer(A from, A to, int amount);

}

package com.github.sormuras.stash.benchmark.bank.stash;

import com.github.sormuras.stash.Stash;
import com.github.sormuras.stash.benchmark.bank.Status;

@Stash.Interface(verify = true)
public interface Bank extends com.github.sormuras.stash.benchmark.bank.Bank<Integer> {

  @Stash.Volatile(direct = true)
  void close();

  Integer[] createAccounts(Integer numberOfAccounts);

  @Stash.Volatile
  Status getAccountStatus(Integer id);

  void transfer(Integer from, Integer to, int amount);
}

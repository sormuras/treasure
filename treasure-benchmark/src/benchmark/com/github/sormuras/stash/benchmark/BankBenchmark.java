package com.github.sormuras.stash.benchmark;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import com.github.sormuras.stash.Treasure;
import com.github.sormuras.stash.benchmark.bank.stash.Bank;
import com.github.sormuras.stash.benchmark.bank.stash.BankLock;
import com.github.sormuras.stash.benchmark.bank.stash.BankStash;
import com.github.sormuras.stash.benchmark.bank.stash.DefaultBank;

@State(Scope.Benchmark)
public class BankBenchmark {

  @State(Scope.Thread)
  public static class Coin {
    final Random random = new Random();

    int any() {
      return random.nextInt(NUMBER_OF_ACCOUNTS);
    }

    boolean flip(int chance) {
      return random.nextInt(100) < chance;
    }
  }

  public static final int NUMBER_OF_ACCOUNTS = 100;
  public static final long JOURNAL_SIZE = 100 * 1024 * 1024;

  public static void main(String... args) throws Exception {
    for (int threads = 1; threads <= 4; threads++) {
      Options opts = new OptionsBuilder()
          .include(BankBenchmark.class.getSimpleName())
          .warmupIterations(3)
          .measurementIterations(3)
          .measurementTime(TimeValue.seconds(3))
          .threads(threads)
          .forks(1)
          .build();
      new Runner(opts).run();
    }
  }

  private Bank bank;
  private Path temp;

  @Setup(Level.Iteration)
  public void before(BenchmarkParams params) throws Exception {
    long size = JOURNAL_SIZE;
    if (params.getBenchmark().endsWith("BankBenchmark.transfer")) {
      size = 10 * JOURNAL_SIZE;
    }
    temp = Files.createTempDirectory(params.getBenchmark() + "-" + params.getThreads() + "-");
    bank = new BankStash(Treasure.create(new DefaultBank(), temp, size));
    if (params.getThreads() > 1) {
      bank = new BankLock(bank);
    }
    bank.create(NUMBER_OF_ACCOUNTS);
  }

  @Benchmark
  public void mixed_10(Coin coin) {
    if (coin.flip(10)) {
      bank.transfer(coin.any(), coin.any(), coin.any());
    } else {
      bank.status(coin.any());
    }
  }

  @Benchmark
  public void mixed_20(Coin coin) {
    if (coin.flip(20)) {
      bank.transfer(coin.any(), coin.any(), coin.any());
    } else {
      bank.status(coin.any());
    }
  }

  @Benchmark
  public void mixed_50(Coin coin) {
    if (coin.flip(50)) {
      bank.transfer(coin.any(), coin.any(), coin.any());
    } else {
      bank.status(coin.any());
    }
  }

  @Benchmark
  public void readonly(Coin coin) {
    bank.status(coin.any());
  }

  @Benchmark
  public void transfer(Coin coin) {
    bank.transfer(coin.any(), coin.any(), coin.any());
  }
}

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

    boolean flip() {
      return random.nextInt(100) < 20;
    }
  }

  public static final int CHANCE_OF_TRANSER = 20; // 0..100
  public static final int NUMBER_OF_ACCOUNTS = 100;

  public static void main(String... args) throws Exception {
    // Runtime.getRuntime().availableProcessors()
    for (int threads = 1; threads <= 2; threads++) {
      Options opts = new OptionsBuilder()
          .include(BankBenchmark.class.getSimpleName())
          .warmupIterations(3)
          .measurementIterations(3)
          .measurementTime(TimeValue.seconds(10))
          .threads(threads)
          .forks(1)
          .build();
      new Runner(opts).run();
    }
  }

  private Bank bank;
  private Path temp;

  @Setup(Level.Iteration)
  public void setup(BenchmarkParams params) throws Exception {
    temp = Files.createTempDirectory("BankBench-" + params.getThreads() + "-");
    bank = new BankStash(Treasure.create(new DefaultBank(), temp, 100 * 1024 * 1024));
    if (params.getThreads() > 1) {
      bank = new BankLock(bank);
    }
    bank.createAccounts(NUMBER_OF_ACCOUNTS);
  }

  @Benchmark
  public void stash(Coin coin) {
    if (coin.flip()) {
      bank.transfer(coin.any(), coin.any(), coin.any());
    } else {
      bank.getAccountStatus(coin.any());
    }
  }
}

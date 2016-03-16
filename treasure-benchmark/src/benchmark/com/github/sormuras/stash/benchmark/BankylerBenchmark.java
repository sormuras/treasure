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

import com.github.sormuras.stash.benchmark.bank.Bank;
import com.github.sormuras.stash.benchmark.bank.prevayler.PrevaylerBank;

@State(Scope.Benchmark)
public class BankylerBenchmark {

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

  public static void main(String... args) throws Exception {
    for (int threads = 4; threads <= 4; threads++) {
      Options opts = new OptionsBuilder()
          .include(BankylerBenchmark.class.getSimpleName())
          .warmupIterations(3)
          .measurementIterations(3)
          .measurementTime(TimeValue.seconds(3))
          .threads(threads)
          .forks(1)
          .build();
      new Runner(opts).run();
    }
  }

  private Bank<Integer> bank;
  private Path temp;

  @Setup(Level.Iteration)
  public void before(BenchmarkParams params) throws Exception {
    temp = Files.createTempDirectory(params.getBenchmark() + "-" + params.getThreads() + "-");
    bank = new PrevaylerBank(temp);
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

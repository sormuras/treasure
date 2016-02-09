package com.github.sormuras.stash.benchmark;

import java.nio.ByteBuffer;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.github.sormuras.stash.ProxyStash;

public class AppendableBenchmark {

  @State(Scope.Thread)
  public static class AppendablePlainHolder {
    final StringBuilder builder;

    public AppendablePlainHolder() {
      this.builder = new StringBuilder();
    }

    Appendable get() {
      builder.setLength(0);
      return builder;
    }
  }

  @State(Scope.Thread)
  public static class AppendableProxyHolder {
    final Appendable appendable;
    final ByteBuffer buffer;

    public AppendableProxyHolder() {
      this.buffer = ByteBuffer.allocate(10000);
      this.appendable = ProxyStash.create(Appendable.class, new StringBuilder(), buffer);
    }

    Appendable get() {
      buffer.clear();
      buffer.putLong(0);
      return appendable;
    }
  }

  public static void main(String... args) throws Exception {
    Options opts = new OptionsBuilder()
        .include(".*")
        .warmupIterations(10)
        .measurementIterations(10)
        .jvmArgs("-server")
        .forks(1)
        .build();

    new Runner(opts).run();
  }

  @Benchmark
  public void plain(Blackhole blackhole, AppendablePlainHolder holder) throws Exception {
    blackhole.consume(holder.get().append('@').append("abc").append("abcdef", 3, 6));
  }

  @Benchmark
  public void proxy(Blackhole blackhole, AppendableProxyHolder holder) throws Exception {
    blackhole.consume(holder.get().append('@').append("abc").append("abcdef", 3, 6));
  }

}

package com.github.sormuras.stash.benchmark;

import java.io.IOException;
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

  @State(Scope.Thread)
  public static class AppendableStashHolder implements Appndble {
        
    final Appndble appendable;
    final ByteBuffer buffer;
    final StringBuilder builder;
    
    public AppendableStashHolder() {
      this.buffer = ByteBuffer.allocate(10000);
      this.appendable = new AppndbleStash(this, buffer);
      this.builder = new StringBuilder();
    }
    
    Appendable get() {
      builder.setLength(0);
      buffer.clear();
      buffer.putLong(0);
      return appendable;
    }

    @Override
    public Appendable append(CharSequence csq) throws IOException {
      return builder.append(csq);
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
      return builder.append(csq, start, end);
    }

    @Override
    public Appendable append(char c) throws IOException {
      return builder.append(c);
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
  
  @Benchmark
  public void stash(Blackhole blackhole, AppendableStashHolder holder) throws Exception {
    blackhole.consume(holder.get().append('@').append("abc").append("abcdef", 3, 6));
  }

}

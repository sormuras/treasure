package com.github.sormuras.treasure.demo;

import java.nio.ByteBuffer;

import com.github.sormuras.stash.Stash;

public class AppenableDemo {

  public static void main(String[] args) throws Exception {
    ByteBuffer buffer = ByteBuffer.allocate(1000);
    Appendable appendable = Stash.proxy(Appendable.class, new StringBuilder(), buffer);
    appendable.append('@').append("abc").append("abcdef", 3, 6);
    // assertEquals("@abcdef", appendable.toString());
    buffer.flip();
    appendable = Stash.proxy(Appendable.class, new StringBuilder(), buffer);
    // assertEquals("@abcdef", appendable.toString());
    appendable.append('@');
    // assertEquals("@abcdef@", appendable.toString());
    buffer.flip();
    appendable = Stash.proxy(Appendable.class, new StringBuilder(), buffer);
    // assertEquals("@abcdef@", appendable.toString());
    System.out.println(appendable);
  }

}

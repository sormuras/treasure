package com.github.sormuras.treasure.club;

import java.nio.ByteBuffer;

import com.github.sormuras.stash.Stash;

@Stash.Interface
public interface Club extends AutoCloseable {

  Member create(String name);

  Member member(int number);

  void rename(int number, String newName);

  static void main(String[] args) throws Exception {
    ByteBuffer tmp1 = ByteBuffer.allocate(1000);
    try (Club club = Stash.create(Club.class, new Disco(), tmp1)) {
      club.create("Aaron");
      club.create("Bert");
      club.create("Carla");
      club.rename(1, "Bartholomaeus");
    }
    ByteBuffer tmp2 = ByteBuffer.allocate(1000);
    try (Club club = Stash.proxy(Club.class, new Disco(), tmp2)) {
      club.create("Aaron");
      club.create("Bert");
      club.create("Carla");
      club.rename(1, "Bartholomaeus");
    }
    System.out.println(tmp1.equals(tmp2));
  }

}

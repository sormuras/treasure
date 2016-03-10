package com.github.sormuras.treasure.club;

import java.nio.ByteBuffer;

import com.github.sormuras.stash.Stash;
import com.github.sormuras.stash.Treasure;

@Stash.Interface
public interface Club extends AutoCloseable {

  Member create(String name);

  Member member(int number);

  void rename(int number, String newName);

  static void main(String[] args) throws Exception {
    ByteBuffer buffer = ByteBuffer.allocate(1000);
    try (Club club = Stash.create(Club.class, new Disco(), buffer)) {
      club.create("Aaron");
      club.create("Bert");
      club.create("Carla");
      club.rename(1, "Bartholomaeus");
    }
    try (Club club = Stash.proxy(Club.class, new Disco(), buffer)) {
      assert "Aaron".equals(club.member(0).name());
      assert "Bartholomaeus".equals(club.member(1).name());
      assert "Carla".equals(club.member(2).name());
      club.rename(2, "Ceera");
    }
    try (Club club = new ClubStash(Treasure.create(new Disco(), buffer))) {
      assert "Aaron".equals(club.member(0).name());
      assert "Bartholomaeus".equals(club.member(1).name());
      assert "Ceera".equals(club.member(2).name());
    }
  }

}

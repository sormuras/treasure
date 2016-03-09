package com.github.sormuras.treasure.club;

import java.util.ArrayList;
import java.util.List;

public class Disco implements Club {

  List<Member> members = new ArrayList<Member>();

  @Override
  public void close() throws Exception {
    members.clear();
  }

  @Override
  public Member create(String name) {
    int number = members.size();
    Member member = new Member(number, name);
    members.add(member);
    return member;
  }

  @Override
  public Member member(int number) {
    return members.get(number);
  }

  @Override
  public void rename(int number, String newName) {
    members.get(number).setName(newName);
  }

}

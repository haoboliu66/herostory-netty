package com.hbliu.herostory.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class User {

  public static final int INITIAL_HP = 100;

  private final int userId;

  private final String heroAvatar;

  @Setter
  private int currentHp;

}

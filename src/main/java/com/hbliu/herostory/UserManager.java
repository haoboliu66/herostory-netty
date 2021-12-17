package com.hbliu.herostory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.hbliu.herostory.entity.User;

public final class UserManager {

  private static final Map<Integer, User> userMap = new HashMap<>();

  private UserManager() {
  }

  public static void addUser(User user) {
    if (user == null) {
      return;
    }
    userMap.putIfAbsent(user.getUserId(), user);
  }

  public static void removeUser(Integer userId) {
    if (userId == null) {
      return;
    }
    userMap.remove(userId);
  }

  public static Collection<User> listUsers() {
    return userMap.values();
  }

  public static User getUserById(int targetUserId) {
    return userMap.get(targetUserId);
  }
}

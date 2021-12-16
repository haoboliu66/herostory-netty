package com.hbliu.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.hbliu.herostory.message.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
decoder
 */
public final class GameMsgRecognizer {

  private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgRecognizer.class);

  private static final Map<Integer, GeneratedMessageV3> msgCodeAndMsgObjectMap = new HashMap<>();

  private static final Map<Class<?>, Integer> msgClazzAndMsgCodeMap = new HashMap<>();

  private GameMsgRecognizer() {
  }

//    public static void init() {
//        msgCodeAndMsgObjectMap.put(GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE, GameMsgProtocol.UserEntryCmd.getDefaultInstance());
//        msgCodeAndMsgObjectMap.put(GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE, GameMsgProtocol.WhoElseIsHereCmd.getDefaultInstance());
//        msgCodeAndMsgObjectMap.put(GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE, GameMsgProtocol.UserMoveToCmd.getDefaultInstance());
//
//        clazzAndMsgCodeMap.put(GameMsgProtocol.UserEntryResult.class, GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE);
//        clazzAndMsgCodeMap.put(GameMsgProtocol.WhoElseIsHereResult.class, GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE);
//        clazzAndMsgCodeMap.put(GameMsgProtocol.UserMoveToResult.class, GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE);
//        clazzAndMsgCodeMap.put(GameMsgProtocol.UserQuitResult.class, GameMsgProtocol.MsgCode.USER_QUIT_RESULT_VALUE);
//    }

//  public static void init() {
//    LOGGER.info("完成消息类与消息编号的映射");
//
//    Class<?>[] innerClazzArray = GameMsgProtocol.class.getDeclaredClasses();
//    for (Class<?> clazz : innerClazzArray) {
//      if (clazz == null || !GeneratedMessageV3.class.isAssignableFrom(clazz)) {
//        continue;
//      }
//
//      String clazzName = clazz.getSimpleName();
//      String clazzNameLowerCase = clazzName.toLowerCase();
//
//      for (GameMsgProtocol.MsgCode msgCode : GameMsgProtocol.MsgCode.values()) {
//        if (msgCode == null) {
//          continue;
//        }
//        String msgCodeName = msgCode.name();
//        msgCodeName = msgCodeName.replaceAll("_", "")
//                                 .toLowerCase();
//
//        if (!msgCodeName.startsWith(clazzNameLowerCase)) {
//          continue;
//        }
//
//        Object obj = null;
//        try {
//          obj = clazz.getDeclaredMethod("getDefaultInstance")
//                     .invoke(clazz);
//          msgCodeAndMsgObjectMap.put(msgCode.getNumber(), (GeneratedMessageV3) obj);
//          LOGGER.info("msgClass:{} <===> msgCode:{}", clazz.getName(), msgCode.getNumber());
//
//          msgClazzAndMsgCodeMap.put(clazz, msgCode.getNumber());
//
//        } catch (Exception e) {
//          LOGGER.error(e.getMessage(), e);
//        }
//      }
//    }
//  }

  public static void init() {
    LOGGER.info("完成消息类与消息编号的映射");

    Class<?>[] innerClazzArray = GameMsgProtocol.class.getDeclaredClasses();

    for (Class<?> innerClazz : innerClazzArray) {
      if (innerClazz == null || !GeneratedMessageV3.class.isAssignableFrom(innerClazz)) {
        continue;
      }
      final String clazzName = innerClazz.getSimpleName();
      final String clazzNameLowerCase = clazzName.toLowerCase();

      for (GameMsgProtocol.MsgCode msgCode : GameMsgProtocol.MsgCode.values()) {
        if (msgCode == null) {
          continue;
        }
        String msgCodeName = msgCode.name();
        msgCodeName = msgCodeName.replaceAll("_", "")
                                 .toLowerCase();

        if (!msgCodeName.startsWith(clazzNameLowerCase)) {
          continue;
        }

        Object mappedObject = null;
        try {
          mappedObject = innerClazz.getDeclaredMethod("getDefaultInstance")
                                   .invoke(innerClazz);

          LOGGER.info("Mapping {} <==> {}", innerClazz.getName(), msgCode.getNumber());

          msgCodeAndMsgObjectMap.put(msgCode.getNumber(), (GeneratedMessageV3) mappedObject);
          LOGGER.info("msgClass:{} <===> msgCode:{}", innerClazz.getName(), msgCode.getNumber());

          msgClazzAndMsgCodeMap.put(innerClazz, msgCode.getNumber());

        } catch (final Exception e) {
          LOGGER.error(e.getMessage(), e);
        }
      }
    }
  }


  public static Message.Builder getBuilderByMsgCode(int msgCode) {
    if (msgCode < 0) {
      return null;
    }
    LOGGER.info("GameMsgRecognizer # msgCode received: {}", msgCode);

    final GeneratedMessageV3 obj = msgCodeAndMsgObjectMap.get(msgCode);
    System.out.println(obj);
    if (obj == null) {
      return null;
    }
    System.out.println(obj);
    return obj.newBuilderForType();
  }


  public static Integer getMsgCodeByClazz(Class<?> msgClazz) {
    if (msgClazz == null) {
      return -1;
    }
    final Integer msgCode = msgClazzAndMsgCodeMap.get(msgClazz);
    if (msgCode == null) {
      return -1;
    }
    return msgCode;
  }

}

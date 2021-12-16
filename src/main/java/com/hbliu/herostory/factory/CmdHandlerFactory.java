package com.hbliu.herostory.factory;

import com.google.protobuf.GeneratedMessageV3;
import com.hbliu.herostory.cmdhandler.CmdHandler;
import com.hbliu.herostory.cmdhandler.UserEntryCmdHandler;
import com.hbliu.herostory.util.PackageUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CmdHandlerFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(CmdHandlerFactory.class);

  private static final Map<Class<?>, CmdHandler<? extends GeneratedMessageV3>> handlerMap = new HashMap<>();

  private CmdHandlerFactory() {
  }

//  public static void init() {
//    handlerMap.put(GameMsgProtocol.UserEntryCmd.class, new UserEntryCmdHandler());
//    handlerMap.put(GameMsgProtocol.WhoElseIsHereCmd.class, new WhoElseIsHereCmdHandler());
//    handlerMap.put(GameMsgProtocol.UserMoveToCmd.class, new UserMoveToCmdHandler());
//  }

  @Test
  public void getGenericType() {
    Class<UserEntryCmdHandler> clazz = UserEntryCmdHandler.class;
    Type[] genericInterfaces = clazz.getGenericInterfaces();
    ParameterizedType type = (ParameterizedType) genericInterfaces[0];
    Class<?> targetClazz = (Class<?>) type.getActualTypeArguments()[0];
    System.out.println(targetClazz);
    System.out.println(GeneratedMessageV3.class.isAssignableFrom(targetClazz));
  }


  /*  建立 cmd消息类型 和 其对应handler类 的映射 */
  public static void init() {
    LOGGER.info("CmdHandlerFactory init");
    final String packageName = CmdHandler.class.getPackage()
                                               .getName();
    // 获取CmdHandler的所有实现类
    Set<Class<?>> clazzSet = PackageUtil.listSubClazz(packageName, true, CmdHandler.class);

    // 要找到subClazz这个类实现的接口上的范型
    for (Class<?> subClazz : clazzSet) {
      if (subClazz == null || Modifier.isAbstract(subClazz.getModifiers())) {
        continue;
      }

      Class<?> targetCmdClazz = null;

      Type[] genericInterfaces = subClazz.getGenericInterfaces();
      ParameterizedType type = (ParameterizedType) genericInterfaces[0];
      targetCmdClazz = (Class<?>) type.getActualTypeArguments()[0];

//      Method[] methodArray = subClazz.getDeclaredMethods();
//      for (Method currMethod : methodArray) {
//        if (currMethod == null || !currMethod.getName()
//                                             .equals("handle")) {
//          continue;
//        }
//        Class<?>[] parameterTypes = currMethod.getParameterTypes();
//
//        if (parameterTypes.length < 2 || parameterTypes[1] == GeneratedMessageV3.class
//            || !GeneratedMessageV3.class.isAssignableFrom(parameterTypes[1])) {
//          continue;
//        }
//        targetCmdClazz = parameterTypes[1];
//        break;
//      }

      if (targetCmdClazz == null) {
        continue;
      }

      try {
        CmdHandler<?> newHandler = (CmdHandler<?>) subClazz.newInstance();
        LOGGER.info("Mapping {} <==> {}", targetCmdClazz.getName(), subClazz.getName());
        handlerMap.put(subClazz, newHandler);

      } catch (InstantiationException e) {
        LOGGER.error("", e);
      } catch (IllegalAccessException e) {
        LOGGER.error("", e);
      }
    }
  }


  /**
   * 根据传入的消息类型创建相应的handler
   */
  public static CmdHandler<? extends GeneratedMessageV3> createHandler(Class<?> msgClazz) {
    if (msgClazz == null) {
      return null;
    }
    return handlerMap.get(msgClazz);
  }

}

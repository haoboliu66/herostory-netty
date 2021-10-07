32 创建第一个游戏服务器 [地址](https://ke.qq.com/webcourse/index.html#cid=398381&term_id=100558952&taid=4185784932766765&type=1024&vid=5285890800399063565)

33 Protobuf消息应用 [地址](https://ke.qq.com/webcourse/index.html#cid=398381&term_id=100558952&taid=4185789227734061&type=1024&vid=5285890800507950005)

34 别急，先重构！设计模式实战 [地址](https://ke.qq.com/webcourse/index.html#cid=398381&term_id=100558952&taid=4185793522701357&type=1024&vid=5285890800718120421)

35 反射的终级实战 [地址](https://ke.qq.com/webcourse/index.html#cid=398381&term_id=100558952&taid=4185797817668653&type=1024&vid=5285890800845174567)

36 重新设计移动消息 [地址](https://ke.qq.com/webcourse/index.html#cid=398381&term_id=100558952&taid=4185802112635949&type=1024&vid=5285890801032246624)

37 单线程设计 [地址](https://ke.qq.com/webcourse/index.html#cid=398381&term_id=100558952&taid=4185806407603245&type=1024&vid=5285890801121684993)

38 多线程设计 [地址](https://ke.qq.com/webcourse/index.html#cid=398381&term_id=100558952&taid=4185810702570541&type=1024&vid=5285890801342298616)

39 排行榜功能实现 [地址](https://ke.qq.com/webcourse/index.html#cid=398381&term_id=100558952&taid=4185814997537837&type=1024&vid=5285890801430986082)

40 游戏服务器的部署 [地址](https://ke.qq.com/webcourse/index.html#cid=398381&term_id=100558952&taid=4185819292505133&type=1024&vid=5285890801654691979)







# 第一节





# 第二节

> 33 Protobuf消息应用



为什么要传字节数组?  而不是String 

- 出于带宽考虑  e.g. 字符串中一个char就占1个字节, 假设传数字1234567890, 需要10个字节, 但是如果传字节数组, 一个int就够了



sticking packet



消息编号: 消息类型的编码, 这个消息是干什么用的   





## Protobuf协议文档

根据 .proto文件自动生成java类





如何让用户之间互相看到



先上线的用户可以看到后上线的用户(因为后上线的用户会做消息广播), 但是后上线的用户看不到之前的用户

- 新用户收不到老用户的广播消息

后上线的用户需要询问: who else is here (客户端要发送另一个cmd)







Decoder(解析消息, 根据消息编码, 看msg是什么类型的cmd) 

=> MsgHandler(业务handler, 把cmd传递给业务handler进行处理, 根据请求的cmd类型, 决定我要返回什么类型的result)

=> Encoder(拿到业务handler生成的result, 找到对应的消息编码/msgCode, 然后按照 <消息长度> + <消息编码> + <消息体> 的格式进行数据编排并写回)





# 第三节












# MongoDB-Learning

> 这个项目使用了 `spring-boot-starter-data-mongodb` 整合了 SpringBoot 和 MongoDB，归纳了一些 MongoDB 的常用操作
>
> 适合入门学习的小伙伴，也可以作为平时开发要使用 MongoDB 时参考的原型
>
> 我的知识库👉[Javrin](https://github.com/gelald/javrin)，希望可以帮到你！希望可以给我一个Star，亮起小星星🌟～

## 模块说明

### mongodb-learning

这个是所有模块的父工程，主要是统一子工程使用的依赖版本，统一打包配置

本项目使用的主要依赖的版本:

| 🔧依赖                              | 📖版本   |
|-----------------------------------|--------|
| spring-boot                       | 2.6.11 |
| mongodb-driver-core               | 4.4.2  |
| spring-boot-starter-data-mongodb  | 2.6.11 |

### mongodb-gridfs

这个模块展示了如何使用 MongoDB 中的 GridFS 作为文件存储中间件

## 部署说明

### MongoDB 部署方式

MongoDB 部署方式官网中提供了二进制安装部署方式，我们这里使用 docker-compose 的部署方式

在 mongodb-learning 下有一个目录 docker，这个目录下包含了：

- docker-compose.yaml：docker compose 配置文件
  - mongo：mongodb 数据库的部署
  - mongo-express：mongodb 的一个网页客户端，功能还算够用，**如果有其他 mongodb 客户端工具可以不启动**，如 Navicat、MongoDB Compress 等

- mongodb：mongodb 容器中挂载的目录，数据库中的数据会存储在这个目录下

启动方式：在 docker-compose.yaml 文件同目录下打开终端，输入命令 `docker-compose up -d` 启动

验证是否启动成功：输入命令 `docker-compose ps` 可以看到 mongodb、mongo-express 三个容器

部署正常打开 dashboard 映射宿主机的端口能看到 mongo-express 客户端👇：

![](https://wingbun-notes-image.oss-cn-guangzhou.aliyuncs.com/images/20230313160854.png)

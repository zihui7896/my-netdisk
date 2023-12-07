#### netdisk

### 项目介绍

---

在线网盘是一个前后端分离项目，项目采用 SpringBoot+Vue3 开发。

### 项目功能

文件上传（断点续传，秒传）

![](./images/20230628211004.png)

[百度 (快速体验文件上传)](http://fex.baidu.com/webuploader/getting-started.html) 

Github: [https://github.com/fex-team/webuploader](https://github.com/fex-team/webuploader)

文件分享

![](./images/20230628210057.png)

回收站

![](./images/20230628210120.png)

文件预览 （支持 .pdf .docx .xlsx .mkv 图片）

![](./images/20230628211422.png)

![](./images/20230628211437.png)

![](./images/20230628211449.png)

![](./images/20230628212148.png)

![](./images/20230628212342.png)

![](./images/20230628212459.png)

管理员功能

浏览用户文件

![](./images/20230629194347.png)

搜索用户 (添加空间，禁用用户)

![](./images/20230629194445.png)

登录注册

![](./images/20230630144842.png)

![](./images/20230630144854.png)

### 数据库

1. 新建数据库 netdist
2. 运行 SQL 文件 （在 netdiskserver/src/main/resources/netdisk.sql)
3. 修改 application.properties 中的数据库配置

# keycloak-login-spi

#### 一、下载解压 keycloak server

https://www.keycloak.org/downloads.html

#### 二、使用自定义插件

https://moz-kamen.github.io/keycloak/keycloak_login_spi/keycloak-login-spi-1.0.0-SNAPSHOT.jar

在 ***keycloak*** 根目录下创建 ***providers*** 文件夹

将自定义插件放入改文件夹中, 执行以下启动 ***keycloak*** 

###### windows : ***$keycloak/bin/standalone.bat*** 

###### linux : ***$keycloak/bin/standalone.sh*** 



#### 三、配置自定义Authentication

1、新增 authentication flow

***点击新增按钮 :***
![](https://moz-kamen.github.io/keycloak/keycloak_login_spi/image/001_add_authenticator_1.png)

# keycloak-login-spi

#### 一、下载解压 keycloak server

https://www.keycloak.org/downloads.html

#### 二、使用自定义插件

https://moz-kamen.github.io/keycloak/keycloak_login_spi/keycloak-login-spi-1.0.0-SNAPSHOT.jar

在 ***keycloak*** 根目录下创建 ***providers*** 文件夹

将自定义插件放入改文件夹中, 执行以下启动 ***keycloak*** 

###### windows : ***$keycloak/bin/standalone.bat*** 

###### linux : ***$keycloak/bin/standalone.sh*** 



#### 三、创建 Authentication

1、新增 authentication flow

***点击新增按钮 :***
![add_authentication_1](https://moz-kamen.github.io/keycloak/keycloak_login_spi/image/add_authentication_1.png)

***填写 alias 点击保存 :***
![add_authentication_2](https://moz-kamen.github.io/keycloak/keycloak_login_spi/image/add_authentication_2.png)



#### 四、创建 client 并指定接口登录校验方式

***点击创建 client :***

![add_client_1](https://moz-kamen.github.io/keycloak/keycloak_login_spi/image/add_client_1.png)

***填写 client Id , 选择 openid-connect :***

![add_client_2](https://moz-kamen.github.io/keycloak/keycloak_login_spi/image/add_client_2.png)

***完成创建后进入编辑页，settings最下方 , 选择上一步创建的 authentication flow 并保存  :***

![edit_client_1](https://moz-kamen.github.io/keycloak/keycloak_login_spi/image/edit_client_1.png)



#### 五、创建用户

![add_user_1](https://moz-kamen.github.io/keycloak/keycloak_login_spi/image/add_user_1.png)

![add_user_2](https://moz-kamen.github.io/keycloak/keycloak_login_spi/image/add_user_2.png)

***编辑用户属性 :***

![edit_user_1](https://moz-kamen.github.io/keycloak/keycloak_login_spi/image/edit_user_1.png)



#### 六、配置自定义登录流程

***编辑 Authentication 并添加 execution :***

![edit_authentication_1](https://moz-kamen.github.io/keycloak/keycloak_login_spi/image/edit_authentication_1.png)

![add_excution_1](https://moz-kamen.github.io/keycloak/keycloak_login_spi/image/add_excution_1.png)

***设置 execution可用 :***

![edit_excution_1](https://moz-kamen.github.io/keycloak/keycloak_login_spi/image/edit_excution_1.png)

***添加配置 :***

![edit_excution_2](https://moz-kamen.github.io/keycloak/keycloak_login_spi/image/edit_excution_2.png)

***推荐完整配置 :***

![suggest_execution_flow](https://moz-kamen.github.io/keycloak/keycloak_login_spi/image/suggest_execution_flow.png)



---

###### 1、密码登陆模式

---

***config :***

```txt
# 登录接口用户名字段名
# eg: username
login form key - username

# 登录接口密码字段名
# eg: password
login form key - password

```

***request :***

```txt
http://localhost:8080/auth/realms/master/protocol/openid-connect/token

Content-Type:application/x-www-form-urlencoded

grant_type:password
client_id:client001
login_type:password
username:liang.xu
password:123456
```



---

###### 2、手机验证码模式

---

***config :***

```txt
# 使用手机号检索用户时，所用 keycloak user attribute 的字段名
# eg: phone
user attribute key - phone

# 登录接口手机号字段名
# eg: phone
login form key - phone 

# 登录接口手机验证码字段名
# eg: code
login form key - code

# 短信校验请求地址
# eg: https://localhost:8080/v1/verifycode/checkcode
sms request url

# 短信校验请求方式 ( get / post.form / post.json )
# eg: post.json
sms request method

# 请求短信服务时默认请求参数，json格式，用于配置常量参数，例如模板类型
# eg: {"clientId": "AAABBB"}
sms request param - default

# 请求短信服务时传入手机号的字段名称
# eg: phone
sms request param - phone

# 请求短信服务时传入验证码的字段名称
# eg: code
sms request param - code

# 短信服务返回值用于确认是否成功的字段名(多个json层级使用.取下级键)
# eg: data.successFlag
sms response check - key

# 短信服务返回值用于确认是否成功的字段值，会根据实际返回值类型转换后校验
# eg: true
sms response check - value
```

***request :***

```txt
http://localhost:8080/auth/realms/master/protocol/openid-connect/token

Content-Type:application/x-www-form-urlencoded

grant_type:password
client_id:client001
login_type:phone
phone:18355512590
code:1234
```



---

###### 3、微信登录模式

---

***config :***

```
# 使用微信号检索用户时，所用 keycloak user attribute 的字段名
# eg: wechat_unionid
user attribute key - wechat unionid

# 登录接口微信号字段名
# eg: wechat_unionid
login form key - wechat unionid
```

***request :***

```txt
http://localhost:8080/auth/realms/master/protocol/openid-connect/token

Content-Type:application/x-www-form-urlencoded

grant_type:password
client_id:client001
login_type:wechat
wechat_unionid:wx_123456
```


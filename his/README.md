### 后端
+ 新建springboot项目，在pom.xml添加相关依赖
+ 根据需要使用 `utils/CodeGenerator` 生成代码 
+ 需改项目配置文件 `application.yml`
+ `utils` 工具类
  + `Constants` 定义常量的类
  + `Result` 返回json数据的格式 （data, status , msg) 数据 状态码 消息类型
  + `ResultGenerator` : 封装一些 返回Result方法
  + `CodeGenerator`: mybatis的代码生成器
  + `InitUtil`: 用来初始化参数信息包括 **分页默认值、like判断**
  + `JwtUtill`: 利用Jwt生成token 

+ `config` 配置类
  + `DefaultFastjsonConfig` : fastjson配置

+ 启动类
  + 需要加上`@MapperScan`注解

### 登录信息保存
+ 导入JWT依赖
```xml
		<dependency>
			<groupId>com.auth0</groupId>
			<artifactId>java-jwt</artifactId>
			<version>3.10.3</version>
		</dependency>
```

### mybatis-plus的使用 
+ 对于实体类的字段，mybatis-plus默认会认为数据库的字段，在字段对应不上的报错
  + `@TableField(exist = false)` 表明不需要在数据库对应

#### mybatis-plus分页功能使用



### 问题

+ `java.sql.SQLFeatureNotSupportedException`
  + 这里的问题是mybatis和druid的版本不一致 mybatis 3.5.4 和 druid 1.1.12 
  + 把druid版本更新到1.1.23 问题解决


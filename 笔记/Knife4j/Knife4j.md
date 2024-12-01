# Knife4j

Knife4j是什么详见：

[Swagger](https://www.wolai.com/nsUR5rAmGcPcsF6DM9epoA "Swagger")

# 1. 导入

在使用Knife4j时需要导入maven坐标：

```java
<dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-spring-boot-starter</artifactId>
</dependency>
```

# 2. 生成接口文档

通过定义一个函数生成接口文档

```java
public Docket docket() {
    // 创建 API 文档的基本信息，包括标题、版本和描述
    ApiInfo apiInfo = new ApiInfoBuilder()
            .title("苍穹外卖项目接口文档") // 设置文档标题
            .version("2.0") // 设置文档版本
            .description("苍穹外卖项目接口文档") // 设置文档描述信息
            .build(); // 构建 ApiInfo 对象

    // 创建 Docket 对象，用于配置 Swagger 的核心内容
    Docket docket = new Docket(DocumentationType.SWAGGER_2) // 指定使用 Swagger 2 的规范
            .apiInfo(apiInfo) // 设置文档的基本信息
            .select() // 开始选择接口的扫描规则
            .apis(RequestHandlerSelectors.basePackage("com.sky.controller")) // 指定扫描的包路径
            .paths(PathSelectors.any()) // 对所有路径生成文档
            .build(); // 构建 Docket 对象

    // 返回 Docket 对象供 Spring 容器使用
    return docket;
}

```

# 3. 设置静态资源映射

通过设置映射，这样可以通过浏览器的指定地址访问生成的接口信息

```java
protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
```

# 4. 在具体位置写说明

调用注解，在需要说明的类，方法，参数等地方写上注释，例如对方法的注释使用@ApiOperation

```java
@ApiOperation(value = "员工退出")
    public Result<String> logout() {
        return Result.success();
    }
```

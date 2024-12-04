# 新增菜品-上传图片（阿里云OSS配置）

# 1. access key

# 2. 配置阿里云OSS

在src/main/resources/application.yml的sky下配置阿里云OSS的账户密码，在配置时，会有代码补全，这是因为在sky-common/src/main/java/com/sky/properties文件夹下有一个AliOssProperties类，这个类使用@ConfigurationProperties(prefix = "sky.alioss")注解，表明其是一个属性配置类，代码如下：

```java
@Component
@ConfigurationProperties(prefix = "sky.alioss")
@Data
public class AliOssProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

}
```

## 2.1 配置属性

在application.yml里配置属性时，不需要将具体的属性写进去，我们需要将具体的属性写在application\_dev.yml中，而application.yml只做引用。

```yaml
sky:
  jwt:
    # 设置jwt签名加密时使用的秘钥
    admin-secret-key: itcast
    # 设置jwt过期时间
    admin-ttl: 7200000
    # 设置前端传递过来的令牌名称
    admin-token-name: token
  alioss:
    endpoint: ${sky.alioss.endpoint}
    access-key-id: ${sky.alioss.access-key-id}
    access-key-secret: ${sky.alioss.access-key-secret}
    bucket-name: ${sky.alioss.bucket-name}
```

## 2.2 创建阿里云文件上传工具类对象

通过实例化sky-common/src/main/java/com/sky/utils/AliOssUtil类，我们就可以使用这个工具类的功能，实例化代码如下：

```java
@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    @ConditionalOnMissingBean       //表示只需要有一个bean就行
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("开始创建阿里云文件上传工具类对象：{}", aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}
```

## 2.3 上传文件并将文件地址返回给前端

```java
/**
     * 用于接收前端传来的文件，并将其上传到阿里云服务器
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);

        try {
            //获取原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名后缀
            String extention = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构建新文件名称
            String objectName = UUID.randomUUID().toString() + extention;
            //文件请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e.getMessage());
        }
        return null;
    }
```

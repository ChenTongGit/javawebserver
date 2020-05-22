# Development Record

## 问题记录

* Date 2020/05/22
    * 描述 : lombok导入失败, @Slf4j/@Data/等注解无法在正常工作。
    * 解决 : IDEA未安装lombok插件, 无法解析注解 ; Java版本问题, 将JDK版本降低到jdk1.8解决问题 
    * 描述 : 解析Request中中文出现乱码 
    * 解决 : 解析GET请求url中的参数时 invoke decode() -> utf-8 
    
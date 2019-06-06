# springBoot-scaffold
springBoot2.1.x脚手架，集成redis、pagehelper、mongodb、mybatis、log4j2、druid、jwt、mail等。完成大部分公共模块，只需要写业务代码即可。
### redis方面
1、集成springBoot-redis，同时解决redisTemplate序列化问题，提供工具类手动写缓存
2、集成springBoot-cache，同时解决redis序列化问题，注解完成。  
补充：如果将注解缓存写在dao层除了注意继承序列话接口以外，热部署依赖也可能报同类型不能转换。
### AOP方面
1、检验token的有效信  
2、检验是否在线    
3、你也可以继续写校验  
4、由于AOP切的是controller层，所以当dao层发生错误时候就会向上抛给serviceImpl，接着继续向上service，最后到controller。
这也以为着，在AOP能捕获所有的错误，所以当业务代码有错误的时候，就会发邮件给管理者，提示API出错。在ECS可能有BUG，后续修改。  
5、记录API调用次数  
6、记录API调用时间，写在日志文件info里。  
补充：后续方面将检验都写成注解形式，通过切面编程，更可能少的写在业务代码里面。
### mybatis方面
1、提供逆转录的配置文件，以及插件依赖  
2、集成pagehelper，可直接使用。  
### log4j2方面
1、提供全面日志系统，可根据项目自行修改项目日志文件  
2、也可根据自行配置线上环境以及测试环境、本地环境的日志文件
### druid方面
1、在配置文件集成
### jwt方面
1、可根据实际的业务修改token信息
### schedule方法
1、清除错误缓存  
2、清除API记录

## 2019.05.08更新
新增async包，里面封装异步操作redis缓存API  
使用方法：  

```
    @Autowired
    RedisAsync redisAsync;

    @Test
    public void test() {

        /**
         * 异步更新redis
         */
        redisAsync.updateRedisAsync("key", "value1", "test1");

        /**
         * 异步添加redis
         */
        redisAsync.addRedisAsync("key", "value", "test2");

        /**
         * 异步删除redis
         */
        redisAsync.deleteRedisAsync("key", "test3");
    }
```
可以和srping Cache结合使用，注意redis的key的生成策略即可。
## 2019.05.10更新
新增config底下的cors包，解决前后端测试跨域问题，解决@CrossOrigin可能无效问题。

## 2019.06.04更新
将config底下的cors包重新命名为mvc，移除AOP处理的token，使用拦截器处理更优雅，可以在ParamsInterceptor.java类中继续写参数校验。
同时解决了springCache的统一命名方式，解决缓存有效期问题，达到实际缓存时间效果。  
使用方式
```
@Cacheable(value = "XXX方法", key = "#request.getSession().getAttribute('account')")
   public MessageVO XXX方法(HttpServletRequest request, XXX xxx) {}
注：XXX可自定义
```
## 2019.06.06更新
修复不能异步写日志


# 最后，如果这个项目对您有参考价值，请不要吝啬您的star。您star就是对我最大鼓励。 2019.05.07

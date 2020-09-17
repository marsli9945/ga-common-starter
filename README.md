# GA-COMMON工具包使用文档

## 开发前提
&emsp;&emsp;在GA服务开发过程中，由于前期没有进行严格的统一规范约定，因此在现行的各子服务中对统一的输出、异常处理等缺失或不一致。在此情景下开发ga-common-spring-boot-starter工具集，致力于通过简单的配置和导入即可完成对服务的输出、异常处理及日志有统一的规范和实现。

## 开发方案
&emsp;&emsp;在spring-boot生态中自定义starter功能可以十分方便的对宿主程序进行功能增补及可配置调整，在当前场景下十分适合工具集的开发。在ga-common中不仅提供子服务中可能使用的工具类，同时提供基于配置灵活开启的自动注入程序从而帮助开发者避免重复性劳动。

## 注意事项
### 一、在jar包同级目录创建tem/galog文件用于日志本地缓存


## 功能模块

### 一、统一输出

#### 1、CommonResult统一输出工具类
> 可直接使用此工具类输出统一的json结果

```
CommonResult.success("success");
{
    "code": 0,
    "msg": "操作成功",
    "data": "success"
}

CommonResult.failed("请求超时");
{
    "code": 10,
    "msg": "请求超时"
}
```
#### 2、ResultAdviceAutoConfiguration统一输出自动处理

##### 开启配置即可自动将输出包装为既定格式json

```
common:
  result:
    enable: true
```

```
@GetMapping("say")
public String say(@RequestParam String name)
{
    return "say hello";
}

{
    "code": 0,
    "msg": "操作成功",
    "data": "say hello"
}
```

```
@GetMapping("err")
public String err(HttpServletResponse response)
{
    response.setStatus(400);
    return "error";
}

{
    "code": 400,
    "msg": "error"
}

```

### 二、统一异常处理

##### 开启配置即可自动拦截处理程序运行时错误，并对调用放输出统一输出json

```
common:
  exception:
    enable: true
```

```
@GetMapping("exception")
public int exception()
{
    log.info("除以0了");
    return 1 / 0;
}

{
    "code": 10,
    "message": "/ by zero",
    "data": null
}
```

#### 已处理异常
- 空指针异常 NullPointerException
- http连接异常 HttpClientErrorException
- body参数校验异常 MethodArgumentNotValidException
- query参数校验 BindException
- 自定义定异常 CommonException
  
### 三、日志上报

#### 重构了ga-log-starter的日志上报功能
- 通过在starter中注入HttpServletRequest自动收集了头信息中的必要信息，从而简化了日志记录方法。
- 改用openfeign方式调用log-server，简化starter内部逻辑的同时使用者也无需关系和配置log-server地址
- 简化必须的配置项，对基本不变对配置项设置默认值

```
common:
  logger:
    lib:
      language: java # 有默认值，没必要写
      service-version: 1.3.6
    properties:
      model-version: 2.7
      service-name: test
    # 可选项，有默认值
    project-id: 20435
    client-id: "H5_5.0_tuyoo.tuyoo.0-hall20435.tuyoo.GA"
    local-path: tmp/galog #日志本地临时存储位置
    advice: false # 开启后会在服务处理前后自动上报日志
    enable-send: true # 日志上报开关，可关闭日志上报
    enable-terminal: false # 开启后可在控制台打印日志内容
    send-wait-size: 10 #日志队列长度，到达峰值后上报一次
    send-wait-time: 10 #日志上报定时，单位分钟
```

#### 日志上报调用方法
```
public void track(String event);

public void track(String event, HashMap<String, String> properties, HashMap<String, String> lib);
```

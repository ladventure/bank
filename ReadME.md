# 功能设计

设计一个银行交易管理系统。主要逻辑是用户存取款记录。主要功能如下，具体API 通过 spring 提供

运行系统，然后通过 http://localhost:8080/v3/api-docs 获取系统 API 说明文档。

createTransaction 创建一条记录
deleteTransaction 删除一条记录
modifyTransaction 修改一条记录
listAllTransactions 查询所有记录

PS: 页面功能目前只实现了列表和删除，创建和修改待补充。

# 技术选型与架构设计

1. 可扩展性
2. 可观测性
3. 高性能
4. 高可靠性

## 模块划分 & 依赖抽象

使用 DDD 设计模式，将功能内聚在 domain 内。项目层级划分如下，正式项目应该通过 module 划分，demo 比较简单，通过 package 代替 module

1. controller：对外暴漏 http 服务
2. application：service 层，项目对外接口抽象实现，服务定义在 client，可以对外暴露 rpc 客户端。通过组织 domain功能提供服务。
3. domain：具体业务逻辑实现层
4. infrastructure：底层依赖层，数据模块、核心依赖、配置等
5. common：通用常量以及工具方法等[](https://)

通过 controller 层拦截器实现异常的统一处理、日志的统一格式打印

## 可观测

通过拦截器打印统一格式日志：

1. 统一格式日志排查问题
2. 日志通过采集、上报打点的方式作为业务可观测建设。todo

## 高性能

1. 代码实现高性能，通过框架与多线程技术，利用所有计算资源
2. 业务逻辑中的锁、同步手段，尽量高性能
3. 引入 cache 等逻辑加速。 todo

## 高可靠性

本服务是单机 demo，也没有对外依赖，暂不涉及。todo

高性能主要需要能够弹性、限流、可降级、快速恢复等手段，在系统遇到异常的时候，仍然能够将影响降低到最低

## 额外依赖

springboot web 相关功能，基础功能框架

lombok ：简化代码，减少 get set 等格式代码

fastjson：进行 json 相关处理

springdoc-openapi-starter-webmvc-ui：生成文档

spring-boot-starter-thymeleaf：使用thymeleaf 模版编写页面

# 测试设计

测试整体考虑几个点：

1. 功能
2. 安全性
3. 性能

## 功能单元测试

通过本地单元化测试，单元测试着重测试单函数或者单模块逻辑是否正常。完善的单元测试可以避免代码修改之后不符合预期情况，对后续代码维护质量也是一个保障。

本项目由于是个demo，逻辑简单，无对外依赖，当前使用端到端单元测试模式测试整体功能。

单元测试应该对结果进行较为完善的断言，时间问题，目前仅断言简单的状态，这个待补充。

## 功能页面测试

通过页面点击查看功能正常、页面显示正常

## 功能自动化测试

交付之后，可以构建自动化测试平台。通过自动化测试，可以有几种方案：

1. 线上流量引流测试
2. 编写接口自动化测试
3. 页面自动化测试

## 性能测试

性能测试方案：

1. 模块性能测试：单方法、单模块性能测试，需要保证有性能风险的局部性能稳定。一般使用 benchmark 工具进行。
2. 整体性能测试：一般分布式系统，需要通过性能压测平台打流。

本项目是单机 demo，使用 JMH 进行简单的接口性能测试。测试代码参见：SpringBootBenchmark

测试结果：

分别对 几个API 性能测试：

CPU 消耗：很低，火焰图也没有明显的代码瓶颈。内存、事件也无异常。

benchmark结果：

Result "com.example.lili.bank.controller.SpringBootBenchmark.testListAllTransactions":
≈ 0.001 ms/op

Result "com.example.lili.bank.controller.SpringBootBenchmark.testCreateTransactions

≈ 10⁻⁴ ms/op

Result "com.example.lili.bank.controller.SpringBootBenchmark.testModifyTransactions":
≈ 10⁻⁴ ms/op

Result "com.example.lili.bank.controller.SpringBootBenchmark.testDeleteTransactions":
≈ 0.001 ms/op

# 上线运维设计

上线一般需要通过持续集成流水线进行。demo 使用 docker 方式，通过 Dockerfile 构建镜像，手动运行镜像方式。

1. 先使用 mvn package 打包
2. 然后使用 docker build -t bank:1.0 . 执行镜像
3. 使用 docker run bank:1.0

监控运维等一般可以通过 sidecar 以及 service mesh 实现。

# 用户手册

## 页面功能

访问：http://localhost:8080/transactions

## 接口功能

1. 创建接口

```
curl --location --request POST 'http://localhost:8080/transactions/createTransaction' \\

--header 'Content-Type: application/json' \\

--data-raw '{

"accountId":"111",

"amount": 110.12,

"timestamp": "2025-01-24 21:21:21",

"type": "WITHDRAWAL"
}'
```

2. 修改接口
```
--header 'Content-Type: application/json' 
--data-raw '{
"pageSize":90,
"accountId":"222"
}'
```
3. 删除接口

```
http://localhost:8080/transactions/deleteTransaction/fa966440-8564-4903-a19b-eed93ea7445e
```

4. 查询接口

```
curl --location --request POST 'http://localhost:8080/transactions/listTransactions' \
--header 'Content-Type: application/json' \
--data-raw '{
    "pageSize":2,
    "accountId":"111"
}'
```

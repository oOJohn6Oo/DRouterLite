## 轻量 Android 路由框架

使用 [DRouter] API + AGP 8.0 API + [KSP] 实现的轻量级 Android 路由框架

----------

### 实现功能

- [x] Activity 跳转
- [ ] Fragment 跳转 TODO
- [x] 服务发现
- [x] 支持 Configuration Cache
- [x] 隐藏 DRouterLite 非必要 API

### 接入

1. 在 base 模块依赖 `drouter-lite-api`

``` kotlin
api("io.github.oojohn6oo:drouterlite-api:1.0.0-alpha03")

```

2. 在所有需要路由收集的模块 build.gradle 中依赖 `collector` 插件

``` kotlin
plugins {
    id("com.google.devtools.ksp")
}
...
dependencies {
    ksp("io.github.oojohn6oo:drouterlite-collector:1.0.0-alpha04")
}

```
3. 在 app 模块 build.gradle 中依赖 `assembler` 插件

``` kotlin
plugins {
    id("io.github.oojohn6oo:drouterlite-assembler") version "1.0.0-alpha06"
}
```

### 使用

* 添加路由
    https://github.com/oOJohn6Oo/DRouterLite/blob/f59c2292c855f559560158eb2d02bec5b91d62e8/app/src/main/kotlin/io/john6/router/drouterlite/MainActivity.kt#L24-L25
* 跳转路由
    https://github.com/oOJohn6Oo/DRouterLite/blob/f59c2292c855f559560158eb2d02bec5b91d62e8/app/src/main/kotlin/io/john6/router/drouterlite/MainActivity.kt#L65-L71
* 添加服务
    https://github.com/oOJohn6Oo/DRouterLite/blob/f59c2292c855f559560158eb2d02bec5b91d62e8/app/src/main/kotlin/io/john6/router/drouterlite/MainActivity.kt#L127-L128
* 跳转服务
    https://github.com/oOJohn6Oo/DRouterLite/blob/f59c2292c855f559560158eb2d02bec5b91d62e8/app/src/main/kotlin/io/john6/router/drouterlite/MainActivity.kt#L54

### 模块说明

| 模块 | 说明 |
| --- | --- |
| app | APP Demo 模块 |
| out/mylibrary | APP 测试子模块 |
| drouter-api | DRouterLite API 核心模块 |
| drouter-api-annotation | DRouterLite API 注解模块 |
| drouter-api-stub | RouterLite API 占桩模块 |
| plugin-assembler | 路由组装模块 |
| plugin-collector | 路由收集模块，使用 KSP 实现 |
| plugin-common-module | 提供模块通用配置 |


### 调试说明

* `local.properties` 中 `dRouterLiteLocalTest` 为 true 使用本地测试
* 测试通过 [DRouterLiteTestSuite]



[DRouter]: https://github.com/didi/DRouter
[KSP]: https://github.com/google/ksp
[DRouterLiteTestSuite]: ./app/src/androidTest/kotlin/io/john6/router/drouterlite/DRouterLiteTestSuite.kt

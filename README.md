## 轻量 Android 路由框架

使用 [DRouter] API + AGP8.0 API + [KSP] 实现的轻量级 Android 路由框架

### 实现功能

- [x] Activity 跳转
- [] Fragment 跳转 TODO
- [x] 服务发现

### 接入

1. 在 base 模块依赖 `drouter-lite-api`

``` kotlin
api("io.john6.router.drouterlite:api:1.0.0-alpha01")

```

2. 在所有需要路由收集的模块 build.gradle 中依赖 `collector` 插件

``` kotlin
plugins {
    id("com.google.devtools.ksp")
}
...
dependencies {
    ksp("io.john6.router.drouterlite:collector:1.0.0-alpha01")
}

```
3. 在 app 模块 build.gradle 中依赖 `assembler` 和 `collector` 插件

``` kotlin
plugins {
    id("io.john6.router.drouterlite.assembler")
}
```


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

* `gradle.properties` 中 `router_local_test` 为 true 使用本地测试



[DRouter]: https://github.com/didi/DRouter
[KSP]: https://github.com/google/ksp
[//]: # ([**📄README-EN**]&#40;README-EN.md&#41;)

<div>
    <img 
        src="./hyper-logo/hyper-logo.svg"
        width="200px"
        alt="hyper-logo"
    >
    更强？更简？不，我都要！
</div>

[![](https://img.shields.io/badge/JDK-17+-green?logo=Jdk)](https://gradle.org)
[![](https://img.shields.io/badge/Gradle-v7%2E6-g?logo=Gradle)](https://gradle.org)
[![](https://img.shields.io/badge/邮箱-482194973%40qq%2Ecom-orange?logo=Mail%2ERu)]()
[![](https://img.shields.io/badge/QQ群-929450805-blue?logo=Tencent%20QQ)](https://qm.qq.com/cgi-bin/qm/qr?k=M8BRtN-w29gUFbp83PHOOoHDmNga4miz&jump_from=webapi)

### 📢 声明

> hyper 是本项目及其所有子组件的统称
> 
> hyper-libs 为虚拟目录，仅是为了方便组件的分类而创建，其本身并不是一个有效的子组件
> 
> 本项目下所有有效的子组件（包括但不限于 logo、模块等）所使用的开源许可证均由其（指子组件本身）根目录下的许可证文件指定
> 
> 如有未明确指定开源许可证（指许可证文件不存在）的组件，均为仅托管内容且闭源的组件
> 
> 基于本项目的任何作品必须遵守其（指基于本项目的作品）所继承的组件（指本项目下的组件）所使用的开源许可证中的所有条款

-------------------------------------------------------------------------------

### 📜 简介

* 让你更加简单地开发出一个 `我的世界插件`
* 让你的插件功能更加强大
* ……

-------------------------------------------------------------------------------

### 📦 包含组件

| 模块                     | 简介                        | 开源许可证（闭源则无）        |
|------------------------|---------------------------|--------------------|
| hyper-logo             | hyper 项目集的专属 logo         | 无                  |
| hyper-gradle-util      | 开发 hyper 插件时必装的 gradle 插件 | Apache 2.0 License |
| hyper-commons          | 公共工具相关                    | Apache 2.0 License |
| hyper-annotations      | 全局注解相关                    | Apache 2.0 License |
| hyper-events-listeners | 事件与监听器相关                  | Apache 2.0 License |
| hyper-commands         | 命令相关                      | Apache 2.0 License |
| hyper-resources        | 资源相关                      | Apache 2.0 License |
| hyper-tasks-executors  | 任务与执行者相关                  | Apache 2.0 License |
| hyper-stores           | 数据存储相关                    | Apache 2.0 License |
| hyper-core             | hyper 插件在编译和运行时的必要核心前置    | GPLv3              |
| ……                     | 更多模块敬请期待                  | 无                  |

hyper 插件：指依赖 hyper 项目下任何组件的 `我的世界插件`

-------------------------------------------------------------------------------

[//]: # (### ⚙ 使用)

[//]: # ()

[//]: # (#### Maven)

[//]: # (```xml)

[//]: # (    <!-- pom.xml -->)

[//]: # (    <dependency>)

[//]: # (        <groupId>pres.ketikai.hyper</groupId>)

[//]: # (        <artifactId>hyper</artifactId>)

[//]: # (        <version>${version}</version>)

[//]: # (    </dependency>)

[//]: # (```)

[//]: # ()

[//]: # (#### Gradle)

[//]: # (```kotlin)

[//]: # (    /* build.gradle.kts */)

[//]: # (    dependencies {)

[//]: # (        compileOnly&#40;"pres.ketikai.hyper:hyper:${version}"&#41;)

[//]: # (    })

[//]: # (```)

[//]: # ()

[//]: # (### ⬇️ 下载)

[//]: # ()

[//]: # (* 暂无)

### 🧩 编译

1. [访问 hyper 项目主页](https://github.com/ketikai/hyper)
2. 选择 `master` 或 `dev` 分支，下载项目源码
3. 进入项目根目录 `cd ./hyper`
4. 执行 gradle 打包任务，如下：

```shell
    # 打包所有模块
    ./gradlew clean jar
    
    # 打包指定模块
    ./gradlew clean ${module_name}:jar
```

-------------------------------------------------------------------------------

### 🐞 反馈

> 反馈 bug 时请提供出现问题时所使用的 jdk 、hyper 和其他相关依赖库的版本、配置等信息。

* [GitHub Issue](https://github.com/ketikai/hyper/issues)

### 📝 贡献

非常高兴有人会为 hyper 贡献，不过为了让 hyper 看起来更棒，就不得不提出一些相应的规范。
仔细阅读下列内容，对你所贡献的内容是否能够通过审核很有帮助！

> #### 📏 一些规范
>
> * 重要！！！尽量避免使用任何来自第三方库的内容（除了 `hyper-core` 和 `hyper-commons` 模块）
> * 统一缩进，即 4 个空格
> * 任何可能会被开放调用的类、方法等内容，都需要为其添加说明注释（包括但不限于描述、参数、返回值、异常等必要说明）
> * 贡献者可以在其添加或修改的内容上的说明注释中留下其名字，但不能随意地更改或删除已存在的其他贡献者的名字
> * 只有 `dev` 分支会接受贡献请求
> * 待补充……

#### 📌 步骤说明

1. `fork` 项目并 `clone` 项目到本地
2. 切换到 `dev` 分支，编辑你需要修改的部分
3. 提交并推送 `dev` 分支的改动到你 `fork` 后所创建的仓库
4. 点击 GitHub 页面顶部栏的 `pull request` ，认真填写与改动部分有关的说明信息后提交
5. 等待维护者审核通过后合并

[//]: # ()

[//]: # (-------------------------------------------------------------------------------)

[//]: # ()

[//]: # (### 📖 文档)

[//]: # ()

[//]: # (* [Java Doc]&#40;https://javadoc.github.com/ketikai/hyper&#41;)

[//]: # ()

[//]: # (-------------------------------------------------------------------------------)

[//]: # ()

[//]: # (### 🪙 捐赠)

[//]: # ()

[//]: # (* 暂无)

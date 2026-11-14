# 07 选择题知识点

> 这个文件管什么题：只在选择题或开放题里出现、不用写代码的主题，包括 Android、契约式设计和 JML、知识产权和许可证、职业道德、Git、软件构造的范围、性能分析和复杂度。树、文法、测试、设计模式、SOLID 的选择题在别的文件里。
> 历年出现：Android 2021 S2、2024 S1、2025 S1、2025 S2；JML 2025 S1、2025 S2；知识产权 2022 S1、2024 S1、2025 S1；职业道德 2024 S1、2024 S2；Git 2019 S1、2025 S1；软件构造的范围 2024 S1。今年预测：选择题一组，Android 三套全考过，JML、知识产权、职业道德大概率出现。
> 所有答案都是非官方答案。本学期课件看不到，标了「以课件为准」的地方要对照课件。

## 目录

- [1 先认题型](#1-先认题型)
- [2 零基础速懂](#2-零基础速懂)
  - [2.1 Android](#21-android)
  - [2.2 契约式设计和 JML](#22-契约式设计和-jml)
  - [2.3 知识产权和许可证](#23-知识产权和许可证)
  - [2.4 职业道德](#24-职业道德)
  - [2.5 Git 和 Makefile](#25-git-和-makefile)
  - [2.6 软件构造的范围](#26-软件构造的范围)
  - [2.7 性能分析和复杂度](#27-性能分析和复杂度)
- [3 套路](#3-套路)
  - [3.1 选择题通用做法](#31-选择题通用做法)
  - [3.2 答案文件填写和格式检查](#32-答案文件填写和格式检查)
  - [3.3 Android 回调顺序题](#33-android-回调顺序题)
  - [3.4 JML 和契约题](#34-jml-和契约题)
  - [3.5 知识产权和许可证题](#35-知识产权和许可证题)
  - [3.6 伦理情境题](#36-伦理情境题)
  - [3.7 Git 图题](#37-git-图题)
  - [3.8 范围题和复杂度题](#38-范围题和复杂度题)
- [4 真题套用](#4-真题套用)
  - [4.1 2025 S2 Q1 MCQ2 启动新 Activity 后旧 Activity 怎样](#41-2025-s2-q1-mcq2-启动新-activity-后旧-activity-怎样)
  - [4.2 2025 S2 Q1 MCQ4 JML 不能做什么](#42-2025-s2-q1-mcq4-jml-不能做什么)
  - [4.3 2025 S1 Q4 MCQ1 Git 图](#43-2025-s1-q4-mcq1-git-图)
  - [4.4 2025 S1 Q4 MCQ5 Android 多语言](#44-2025-s1-q4-mcq5-android-多语言)
  - [4.5 2025 S1 Q4 MCQ9 JML 注解写法](#45-2025-s1-q4-mcq9-jml-注解写法)
  - [4.6 2025 S1 Q4 MCQ10 没有许可证的 GitHub 代码](#46-2025-s1-q4-mcq10-没有许可证的-github-代码)
  - [4.7 2024 S2 Q4 开放题的伦理小问](#47-2024-s2-q4-开放题的伦理小问)
  - [4.8 2024 S1 Q4 MCQ1 软件构造包括什么](#48-2024-s1-q4-mcq1-软件构造包括什么)
  - [4.9 2024 S1 Q4 MCQ4 澳洲知识产权](#49-2024-s1-q4-mcq4-澳洲知识产权)
  - [4.10 2024 S1 Q4 MCQ8 IEEE 职业道德](#410-2024-s1-q4-mcq8-ieee-职业道德)
  - [4.11 2024 S1 Q4 MCQ12 Android 综合判断](#411-2024-s1-q4-mcq12-android-综合判断)
  - [4.12 2022 S1 Q4 许可证小问](#412-2022-s1-q4-许可证小问)
  - [4.13 2021 S2 Q5 Activity 生命周期开放题](#413-2021-s2-q5-activity-生命周期开放题)
  - [4.14 2019 S1 Q2 Git 提交和 Makefile](#414-2019-s1-q2-git-提交和-makefile)
- [5 易错清单](#5-易错清单)

## 1 先认题型

| 题干里出现 | 主题 | 查事实 | 套路 | 最像的真题 |
|---|---|---|---|---|
| `Activity`、`onCreate`、`onPause`、back stack、`Intent`、`AndroidManifest.xml`、`strings.xml`、Toast | Android | 2.1 | 3.3 | 4.1、4.4、4.11 |
| JML、design by contract、precondition、postcondition、invariant、`requires`、`ensures`、`@param` | 契约式设计和 JML | 2.2 | 3.4 | 4.2、4.5 |
| copyright、patent、trade mark、license、MIT、GPL、copyleft、moral rights、GitHub | 知识产权和许可证 | 2.3 | 3.5 | 4.6、4.9 |
| ethics、IEEE、ACM、Code of Ethics、public interest、privacy、consent | 职业道德 | 2.4 | 3.6 | 4.10、4.7 |
| `git`、commit、merge、fetch、push、`origin/`、conflict、Makefile | Git | 2.5 | 3.7 | 4.3、4.14 |
| Software Construction includes | 软件构造的范围 | 2.6 | 3.8 | 4.8 |
| Big-O、complexity、`O(n)`、profiling、performance、bottleneck | 性能分析和复杂度 | 2.7 | 3.8 | 没考过选择题 |

各年的计分方式（考试时以当年题目首页和 README 为准）：

| 试卷 | 答案文件 | 单选还是多选 | 计分 |
|---|---|---|---|
| 2024 S1 Q4，14 题 | `src/Q4/Q4Answers.txt` | 多选，每题至少一个正确选项 | 选的和标准答案完全一致得 1 分，多选或漏选都是 0 分，不答 0 分 |
| 2025 S1 Q4，10 题 | `src/Q4/Q4Answers.txt` | 全部单选 | 每题等分，答错不倒扣 |
| 2025 S2 Q1，5 题 | `src/Q1/Q1Answers.txt` | MCQ1 写着 select all the correct statements，其余四题是单选的问法 | 题面没写计分规则；按全对才得分来准备 |

答案文件的格式和检查方法见 3.2。

## 2 零基础速懂

### 2.1 Android

Activity 是 App 的一个屏幕，包含界面和它的逻辑。写法是 `class MainActivity extends AppCompatActivity`，在 `onCreate` 里调用 `setContentView(R.layout.activity_main)` 加载界面。生命周期回调（lifecycle callback）由系统调用，不是自己调用。

| 回调 | 系统什么时候调用 | 调用后的状态 |
|---|---|---|
| `onCreate()` | Activity 第一次被创建，只调一次。在这里初始化。它是唯一必须实现的回调 | Created，不可见 |
| `onStart()` | 即将对用户可见 | Started，可见 |
| `onResume()` | 到了前台，可以和用户交互 | Resumed，运行中 |
| `onPause()` | 失去焦点，要离开前台，例如别的 Activity 要盖上来 | Paused，可能仍部分可见 |
| `onStop()` | 完全不可见，例如被新 Activity 盖满、按了 Home 键 | Stopped，仍在内存里 |
| `onRestart()` | 从 Stopped 回到前台之前，后面一定接 `onStart()` | 无 |
| `onDestroy()` | 销毁之前：按返回键、调用 `finish()`、旋转屏幕。系统直接杀进程时可能不调 | Destroyed |

成对记：`onCreate` 对 `onDestroy`，`onStart` 对 `onStop`，`onResume` 对 `onPause`。

| 场景 | 回调顺序 |
|---|---|
| 启动 App | `onCreate`，`onStart`，`onResume` |
| A 启动 B，B 盖满屏幕 | A.`onPause`；B.`onCreate`，B.`onStart`，B.`onResume`；A.`onStop`。A 留在返回栈里，不销毁 |
| A 启动的 B 是透明的或对话框样式 | A 只走 `onPause`，不走 `onStop` |
| 在 B 按返回键回到 A | B.`onPause`；A.`onRestart`，A.`onStart`，A.`onResume`；B.`onStop`，B.`onDestroy` |
| 按 Home 键退到后台 | `onPause`，`onStop` |
| 从后台回来 | `onRestart`，`onStart`，`onResume` |
| 旋转屏幕（configuration change） | `onPause`，`onStop`，`onDestroy`，然后新实例 `onCreate`，`onStart`，`onResume`。界面状态用 `onSaveInstanceState(Bundle)` 保存 |
| 最后一个 Activity 按返回键 | `onPause`，`onStop`，`onDestroy`（Android 12 起根 Activity 可能只退到后台，以课件为准） |

返回栈（back stack）：后进先出。`startActivity` 把新 Activity 压到栈顶；返回键或 `finish()` 弹出栈顶并销毁它，下面那个回到前台。

| 名词 | 是什么 | 选择题怎么判断 |
|---|---|---|
| Intent | 组件之间传的消息，用来启动 Activity | 显式（explicit）必须给目标类：`new Intent(this, B.class)`。隐式（implicit）只给动作如 `ACTION_VIEW`，由系统找能处理的 App。数据用 `putExtra` 带过去 |
| `AndroidManifest.xml` | App 的清单文件 | 声明四大组件（Activity、Service、BroadcastReceiver、ContentProvider）、权限 `<uses-permission>`、启动入口。漏声明权限或 Activity 是运行时出错，不是编译错误 |
| 布局 XML | `res/layout/*.xml`，描述界面 | `LinearLayout` 排成一行或一列；`ConstraintLayout` 让 View 相对父容器或别的 View 定位。代码里用 `findViewById(R.id.x)` 取 View |
| 资源限定符目录 | `res/` 下带后缀的目录 | `values/strings.xml` 是默认文字，`values-es/strings.xml` 是西班牙语，`layout-land/` 是横屏。系统按设备配置自动选，找不到就用默认 |
| 字符串资源 | 文字不写死在代码里 | XML 里写 `@string/welcome`，代码里写 `getString(R.string.welcome)` |
| Listener | 事件监听器 | 任何 View 都能设，如 `setOnClickListener`，不限于按钮和输入框 |
| Toast | 短暂的浮动提示 | `Toast.makeText(this, "msg", Toast.LENGTH_SHORT).show()`，自动消失，不能交互 |
| 主线程（UI thread） | 负责更新界面的线程 | 网络、数据库等耗时操作不能放在主线程，否则界面卡死（ANR） |
| Fragment | Activity 里可复用的一块界面 | 有自己的生命周期，必须依附于 Activity |

### 2.2 契约式设计和 JML

契约式设计（design by contract）把方法看成调用者和方法之间的合同。JML（Java Modeling Language）是在 Java 注释里写这种合同的语言。

| 术语 | 是什么 | 谁负责 | JML 关键字 |
|---|---|---|---|
| 前置条件（precondition） | 调用前必须成立的条件 | 调用者 | `requires` |
| 后置条件（postcondition） | 方法正常返回后保证成立的条件 | 方法 | `ensures` |
| 类不变式（class invariant） | 每个公开方法调用前后都成立的条件 | 类 | `invariant` |

```java
public class Account {
    private /*@ spec_public @*/ int balance;      // 私有字段允许出现在公开规格里
    //@ public invariant balance >= 0;            // 不变式：余额永远不为负

    //@ requires amount > 0 && amount <= balance; // 前置条件：调用者保证
    //@ ensures balance == \old(balance) - amount; // 后置条件：\old 是调用前的值
    //@ ensures \result == balance;                // \result 是返回值
    public int withdraw(int amount) {
        balance -= amount;
        return balance;
    }
}
```

| 写法 | 含义 |
|---|---|
| `//@ ...` 或 `/*@ ... @*/` | JML 只写在这两种注释里，放在方法声明前面。`javac` 把它当普通注释忽略 |
| `\result`、`\old(e)` | 返回值、调用前 `e` 的值。只能用在 `ensures` 里，前面有反斜杠 |
| `\forall int i; 0 <= i && i < a.length; a[i] >= 0` | 对所有 `i` 成立。`\exists` 是存在某个 `i` |
| `==>` | 蕴含，左边成立则右边成立 |
| `assignable x;` | 方法只允许修改 `x`。`assignable \nothing;` 是什么都不改 |
| `signals (Exception e) 条件;` | 抛出异常时保证的条件 |
| `/*@ pure @*/` | 方法没有副作用 |
| `/*@ non_null @*/`、`/*@ nullable @*/` | 不能为 `null`、可以为 `null` |
| `also` | 子类重写方法时，在父类规格上追加 |
| `(* 文字 *)` | JML 自己的非形式化描述 |

| JML 能做 | JML 不能做 |
|---|---|
| 形式化地写出前置条件、后置条件、不变式 | 实现业务功能，如处理 XML、JSON、网络、界面 |
| 配合 OpenJML 等工具做运行时断言检查（runtime assertion checking）和静态验证 | 不靠工具自己生效，`javac` 完全忽略它 |
| 记录详细的设计决定，当精确的文档 | 自动修好代码，或保证程序一定正确 |
| 支持契约式设计，辅助生成单元测试 | 描述性能和界面外观 |

和 Javadoc 的关系：Javadoc 写在 `/** ... */` 里，用 `@param 参数名 描述`、`@return 描述`、`@throws 异常类 描述`，是给人读的自然语言，不能被检查。JML 是形式化的，能被工具检查。两者可以写在同一个方法上面。2025 S1 把 `@param` 当作「JML 里给参数写非形式化描述」的写法来考。

其他规则：

- 前置条件不成立时，方法不承诺任何结果。
- 子类重写方法：前置条件只能放宽，后置条件只能加强，不变式要保持。这和里氏替换原则一致，见 [06 SOLID 和重构](06-solid-refactoring.md)。
- 防御式编程（defensive programming）是方法自己检查参数并抛异常。契约式设计把检查的责任交给调用者。
- Java 自带的 `assert 条件 : "消息";` 要用 `java -ea` 运行才生效。

### 2.3 知识产权和许可证

澳洲的细节以课件为准。

| 类型 | 保护什么 | 怎样取得 | 期限 |
|---|---|---|---|
| 版权（copyright） | 表达形式：源代码、文档、图片。不保护想法和算法本身 | 作品写出来就自动产生，不用注册，免费 | 作者终身加 70 年 |
| 专利（patent） | 发明。要求 new、useful、inventive、suitable subject matter | 必须申请并通过审查，要公开技术细节 | 标准专利 20 年 |
| 商标（trade mark） | 名称、logo 等标识 | 注册 | 10 年，可一直续 |
| 商业秘密（trade secret） | 没公开的算法、配方、客户名单 | 保密措施和保密协议 | 保密多久就多久 |

- 版权归属：雇员在工作中写的代码默认归雇主。外包人员写的默认归本人，除非合同另有约定。
- 精神权利（moral rights）：署名权（attribution）、反对冒名、保护作品完整（integrity）。属于作者个人，是非经济权利，不能转让。澳洲把它写在版权法（Copyright Act 1968）里。
- 独立创作（independent creation）不算侵权：两个人各自独立写出同样的作品都合法，但要能证明是独立写的。
- 澳洲没有美国式的 fair use，只有 fair dealing，限于研究学习、批评评论、新闻报道、讽刺模仿等目的。
- 软件专利：纯算法、数学方法、商业方案不能申请，要有技术上的创新。这条各处说法不一，以课件为准。

| 许可证 | 类别 | 用它的代码后能否闭源 | 主要义务 |
|---|---|---|---|
| 没有许可证 | 作者保留所有权利（all rights reserved） | 不能用 | 先取得作者授权 |
| public domain、CC0、Unlicense | 作者放弃权利 | 能 | 无 |
| MIT、BSD | 宽松（permissive） | 能 | 保留版权声明和许可证文本 |
| Apache 2.0 | 宽松 | 能 | 同上，还要标明改动；附带专利授权 |
| LGPL、MPL | 弱 copyleft | 只是调用它的程序能闭源 | 对库本身的修改要开源 |
| GPL | 强 copyleft | 不能 | 分发衍生作品时必须同样用 GPL 并提供源码 |
| AGPL | 强 copyleft | 不能 | 同 GPL，通过网络提供服务也算分发 |
| Creative Commons | 用于文档和图片，不推荐用于代码 | 看条款 | BY 署名，SA 相同方式共享，NC 非商用，ND 禁止改编 |

- copyleft：可以自由使用、修改、分发，但衍生作品必须沿用同样的许可证。它不禁止收费。
- 开源（open source）不等于公有领域。开源代码仍然有版权，只是作者通过许可证授权别人使用。
- 宽松许可证的代码可以放进 GPL 项目。GPL 的代码不能放进闭源项目。

### 2.4 职业道德

ACM 和 IEEE-CS 的 Software Engineering Code of Ethics 有八条原则。题目会引用编号和英文名。

| 编号 | 英文名 | 一句话 | 违反的例子 |
|---|---|---|---|
| 1 | PUBLIC | 行为符合公众利益（public interest） | 隐瞒危害用户安全或隐私的问题 |
| 2 | CLIENT AND EMPLOYER | 在符合公众利益的前提下，为客户和雇主的最大利益做事 | 泄露雇主机密；拿「为雇主好」当理由损害公众 |
| 3 | PRODUCT | 产品达到尽可能高的专业标准 | 明知有 bug、没充分测试就发布 |
| 4 | JUDGMENT | 保持专业判断的诚信（integrity）和独立 | 有利益冲突不申报；收好处后说好话 |
| 5 | MANAGEMENT | 管理者用合乎道德的方式管理开发和维护 | 定不现实的工期，逼下属违规 |
| 6 | PROFESSION | 维护行业的诚信和声誉 | 夸大产品能力 |
| 7 | COLLEAGUES | 公平对待并支持同事 | 抢同事的功劳 |
| 8 | SELF | 终身学习，提升自己的职业水平 | 接不胜任的工作又不学习 |

规则：公众利益排第一。第 2 条原文带着 consistent with the public interest，所以雇主利益压不过公众的安全、隐私、健康。发现危险要向有关的人或机构披露。

### 2.5 Git 和 Makefile

| 名词 | 是什么 |
|---|---|
| commit | 一次提交，是项目的一个快照，记着父提交（parent）、作者、说明 |
| branch | 指向某个 commit 的可移动指针。在分支上新提交后，指针自动前移 |
| `HEAD` | 当前所在的位置，通常指向当前分支 |
| staging area | 暂存区。`git add` 把修改放进去，`git commit` 把暂存区存成一个 commit |
| remote、`origin` | 远程仓库。`origin` 是 `git clone` 时默认的远程名 |
| remote-tracking branch | 远程跟踪分支，如 `origin/main`。本地记录的「上次联系时远程分支在哪」，只在 fetch、pull、push 成功时更新 |
| `git fetch` | 下载远程的新提交，只更新 `origin/xxx`，不动本地分支和工作文件 |
| `git merge X` | 把 X 合进当前分支。当前分支没有新提交时是快进（fast-forward），不产生新 commit。双方都有新提交时产生一个有两个父提交的 merge commit |
| `git pull` | 等于 `git fetch` 加 `git merge` |
| `git push` | 上传本地提交。远程有本地没有的提交时会被拒绝（rejected），要先 fetch 加 merge |
| 合并冲突（conflict） | 双方改了同一文件的同一处，Git 不能自动合并，要人工解决 |
| `git rebase` | 把自己的提交挪到新起点上重放，历史变成直线，提交的哈希会变 |
| `.gitignore` | 列出不纳入版本控制的文件，如 `*.class` |

冲突时文件里会出现三行标记。解决方法：改成最终内容并删掉这三行，`git add 文件`，再 `git commit`。

```text
<<<<<<< HEAD
我这边的内容
=======
对方的内容
>>>>>>> origin/my_branch
```

读 `git log --graph --oneline`：

- 上面新，下面旧。`*` 是一个 commit，竖线和斜线连着子提交和父提交。
- 某个 `*` 的下一行是 `|\`：这个 commit 有两个父提交，是 merge commit。
- `|/`：两条线汇到下面同一个 commit，它是共同祖先。
- merge commit 正下方那条线通向第一个父提交，也就是执行 merge 时自己所在的分支。右边分出去的线通向被合并进来的提交。
- 括号里 `HEAD -> b` 表示当前在分支 b；`origin/b` 表示远程跟踪分支指向这里。

Makefile 最简写法（2019 S1 考过）。格式是「目标: 依赖」，下一行写命令，命令前必须是一个 Tab，不能用空格。只敲 `make` 时执行第一个目标。

```makefile
all:
	javac Demo.java

clean:
	rm -f *.class

rundemo: all
	java Demo

.PHONY: all clean rundemo
```

### 2.6 软件构造的范围

定义（来自 SWEBOK，以课件为准）：软件构造（software construction）是通过编码（coding）、验证（verification）、单元测试（unit testing）、集成测试（integration testing）和调试（debugging），详细地创建可工作的软件。

| 属于软件构造 | 不属于，是别的阶段 |
|---|---|
| coding、debugging | problem definition（问题定义）、requirements（需求） |
| unit testing、integration testing | system testing、acceptance testing（系统测试、验收测试） |
| detailed design、modular design（详细设计、模块化设计） | software architecture（高层架构设计） |
| construction planning（构造阶段自己的计划） | 项目管理层面的 planning（不确定，见 4.8） |
| construction for reuse、construction with reuse | maintenance（维护）、deployment（部署） |
| use of standards、readability、minimizing complexity | |
| 工具：IDE、单元测试工具、profiling 和 performance analysis 工具 | |
| 技术：design by contract、异常处理、parsing、API 设计、performance tuning | |

### 2.7 性能分析和复杂度

大 O（Big-O）描述输入规模 `n` 变大时，运行时间或内存增长的量级。只看增长最快的一项，去掉常数：`3n² + 5n + 2` 是 O(n²)。

| 记号 | 代码特征 | 例子 |
|---|---|---|
| O(1) | 没有循环，或循环次数固定 | 数组按下标取值 |
| O(log n) | 每一轮把规模减半 | 二分查找，平衡树查找 |
| O(n) | 一层循环 | 遍历列表，遍历一棵树 |
| O(n log n) | 外层 n 次，内层 log n | 归并排序，快速排序的平均情况 |
| O(n²) | 两层嵌套循环 | 冒泡排序、插入排序、选择排序 |
| O(2ⁿ) | 每次递归分成两个，规模只减 1 | 枚举所有子集 |

| 数据结构 | 按下标访问 | 查找 | 插入 | 删除 |
|---|---|---|---|---|
| 数组、`ArrayList` | O(1) | O(n)，有序时二分 O(log n) | 末尾均摊 O(1)，中间 O(n) | O(n) |
| 链表 `LinkedList` | O(n) | O(n) | 头尾或已知位置 O(1) | 头尾或已知位置 O(1) |
| 哈希表 `HashMap` | 无 | 平均 O(1)，最坏 O(n) | 平均 O(1)，最坏 O(n) | 平均 O(1)，最坏 O(n) |
| 普通 BST | 无 | 平均 O(log n)，最坏 O(n) | 同查找 | 同查找 |
| AVL、红黑树（`TreeMap`） | 无 | 最坏 O(log n) | 最坏 O(log n) | 最坏 O(log n) |
| 栈、队列 | 无 | O(n) | O(1) | O(1) |

- 普通 BST 按升序插入会退化成一条链，高度是 n，所以最坏 O(n)。
- AVL 平衡得更严，查找略快。红黑树旋转更少，插入删除略快。
- 最好（best）、最坏（worst）、平均（average）是同一算法在不同输入下的表现。均摊（amortized）是一串操作的平均成本。
- 性能剖析（profiling）：运行程序时测量每个方法花的时间、调用次数、内存分配，找出热点（hotspot）和瓶颈（bottleneck）。工具有 IntelliJ Profiler、VisualVM、Android Profiler。
- 复杂度分析是理论预测，profiling 是实测，两者互补。先测量，再优化。

## 3 套路

### 3.1 选择题通用做法

适用：所有选择题。

1. 先看首页的计分说明：单选还是多选，答错是否倒扣。
2. 圈出题干里的方向词：NOT、INCORRECT、cannot、does NOT apply、select all、plausible、best。
3. 每个选项单独判断，在草稿上写 T 或 F 和一个理由。不要看到一个像的就停。
4. 选项里有 only、always、never、must、whatsoever、immediately 这类绝对词时，先怀疑它是错的。
5. 一个选项有两句话时，两句都对才算对。
6. 方向词是 NOT 或 INCORRECT 时，最后选的是标 F 的那些。
7. 单选不倒扣：排除后不管剩几个都要填一个。多选全对才得分：每一项都要定下来，拿不准的回第 2 节查表。
8. 填答案文件并跑语法测试，见 3.2。

### 3.2 答案文件填写和格式检查

适用：交选择题答案。

1. 打开 README 指定的文件（`Q1Answers.txt` 或 `Q4Answers.txt`），里面已有 `1.` 到最后一题的空行。
2. 每行写「题号. 选项」。多选用逗号分隔。不答的题保留题号。
3. 不要增删行，不要留空行，不要改题号顺序。
4. 运行同目录的 `MCQAnswerSyntaxTest`。如果报找不到文件，按测试里 TODO 注释的提示改 `FILE_PATH`。
5. commit 并 push。

```text
1. A,B
2. C
3.
4. D
```

| 写法 | 是否合法 |
|---|---|
| `1. A,B`、`1. a, b`、`1.A`、`3.` | 合法。大小写、空格都可以，题号后必须有点 |
| `1. AB`、`1. A B`、`1. A;B`、`1. A,` | 不合法。多选只能用逗号分隔，末尾不能多逗号 |
| `1) A`、`Q1. A`、`1 A`、空行 | 不合法 |
| `1. N` | 不合法。选项字母只接受 A 到 M |

### 3.3 Android 回调顺序题

适用：问某个操作之后调用哪些回调、Activity 处于什么状态。

1. 确定事件是哪一种：启动、被别的 Activity 盖住、按返回键、按 Home 键、旋转屏幕、从后台回来。
2. 查 2.1 的场景表，写出顺序。
3. 记不住时按三句话推：进入是 Create、Start、Resume；离开是 Pause、Stop、Destroy；重回是 Restart、Start、Resume。
4. 两个 Activity 切换时，旧的先 `onPause`，新的走完三步，旧的才 `onStop`。
5. 判断是否销毁：只有返回键、`finish()`、旋转屏幕、系统回收内存才会 `onDestroy`。只是被盖住不会销毁。
6. 不是生命周期的 Android 题，查 2.1 的名词表。重点看两点：出错是在编译时还是运行时；有没有 only、must 这类绝对词。

### 3.4 JML 和契约题

适用：题干出现 JML、design by contract、precondition、postcondition。

1. 问「能不能做」：JML 只做规格说明和检查。选项说的是业务功能（XML、网络、界面）就是做不到的。
2. 问语法：JML 写在 `//@` 或 `/*@ @*/` 里，关键字是 `requires`、`ensures`、`invariant`，`\result` 和 `\old` 带反斜杠。参数的文字说明是 Javadoc 的 `@param 参数名 描述`。
3. 问谁负责：前置条件归调用者，后置条件归方法。
4. 给了规格问某次调用是否合法：把实参代进 `requires`，成立才合法；再用 `ensures` 算返回后的状态。
5. 问子类：前置条件放宽，后置条件加强。

### 3.5 知识产权和许可证题

适用：题干出现 copyright、patent、license、copyleft、GitHub 上的代码。

1. 先分清说的是哪种权利，查 2.3 第一张表：自动产生的是版权，要申请的是专利和商标。
2. 问能不能用别人的代码：先看有没有许可证。没有就是不能用。有就查 2.3 的许可证表。
3. 「公开可见」「注明了出处」「不收费」都不等于有权使用。
4. 看到 copyleft：关键是衍生作品沿用同样的许可证并公开源码，不是「不许收费」。MIT、BSD、Apache 不是 copyleft。
5. 题干写 Australian context：版权自动产生、不用注册；有精神权利；没有 fair use。

### 3.6 伦理情境题

适用：给出工程师的做法，问是否符合 IEEE 或 ACM 的道德规范。

1. 找出这个做法让谁受损。优先级是公众和用户的安全、隐私、知情同意，其次是客户和雇主，最后是自己。
2. 损害了公众的做法不符合规范，不管选项引用了哪条原则当理由。
3. 核对选项引用的原则编号和名字，查 2.4 的表。引用错了也算错。
4. 符合规范的典型做法：披露风险、拒绝做侵犯隐私的项目、如实报告测试结果、申报利益冲突。
5. 开放题要求提出一个伦理问题时，写四句：问题是什么，谁受害，违反哪条原则，怎样补救。例子见 4.7。

### 3.7 Git 图题

适用：给出 `git log --graph` 的输出，判断哪个说法成立。

1. 自下而上标时间：最下面最早。
2. 找有两个父提交的 commit。只有它是 `git merge` 创建的，其他提交都是 `git commit` 创建的。
3. 看括号。`origin/x` 指着的提交是远程上已经有的，也就是别人先推上去的。
4. merge commit 正下方的父提交是执行 merge 的人自己的本地提交，另一个父提交是被合并进来的。
5. 共同祖先是谁写的，图上看不出来。题目问 plausible 时，和图不矛盾就算成立。

### 3.8 范围题和复杂度题

适用：问某项活动是否属于软件构造；问一段代码或一种数据结构操作的复杂度。

1. 范围题：查 2.6 的表。编码、单元测试、集成测试、调试、详细设计、复用、标准、工具都属于；问题定义和需求不属于。
2. 代码的复杂度：一层循环是 O(n)，嵌套相乘，每轮减半是 O(log n)，前后两段取大的，最后去掉常数和低次项。
3. 数据结构操作：查 2.7 的表。先看题目问的是 average 还是 worst。
4. 题目只说 BST、没说 balanced 时，最坏是 O(n)。AVL 和红黑树最坏是 O(log n)。
5. profiling 题：结论通常是先测量找到瓶颈，再优化那一处。

## 4 真题套用

选择题原题：[2025 S2 MCQQuestions.pdf](../../exam-practice-questions/past-exams/2025-2/src/Q1/MCQQuestions.pdf)、[2025 S1 MC_Questions_2025_S1.pdf](../../exam-practice-questions/past-exams/2025-1/src/Q4/MC_Questions_2025_S1.pdf)、[2024 S1 Q4README.md](../../exam-practice-questions/past-exams/2024-1/src/Q4/Q4README.md)。

### 4.1 2025 S2 Q1 MCQ2 启动新 Activity 后旧 Activity 怎样

**题意**：单选。启动新 Activity 后，栈里前一个 Activity 怎样。原题 [MCQQuestions.pdf](../../exam-practice-questions/past-exams/2025-2/src/Q1/MCQQuestions.pdf) 第 2 题。选项：立刻销毁并调用 `onPause`、`onStop`、`onDestroy`；压入返回栈并暂停；仍保持活动，不调用生命周期方法；从栈里移除但留在内存，只调用 `onStop`。

**套路对应**：用 3.3。事件是 A 启动 B，B 盖满屏幕。查 2.1：A 先 `onPause`，B 走完 `onCreate`、`onStart`、`onResume`，A 再 `onStop`。A 留在返回栈，不销毁。

**推导**：第一项不成立。被盖住不会 `onDestroy`。第二项最接近：旧 Activity 离开前台，并留在返回栈。第三项不成立。`onPause` 和 `onStop` 都会调用，它不再是 Resumed。第四项不成立。它不被移出栈，而且 `onPause` 在 `onStop` 之前。非官方答案：b。不确定：盖满屏幕后旧 Activity 会到 Stopped，不只停在 paused。被压进栈的是新 Activity，旧的是本来就在栈里。四个选项都没写成这样。b 最接近。

**坑**：不要因为记得还有 `onStop`，就改选「立刻销毁」或「只调用 `onStop`」。前者多了销毁，后者多了移出栈。

### 4.2 2025 S2 Q1 MCQ4 JML 不能做什么

**题意**：单选。哪一件不能用 JML 完成。原题同上 PDF 第 4 题。选项：形式化写出前置条件、后置条件和不变式；做运行时断言检查；记录详细设计决定；支持契约式设计；处理 Java 的 XML。

**套路对应**：用 3.4 第 1 步。JML 只写规格、配合工具做检查。选项若是业务功能，就是做不到的。

**推导**：前四项都能做。`requires`、`ensures`、`invariant` 就是规格。OpenJML 可以把规格变成运行时断言。规格本身就是设计记录，也就是契约式设计。处理 XML 做不到，注释里的规格不会解析或生成 XML。非官方答案：e。

**坑**：`javac` 会忽略 JML，所以运行时检查容易被看成做不到。题目说的是 using JML，检查由工具完成，不是由 `javac` 完成。

### 4.3 2025 S1 Q4 MCQ1 Git 图

**题意**：单选，问哪句 plausible。Bernardo 先推送冲突改动。Dylan 推送被拒后用 `git fetch` 和 `git merge` 合并，再推送，并用 `git log --graph --oneline` 看图。图上 A 在最上，标着 `HEAD -> my_branch`，有两条父线。右边 B 标着 `origin/my_branch`，左边 C，最下是 D。原题 [MC_Questions_2025_S1.pdf](../../exam-practice-questions/past-exams/2025-1/src/Q4/MC_Questions_2025_S1.pdf) 第 1 题。

**套路对应**：用 3.7。下面旧，上面新。两个父提交的才是 merge。`origin/my_branch` 指着的是远程已经有的提交。merge 时自己所在分支的提交是第一个父提交。

**推导**：按题面建了两个克隆。Bernardo 提交 D 并推送，Dylan 本地提交 C，Bernardo 再提交 B 并推送。Dylan 的 push 被拒绝。fetch 之后 merge，冲突解决完、push 之前，`git log --graph --oneline` 是：

```text
*   A (HEAD -> my_branch)
|\
| * B (origin/my_branch)
* | C
|/
*   D
```

A 的父提交是 C 然后 B。B 和 C 的父提交都是 D。a 不成立，D 是最早的共同祖先，A 最新。b 不成立，只有 A 有两个父提交，B 是普通 commit。c 不成立，合并前 `origin/my_branch` 指向 B，C 只在 Dylan 本地。d 与图不矛盾。B 是 Bernardo 推上去的提交，D 可以是他更早的提交。非官方答案：d。不确定的是 D 的作者：图上没有作者名，不能证明一定是 Bernardo。a、b、c 和这次复现矛盾，所以最可能是 d。

**坑**：题面把 push 写在看图之前。push 成功后 `origin/my_branch` 会和 HEAD 一起指着 A。印出来的图是 merge 之后、跟踪分支被 push 更新之前。父子关系不变，不要因此改选 c。

### 4.4 2025 S1 Q4 MCQ5 Android 多语言

**题意**：单选。Android 应用怎样支持多种语言才是推荐做法。原题同上 PDF 第 5 题。a 用 Java 检测语言，再给每个 View 设文字。b 每种语言一份布局，在代码里切换。c 用字符串资源，翻译放在各自的 values 目录，例如 `values-es/strings.xml`。d 所有译文放进同一个 `strings.xml`，按用户输入加载。

**套路对应**：用 3.3 第 6 步，查 2.1 的资源限定符。布局写 `@string`，系统按设备语言选目录。

**推导**：a 把语言判断散落在代码里，加一种语言就要改代码。b 要为每种语言复制界面。d 自己在一个文件里切换，用不上资源系统。c 把默认文字放在 `res/values/strings.xml`，西班牙语放在 `values-es/strings.xml`，系统按 locale 选择。非官方答案：c。

**坑**：`layout-es` 可以存在。题目问的是推荐做法。推荐的是翻译 `strings.xml`，不是复制布局再在代码里切换。

### 4.5 2025 S1 Q4 MCQ9 JML 注解写法

**题意**：单选。`swap` 交换数组下标 `i` 和 `j` 的元素。方法上方 `/** */` 里有一个空位，要给参数 `i` 写非形式化描述。原题同上 PDF 第 9 题。a 是 `@parameter` 加引号再写 `i`。b 是 `@input i:` 加引号。c 是 `@param i The first index.` d 是 `@argument` 加等号。

**套路对应**：用 3.4 第 2 步。参数的文字说明是 `@param 参数名 描述`，写在 `/** */` 里。`requires` 和 `ensures` 才是形式化规格。

**推导**：a、b、d 都不是 Javadoc 或 JML 的标签，参数名的位置也不对。c 是 `@param`、参数名 `i`、描述，顺序正确。非官方答案：c。不确定：`@param` 是 Javadoc 标签，不是 `requires` 那种 JML 关键字。JML 自己的非形式化写法是 `(* 文字 *)`，四个选项里都没有。空位在 `/** */` 里，问的又是参数的文字描述，四个里只有 c 的语法成立。

**坑**：不要写成 `@param The first index i`。名字紧跟 `@param`。描述不加引号。

### 4.6 2025 S1 Q4 MCQ10 没有许可证的 GitHub 代码

**题意**：单选。同事在 GitHub 上找到公开的算法代码，仓库没有许可证，想用到工作里。原题同上 PDF 第 10 题。a 视为作者保留专有权利，不能合法使用。b 作者保留版权，署名后就可以用。c 视为没有许可证，可以不署名使用，出 bug 作者不负责。d 按 GitHub 条款，没写许可证的公开仓库视为 GPL v3，公开修改后的源码才能用。

**套路对应**：用 3.5 第 2、3 步。没有许可证就不能用。公开可见和署名都不等于授权。

**推导**：a 的结论成立。没有许可证时版权仍在作者手里，别人不能复制、修改或分发。GitHub 条款只允许其他用户在网站上查看和 fork，不授权放进自己的产品，也不会自动改成 GPL。b 不成立，署名不能代替授权。c 不成立，没有许可证不等于放弃版权。d 不成立，GitHub 不把无许可证仓库当成 GPL。非官方答案：a。

**坑**：a 写的是 exclusive license。法律上的独占许可是作者签给某人的一份许可。这里的事实是作者没有发出许可，权利仍由作者保留。结论「不能合法使用」成立，b、c、d 明确不成立。

### 4.7 2024 S2 Q4 开放题的伦理小问

**题意**：开放题。从九个主题里选两个。A 最多 10 句，讲它们和软件构造的关系。B 最多 5 句，讲两者的关系。C 最多 5 句，提出一个伦理问题或负面的社会、环境影响，并给出理由。这里只写 C。整题结构见 [06 SOLID 和重构](06-solid-refactoring.md)。原题 [Q4README.md](../../exam-practice-questions/past-exams/2024-2/src/Q4/Q4README.md)。

**套路对应**：用 3.6 第 5 步。四句：问题是什么，谁受害，违反哪条原则，怎样补救。

**推导**：两个主题若选性能分析和知识产权，C 可以交下面四句。非官方答案。One ethical issue is copying a faster public implementation that has no licence, so the product ships sooner. The author is harmed because the code is used without permission, and users receive software the team may not legally maintain. This fails the public-interest and judgment principles, because speed is placed above the author's rights. The team should ask for a licence, keep any required notices, or write its own implementation.

**坑**：C 只写伦理。不要把两个主题的定义再写一遍。句数按句号算，最多 5 句。

### 4.8 2024 S1 Q4 MCQ1 软件构造包括什么

**题意**：多选。选出所有不能填进 Software Construction includes 的项。全对才得分。原题 [Q4README.md](../../exam-practice-questions/past-exams/2024-1/src/Q4/Q4README.md) MCQ1。选项有 coding、testing、debugging、modular design、problem definition、readability、planning、construction with reuse、use of standards、IDE、construction for reuse、用 profiling 和性能分析工具做 code tuning。

**套路对应**：用 3.8 第 1 步，查 2.6。编码、调试、单元测试和集成测试、详细设计、复用、标准、IDE、profiling 都属于。问题定义不属于。

**推导**：coding、debugging、modular design、readability、construction with reuse、use of standards、IDE、construction for reuse、profiling 都属于。testing 也属于。SWEBOK 把单元测试和集成测试写进构造的定义。系统测试和验收测试不属于，但「包括测试」这句成立。problem definition 不属于，它是需求阶段的事。planning 不确定。SWEBOK 有 construction planning，构造阶段自己的计划属于软件构造。选项只写了 planning。课件若把 planning 一律当成项目管理，它就不属于。非官方答案：最可能只选 E。不确定：G 是否入选，取决于课件。以课件为准。课件若排除 planning，答案是 E 和 G。

**坑**：多选或漏选都是 0 分。testing 这个词很宽，不要因此把 B 选进「不属于」。

### 4.9 2024 S1 Q4 MCQ4 澳洲知识产权

**题意**：多选。选出在澳洲语境下所有正确的陈述。原题同上 MCQ4。A 版权自动归作者，别人也可以做出同样的作品，作者必须证明自己能独立创作。B 版权保护非经济权利。C copyleft 软件免费，而且任何费用都不能收。D MIT 是 copyleft。E 专利保护 new、useful、inventive、suitable subject matter 的发明，软件有技术或商业创新就可以申请。

**套路对应**：用 3.5，查 2.3。版权自动产生。copyleft 不是不许收费。MIT 是宽松许可证。题干写了 Australian context，细节以课件为准。

**推导**：C 不成立。copyleft 要求衍生作品沿用同一许可证并提供源码，不禁止收费。这类软件仍然有版权。D 不成立。MIT 是 permissive licence，衍生作品可以闭源。A 的前半句成立，澳洲版权写出来就有，不用注册。后半句有问题。版权法里的 reproduce 是复制，这项权利属于版权人。别人可以独立创作出相似作品，这和复制不是一回事。取得版权也不以先证明独立创作为前提。B 不精确。署名权和保护作品完整是精神权利，写在 Copyright Act 1968 里，是作者的人身权利，不等于版权里的经济权利。E 的前半句符合澳洲标准专利的要件。后半句过宽。纯商业方案和纯算法通常不是 suitable subject matter，一般要有技术贡献。以课件为准。非官方答案：最可能是 A、B、E。不确定：A 的 reproduce 和「必须证明」、B 把精神权利说成版权、E 的 business innovation，都和法条有差距。C 和 D 不选。

**坑**：全对才得分。C 里的 whatsoever 是绝对词，可以直接排除。MIT 要求保留版权声明，这不使它变成 copyleft。

### 4.10 2024 S1 Q4 MCQ8 IEEE 职业道德

**题意**：多选。哪些做法符合 IEEE 软件工程道德规范。原题同上 MCQ8。A 发现社交平台有严重安全漏洞，为了不损害平台声誉而不向公众披露，并引用第 2 条 CLIENT AND EMPLOYER。B 在 App 里隐藏收集数据的功能，未经同意，用来改进营销，并说这符合第 1 条 PUBLIC。C 为了速度少做测试，带着已知 bug 发布，认为用户知道就不影响安全，因此符合 JUDGMENT。D 拒绝参与用来侵犯隐私的监控项目。

**套路对应**：用 3.6。先看谁受损。公众的安全和隐私优先。原则编号要和 2.4 的表核对。编号对了，做法相反，仍然不符合。

**推导**：A 不符合。第 2 条写着 consistent with the public interest。雇主声誉压不过用户安全。B 不符合。未经同意收集数据损害隐私，和第 1 条相反。C 不符合。带着已知缺陷赶工发布，违背第 3 条 PRODUCT 和第 4 条 JUDGMENT。用户被告知不等于风险消失。D 符合。拒绝侵犯隐私的项目，把公众利益放在前面。非官方答案：D。

**坑**：A、B、C 里的原则英文名没有引错。错在用这条原则给损害公众的行为辩护。只核对名字会把它们选上。

### 4.11 2024 S1 Q4 MCQ12 Android 综合判断

**题意**：多选，选出所有错误陈述。原题同上 MCQ12。A 不在 `AndroidManifest.xml` 声明互联网权限会导致编译错误。B 显式 Intent 必须给出目标组件的类名。C `onCreate` 在第一次创建时调用，而且它和 `onStart` 是仅有的两个必须实现的生命周期方法。D ConstraintLayout 支持 View 相对彼此定位。E Listener 只适用于按钮和文本框。F Toast 用来短暂显示一条消息。

**套路对应**：用 3.3 第 6 步，查 2.1。方向词是 INCORRECT，选判断为错的项。看出错发生在编译时还是运行时，以及 only、must。

**推导**：A 是错的。漏掉 INTERNET 权限，要到运行时访问网络才失败。B 是对的。显式 Intent 写成 `new Intent(this, 目标.class)`。C 是错的。`onCreate` 确实在第一次创建时调用。`onStart` 不是必须实现的方法。2.1 把 `onCreate` 记为通常要重写的那个回调。D 是对的。E 是错的。任何 View 都能设 Listener。F 是对的。非官方答案：A、C、E。

**坑**：C 的前半句是对的。后半句把 `onStart` 说成必须实现，整句算错。不要因为前半句对就放过它。

### 4.12 2022 S1 Q4 许可证小问

**题意**：开放题第 3 问。计算器要做加减乘除和乘方。问从许可证和知识产权看还有没有改进余地。前两问是缺陷和可扩展性，不在这个文件。原题 [readme.md](../../exam-practice-questions/past-exams/2022-1/q4/readme.md)。同目录的 `Client.java`、`Add.java` 等没有版权声明，也没有许可证。

**套路对应**：用 3.5 第 2 步。没有许可证就是作者保留权利，别人不能拿去用。

**推导**：可以写三条。非官方答案，没有标准措辞。仓库没有 LICENSE 和版权头，按默认规则别人不能复制或修改。某个 `calculate` 若是从别处抄来的，要核对那份许可证。GPL 的代码不能放进准备闭源的程序。MIT 的代码要保留原来的版权声明。准备分发时，选定一个许可证写进仓库。

**坑**：不要只写「加个 MIT」。先写现在缺许可证的后果，再写建议。题目要的是短要点。

### 4.13 2021 S2 Q5 Activity 生命周期开放题

**题意**：列出 Android Activity 生命周期的全部方法，并说明系统何时调用。字数 100 到 400。原题 [readme.md](../../exam-practice-questions/past-exams/2021-2/q5/readme.md)。

**套路对应**：用 3.3，按 2.1 的回调表和场景表写。七个回调都要出现，每个写一句何时调用，再写启动和切换的顺序。

**推导**：下面这段可以直接交。非官方答案。An Android Activity has seven lifecycle methods. The system calls them. The app does not call them on itself. onCreate runs once, when the Activity is first created. Set up the screen here, usually with setContentView. onStart runs when the Activity is about to become visible. onResume runs when it is in the foreground and can take input. onPause runs when the Activity loses focus, for example when another Activity is opening. onStop runs when it is no longer visible, such as when another Activity covers it or the user presses Home. onRestart runs when a stopped Activity is about to return, and onStart always follows it. onDestroy runs before the Activity is destroyed, for example after Back, finish, or a screen rotation. If the system kills the process, onDestroy may not run. Launching the app runs onCreate, then onStart, then onResume. If A starts B and B fills the screen, A runs onPause, B runs onCreate, onStart and onResume, and then A runs onStop. A stays on the back stack and is not destroyed. Pressing Back on B runs B's onPause, then A's onRestart, onStart and onResume, then B's onStop and onDestroy.

**坑**：只列方法名不够 100 字。七个名字是 `onCreate`、`onStart`、`onResume`、`onPause`、`onStop`、`onRestart`、`onDestroy`。不要漏 `onRestart`。

### 4.14 2019 S1 Q2 Git 提交和 Makefile

**题意**：Q2 是指针在屏幕上移动的程序。第 1 小问 2 分：在 Q2 目录里用 git 做版本管理，至少 3 次 commit。第 2 小问 3 分：写 Makefile，`make` 编译，`make clean` 删除 class 文件，`make rundemo` 运行 `Demo.java`。原题 [2019 Semester 1 - Final Examination.pdf](../../exam-practice-questions/past-exams/2019-1/2019%20Semester%201%20-%20Final%20Examination.pdf)。

**套路对应**：commit 的含义见 2.5。Makefile 用 2.5 的模板。第一个目标给单独的 `make`，`clean` 删除 class，`rundemo` 先编译再 `java Demo`。

**推导**：在 Q2 目录 `git init`，至少三次有内容的 commit。Makefile 放在 Q2，三条目标照模板写。非官方答案，按这两条要求做即可。

**坑**：命令行必须以 Tab 开头，空格会报 missing separator。题目后半句限制第 2 到第 5 问只能改指定方法。Makefile 是第 2 问要求的新文件，git 仓库是第 1 问要求的。

## 5 易错清单

| 场景 | 容易错在 | 怎么判 |
|---|---|---|
| 2025 S2 启动新 Activity | 记得 `onStop` 就选立刻销毁，或只调用 `onStop` | 不销毁，留在返回栈。选项里最接近的是暂停并留在栈里。见 4.1 |
| 权限、回调、Listener | 漏权限当成编译错误。`onStart` 当成必须实现。Listener 只给按钮 | 运行时才失败。通常只重写 `onCreate`。任何 View 都能监听。见 4.11 |
| 多语言 | 在 Java 里按语言改字，或复制整套布局 | `values-语言/strings.xml`。见 4.4 |
| JML 能做什么 | 因为 `javac` 忽略注释，就认为不能做运行时检查 | 做不了的是 XML 这类业务功能。检查靠工具。见 4.2 |
| JML 写法 | 把 `@param` 写成 `requires`，或给描述加引号 | 参数的文字说明是 `@param i 描述`。见 4.5 |
| 没有许可证 | 公开、署名，或以为 GitHub 默认 GPL，就拿去用 | 不能用。fork 不等于授权。见 4.6 |
| copyleft 和 MIT | MIT 当成 copyleft。copyleft 当成不许收费 | MIT 可闭源。GPL 可收费，衍生作品要公开源码。见 4.9 |
| 澳洲版权和专利 | 把 A、B、E 的措辞当成法条原文 | C、D 不选。A、B、E 以课件为准。见 4.9 |
| 伦理情境 | 选项里的原则名字是对的就选 | 先看谁受损。损害公众的做法不符合规范。见 4.10 |
| Git 图 | 把最底下的 D 当成最新。把 B 也当成 merge | 上面新。只有两个父提交的 A 是 merge。`origin/` 指着的是已经在远程的提交。见 4.3 |
| 软件构造的范围 | 把 planning 和 problem definition 一起选，或一起不选 | problem definition 不属于。planning 看课件说的是不是 construction planning。见 4.8 |
| 计分 | 2024 S1 漏一个选项也想得分。2025 S1 拿不准就空着 | 2024 S1 全对才有分。2025 S1 单选不倒扣，排除后也要填一个 |
| 答案文件 | 写成 `1. AB` 或 `1. A B` | 多选用逗号：`1. A,B`。见 3.2 |
| Makefile | 命令前用空格 | 必须是 Tab。见 4.14 |
| 生命周期开放题 | 只写七个方法名 | 2021 S2 要求 100 到 400 字，每个方法要写何时调用。见 4.13 |

# 项目名称

**前后端处理 Excel 文件的系统**

---



## 技术栈

### 前端

- **Vue.js 3**: 前端框架，用于构建用户界面。
- **TypeScript**: 为 JavaScript 添加静态类型。
- **Axios**: 用于处理 HTTP 请求。
- **Vite**: 前端构建工具。
- **Bootstrap 5**: 前端样式框架。

### 后端

- **Java**: 后端编程语言。
- **Spring Boot**: 后端框架，用于快速构建独立的、生产级的基于 Spring 的应用程序。
- **MyBatis**: 数据持久层框架，用于与数据库交互。
- **Apache POI**: 处理 Excel 文件的库。
- **MySQL**: 数据库，用于存储班级、课程和教师信息。

---

## 功能特性

- **Excel 文件上传**: 用户可以通过前端界面上传 Excel 文件。
- **Excel 文件处理**: 后端接收上传的文件，处理其中的数据（如匹配课程、班级和教师信息）。
- **Excel 文件下载**: 处理完成后，用户可以下载生成的新 Excel 文件。
- **跨域请求支持**: 后端配置支持跨域请求，确保前后端顺利通信。
- **数据映射与匹配**: 高效的数据处理逻辑，确保数据的准确性和一致性。

---

## 项目结构

### 前端

```
frontend/
├── src/
│   ├── api.ts
│   ├── App.vue
│   ├── assets/
│   │   └── css/
│   │       └── common.css
│   ├── components/
│   │   └── [Components]
│   ├── main.ts
│   ├── router/
│   │   └── [Routes]
│   └── store/
│       └── [Store]
├── public/
│   └── index.html
├── package.json
├── tsconfig.json
└── vite.config.ts
```

### 后端

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── handle_excel/
│   │   │           ├── bean/
│   │   │           │   ├── Class.java
│   │   │           │   ├── Course.java
│   │   │           │   └── Teacher.java
│   │   │           ├── config/
│   │   │           │   └── WebConfig.java
│   │   │           ├── controller/
│   │   │           │   └── HandleController.java
│   │   │           ├── mapper/
│   │   │           │   ├── ClassMapper.java
│   │   │           │   ├── CourseMapper.java
│   │   │           │   └── TeacherMapper.java
│   │   │           ├── service/
│   │   │           │   ├── DataService.java
│   │   │           │   ├── MatchService.java
│   │   │           │   ├── ProcessService.java
│   │   │           │   └── SheetService.java
│   │   │           └── utils/
│   │   │               ├── ExcelUtils.java
│   │   │               └── SymbolUtils.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   └── test/
│       └── [Tests]
├── pom.xml
└── README.md
```

---

## 安装与运行

### 前端

#### 前提条件

- **Node.js**: 确保已安装 Node.js。
- **npm 或 yarn**: 包管理工具。

#### 安装步骤

1. **克隆仓库**

	```bash
	git clone http://120.78.52.202:3000/weiyong/202412G07.git
	cd handle_excel/frontend
	```

2. **安装依赖**

	```bash
	npm install
	# 或者使用 yarn
	yarn install
	```

3. **运行开发服务器**

	```bash
	npm run dev
	# 或者使用 yarn
	yarn dev
	```

### 后端

#### 前提条件

- **Java 17 或更高版本**: 确保已安装 Java。
- **Maven**: 项目管理和构建工具。
- **MySQL**: 数据库管理系统。

#### 安装步骤

1. **配置数据库**

	- 创建一个名为 javaweb的 MySQL 数据库。
	- 导入项目提供的 SQL 文件以创建所需的表和初始数据。

2. **配置应用**

	在 `src/main/resources/application.properties` 文件中配置数据库连接：

	```properties
	spring.datasource.url=jdbc:mysql://localhost:3306/excel_handle_db?useSSL=false&serverTimezone=UTC
	spring.datasource.username=your_username
	spring.datasource.password=your_password
	spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
	
	# 其他配置
	server.port=8080
	```

3. **构建和运行应用**

	```bash
	mvn clean install
	mvn spring-boot:run
	```

	后端服务将运行在 `http://localhost:8080`。

---

## 使用指南

1. **上传 Excel 文件**

	- 打开前端应用，进入“处理Excel”页面。
	- 点击“上传文件”按钮，选择需要处理的 Excel 文件。

2. **处理文件**

	- 上传文件后，点击“添加”按钮，系统将文件发送到后端进行处理。
	- 处理过程中会弹出提示，显示“成功！”或“失败！”。

3. **下载处理后的文件**

	- 处理成功后，浏览器将自动触发下载新的 Excel 文件。
	- 文件名将根据后端生成的 `Content-Disposition` 头部信息自动命名。

---

## API 端点

### `POST /handle`

**描述**: 接收上传的 Excel 文件，处理后返回新的 Excel 文件。

**请求参数**:

- `file`: 上传的 Excel 文件（`multipart/form-data` 格式）。

**响应**:

- 成功时返回处理后的 Excel 文件，`Content-Type` 为 `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`。
- 失败时返回错误信息。

**示例**:

```bash
curl -X POST http://localhost:8080/handle -F "file=@path_to_your_file.xlsx" -o processed_file.xlsx
```

## 详细说明

### 前端代码概述

前端项目基于 Vue 3 和 TypeScript，使用 Axios 处理与后端的 HTTP 通信。主要功能包括文件上传和下载处理。以下是关键代码部分的简要说明：

- **`api.ts`**: 配置 Axios 实例，设置基础 URL。
- **组件逻辑**: 使用 Vue 的 `ref` 管理文件状态，通过 `fileChange` 处理文件选择，`handle` 函数上传文件并处理响应，触发文件下载。

```typescript
import axios from "axios"

const request = axios.create({
    baseURL:'http://localhost:8080'
});

export default request
```

```vue
<script setup lang="ts">
import { ref } from "vue";
import request from "./api.ts";

let file = ref<unknown>();

function fileChange(event: Event): void {
  const target = event.target as HTMLInputElement;
  if (target && target.files && target.files.length > 0) {
    file.value = target.files[0];
  }
}

async function handle() {
  const data = new FormData();

  if (file.value) data.append("file", file.value as File);

  try {
    const response = await request.post('/handle', data, {
      responseType: 'blob',
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });

    console.log(response);

    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;

    const contentDisposition = response.headers['content-disposition'];
    let filename = '课表.xlsx';
    if (contentDisposition) {
      const matches = contentDisposition.match(/filename=([^;]+)/);
      if (matches != null && matches[1]) {
        filename = decodeURIComponent(matches[1].trim().replace(/"/g, ""));
      }
    }

    window.alert("成功!");

    link.setAttribute('download', filename);
    document.body.appendChild(link);
    link.click();
    link.remove();
    window.URL.revokeObjectURL(url);
  } catch (e) {
    window.alert("失败!");
    console.log("失败:" + e);
  }
}
</script>
```

### 后端项目详解

后端项目基于 Spring Boot，采用分层架构，主要包括 Bean 层、配置层、控制器层、数据访问层、服务层和工具类层。以下是各层的详细说明：

#### Bean 层

定义项目中的数据模型，包括 `Class`、`Course` 和 `Teacher` 类。

- **Class类**: 包含 `classNum`（编号）、`className`（班级名）、`grade`（年级）属性。
- **Course类**: 包含 `courNum`（编号）、`courName`（课程名）、`ttime`（课程开始时间）、`utime`（课程结束时间）、`remark`（课程备注）属性。
- **Teacher类**: 包含 `trNum`（编号）、`trFirstName`（老师的姓）、`trName`（老师的名）属性。

#### 配置层 (Config)

通过定义一个配置类并实现 `WebMvcConfigurer` 接口，解决跨域请求问题。

#### 控制器层 (Controller)

负责处理客户端的 HTTP 请求，调用服务层的方法处理业务逻辑，并返回响应结果。主要提供 `/handle` 接口用于文件上传和下载。

#### 数据访问层 (Mapper)

使用 MyBatis 框架，通过接口定义 SQL 查询语句，与数据库交互。包括 `ClassMapper`、`CourseMapper` 和 `TeacherMapper` 接口。

#### 服务层 (Service)

业务逻辑的核心部分，负责实现具体的业务需求，包括数据获取、过滤、转换、匹配等。主要类包括：

- **DataService**: 从数据库获取数据并进行映射。
- **MatchService**: 负责课程和班级的匹配。
- **ProcessService**: 处理 Excel 文件中的数据匹配和填充。
- **SheetService**: 处理整个 Workbook 的 Sheets，复制模板并调用 `ProcessService`。

#### 工具类层 (Utils)

提供辅助方法，处理与 Excel 文件相关的常见操作，如解析表头、复制 Sheet、处理单元格数据等。主要工具类包括：

- **ExcelUtils**: 解析表头、复制 Sheet、填充新行数据。
- **SymbolUtils**: 处理字符串中的符号、统一获取单元格数据。

---

## 版本历史

- **1.0.0** - 初始发布

---

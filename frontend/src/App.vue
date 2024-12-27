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
      responseType: 'blob', // 指定响应类型为 blob
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });

    console.log(response);

    // 创建一个临时的 URL 来触发下载
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;

    // 解析 Content-Disposition 头部信息来获取文件名
    const contentDisposition = response.headers['content-disposition'];
    let filename = '课表.xlsx';
    if (contentDisposition) {
      const matches = contentDisposition.match(/filename=([^;]+)/);
      if (matches != null && matches[1]) {
        filename = decodeURIComponent(matches[1].trim().replace(/"/g, ""));
      }
    }

    window.alert("成功!");

    link.setAttribute('download', filename); // 设置下载文件名
    document.body.appendChild(link);
    link.click();
    link.remove();
    window.URL.revokeObjectURL(url); // 释放对象URL
  } catch (e) {
    window.alert("失败!");
    console.log("失败:" + e);
  }
}
</script>

<template>
  <div class="container" style="height: 100vh;">
    <h3 class="title fw-semibold">处理Excel</h3>
    <form class="form-control d-flex flex-column justify-content-center" style="width: 30vw; margin: 0 auto; transition: all 0.5s">
      <span class="form-control fw-semibold mt-3" style="border: none">上传文件</span>
      <div class="input-group mt-1">
        <input type="file" class="form-control" id="inputGroupFile02" @change="fileChange">
      </div>

      <div class="comBtnOuter">
        <button type="button" class="comBtn mt-3 mb-3 fw-semibold" @click="handle()">添加</button>
      </div>
    </form>
  </div>
</template>

<style scoped>
/* 设置按钮的居中和宽度 */
.comBtnOuter{
  text-align: center;
}

.comBtn{
  width: 15vw;
}
</style>

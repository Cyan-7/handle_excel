package org.example.handle_excel.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.handle_excel.bean.Class;
import org.example.handle_excel.bean.Course;
import org.example.handle_excel.bean.Teacher;
import org.example.handle_excel.service.DataService;
import org.example.handle_excel.service.SheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
@SuppressWarnings("all")

/**
 * 控制器类，处理与 Excel 文件相关的请求。
 * 提供一个端点用于上传 Excel 文件，并生成处理后的 Excel 文件供下载。
 */
@Slf4j
@RestController
public class HandleController {

    /**
     * 模板文件在资源目录中的路径。
     * 该模板用于复制 Sheet 的结构和样式。
     */
    private static final String TEMPLATE_PATH = "templates/template.xlsx"; // 模板文件路径

    @Autowired
    private DataService dataService; // 数据服务，用于获取课程、班级、教师数据

    @Autowired
    private SheetService sheetService; // Sheet 处理服务，用于处理 Excel 的 Sheet

    /**
     * 处理上传的 Excel 文件，支持多张 Sheet，并生成对应的输出文件。
     *
     * @param file 上传的 Excel 文件（MultipartFile 类型）
     * @return 生成的 Excel 文件下载链接或错误信息
     */
    @PostMapping("/handle")
    public ResponseEntity<?> handleExcel(@RequestParam("file") MultipartFile file) {

        // 检查文件是否为空
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("上传的文件为空");
        }

        try (
                // 加载模板 Workbook
                InputStream templateInputStream = new ClassPathResource(TEMPLATE_PATH).getInputStream();
                Workbook templateWorkbook = new XSSFWorkbook(templateInputStream);
                // 加载用户上传的 Workbook
                Workbook inputWorkbook = new XSSFWorkbook(file.getInputStream());
                // 创建输出流，用于存储生成的 Excel 文件
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {

            // 创建一个新的 Workbook 作为输出
            Workbook outputWorkbook = new XSSFWorkbook();

            // 初始化数据映射，获取课程、班级、教师的数据映射
            Map<String, List<Course>> courseMap = dataService.getCourseMap();
            Map<String, Class> classMap = dataService.getClassMap();
            Map<String, List<Teacher>> teacherMap = dataService.getTeacherMap();

            // 处理输入 Workbook 中的每个 Sheet，并生成对应的输出 Sheet
            sheetService.ProcessSheet(inputWorkbook, outputWorkbook, templateWorkbook, courseMap, classMap, teacherMap);

            // 将输出 Workbook 写入输出流
            outputWorkbook.write(outputStream);
            byte[] fileBytes = outputStream.toByteArray();

            // 准备响应，包含生成的 Excel 文件
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(fileBytes));
            String encodedFileName = URLEncoder.encode("课表.xlsx", StandardCharsets.UTF_8);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"") // 设置下载文件名
                    .contentType(MediaType.APPLICATION_OCTET_STREAM) // 设置内容类型为二进制流
                    .contentLength(fileBytes.length) // 设置内容长度
                    .body(resource); // 返回文件资源

        } catch (IOException e) {
            // 记录文件处理失败的错误日志
            log.error("文件处理失败", e);
            return ResponseEntity.status(500).body("文件处理失败");
        } catch (IllegalArgumentException e) {
            // 记录输入参数错误的日志
            log.error("输入参数错误", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

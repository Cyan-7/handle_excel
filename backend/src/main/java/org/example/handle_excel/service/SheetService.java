package org.example.handle_excel.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.example.handle_excel.bean.Class;
import org.example.handle_excel.bean.Course;
import org.example.handle_excel.bean.Teacher;
import org.example.handle_excel.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@SuppressWarnings("all")

/**
 * Sheet 服务类，用于处理整个 Workbook 中的 Sheets。
 * 负责遍历输入 Workbook 的每个 Sheet，复制模板 Sheet 的结构和样式，并调用 ProcessService 处理数据。
 */
@Slf4j
@Service
public class SheetService {

    @Autowired
    private ProcessService processService; // 处理服务，用于处理每个 Sheet 的行和列

    /**
     * 处理输入的 Workbook 中的所有 Sheets，并将结果写入输出 Workbook。
     * 处理步骤：
     * 1. 遍历输入 Workbook 中的每个 Sheet。
     * 2. 查找模板 Workbook 中对应的 Sheet，如果不存在则跳过。
     * 3. 在输出 Workbook 中创建一个新的 Sheet，并复制模板 Sheet 的内容和样式。
     * 4. 解析输入 Sheet 的表头，获取列索引到年级的映射。
     * 5. 调用 ProcessService 处理当前 Sheet 的数据，生成新的行到输出 Sheet。
     *
     * @param inputWorkbook    输入的 Workbook，包含用户上传的数据
     * @param outputWorkbook   输出的 Workbook，将生成的数据写入
     * @param templateWorkbook 模板的 Workbook，提供 Sheet 的结构和样式
     * @param courseMap        课程映射，键为基础课程名称，值为课程列表
     * @param classMap         班级映射，键为班级名称，值为班级对象
     * @param teacherMap       教师映射，键为教师姓名，值为教师对象列表
     */
    public void ProcessSheet(Workbook inputWorkbook,
                             Workbook outputWorkbook,
                             Workbook templateWorkbook,
                             Map<String, List<Course>> courseMap,
                             Map<String, Class> classMap,
                             Map<String, List<Teacher>> teacherMap) {
        log.info("开始处理工作簿");

        // 获取输入 Workbook 中的 Sheet 数量
        int numberOfSheets = inputWorkbook.getNumberOfSheets();
        log.info("输入Workbook包含 {} 个Sheet", numberOfSheets);

        // 遍历每一个 Sheet
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet inputSheet = inputWorkbook.getSheetAt(i); // 获取当前输入 Sheet
            String sheetName = inputSheet.getSheetName(); // 获取 Sheet 名称
            log.info("处理Sheet: {}", sheetName);

            // 在模板 Workbook 中找到对应的 Sheet
            Sheet templateSheet = templateWorkbook.getSheet(sheetName);
            if (templateSheet == null) {
                // 如果模板中不存在对应的 Sheet，记录警告日志并跳过处理
                log.warn("模板中未找到Sheet: '{}', 跳过处理", sheetName);
                continue;
            }

            // 在输出 Workbook 中创建一个新的 Sheet，并复制模板 Sheet 的内容和样式
            Sheet outputSheet = outputWorkbook.createSheet(sheetName);
            log.debug("创建输出Sheet: {}", sheetName);
            ExcelUtils.copySheet(templateSheet, outputSheet);

            // 解析输入 Sheet 的表头，获取列索引到年级的映射
            Row headerRow = inputSheet.getRow(0); // 表头通常在第一行（索引0）
            if (headerRow == null) {
                // 如果表头不存在，记录警告日志并跳过处理
                log.warn("Sheet '{}' 没有表头，跳过处理", sheetName);
                continue;
            }
            Map<Integer, String> gradeMap = ExcelUtils.parseHeader(headerRow); // 解析表头
            log.debug("解析表头完成，gradeMap: {}", gradeMap);

            // 调用 ProcessService 处理当前 Sheet 的行和列数据
            processService.ProcessRow(courseMap, classMap, teacherMap,
                    inputSheet, sheetName, outputSheet, headerRow, gradeMap);

            log.info("工作簿处理完成");
        }
    }
}

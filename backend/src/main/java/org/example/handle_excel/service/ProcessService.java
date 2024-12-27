package org.example.handle_excel.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.handle_excel.bean.Class;
import org.example.handle_excel.bean.Course;
import org.example.handle_excel.bean.Teacher;
import org.example.handle_excel.utils.ExcelUtils;
import org.example.handle_excel.utils.SymbolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@SuppressWarnings("all")

/**
 * 处理服务类，用于处理 Excel 中的行和列数据。
 * 负责解析每一行的数据，匹配课程、班级和教师，并生成新的行到输出 Sheet。
 */
@Slf4j
@Service
public class ProcessService {

    @Autowired
    private MatchService matchService; // 匹配服务，用于匹配课程和班级

    /**
     * 处理一个 Sheet 中的所有行数据。
     *
     * @param courseMap   课程映射，键为基础课程名称，值为课程列表
     * @param classMap    班级映射，键为班级名称，值为班级对象
     * @param teacherMap  教师映射，键为教师姓名，值为教师对象列表
     * @param inputSheet  输入的 Sheet，需要处理的数据
     * @param sheetName   Sheet 的名称，用于日志记录
     * @param outputSheet 输出的 Sheet，将生成的数据写入
     * @param headerRow   表头行，用于解析班级和年级信息
     * @param gradeMap    列索引到年级的映射，键为列索引，值为年级
     */
    public void ProcessRow(Map<String, List<Course>> courseMap,
                           Map<String, Class> classMap,
                           Map<String, List<Teacher>> teacherMap,
                           Sheet inputSheet,
                           String sheetName,
                           Sheet outputSheet,
                           Row headerRow,
                           Map<Integer, String> gradeMap) {
        // 获取表格的最后一行索引（0-based）
        int lastRowNum = inputSheet.getLastRowNum();

        log.info("Sheet '{}' 有 {} 行数据", sheetName, lastRowNum);

        // 遍历从第二行（索引1）开始的每一行
        for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex++) {
            Row inputRow = inputSheet.getRow(rowIndex); // 获取当前行

            // 跳过空行
            if (inputRow == null) {
                log.debug("跳过空行: {}", rowIndex);
                continue;
            }

            // 第一列是课程名称
            String originalCourseName = SymbolUtils.getCellValueAsString(inputRow.getCell(0));

            // 如果课程名称为空，跳过该行
            if (originalCourseName == null || originalCourseName.isEmpty()) {
                log.debug("跳过无课程名称的行: {}", rowIndex + 1);
                continue;
            }

            // 移除课程名称中的符号，获取处理后的课程名称
            String processedCourseName = SymbolUtils.removeSymbol(originalCourseName);
            List<Course> courses = courseMap.get(processedCourseName);

            // 如果没有找到对应的课程，记录警告并跳过
            if (courses == null || courses.isEmpty()) {
                log.warn("未找到课程: '{}' 在Sheet: '{}'", originalCourseName, sheetName);
                continue;
            }

            // 处理当前行的每一列，匹配教师并生成新行
            ProcessColumn(inputRow, sheetName, rowIndex, classMap, teacherMap,
                    gradeMap, courses, originalCourseName, outputSheet, headerRow);

            log.info("Sheet '{}' 处理完成", inputSheet.getSheetName());
        }
    }

    /**
     * 处理一行中的每一列数据，匹配教师并生成新的行到输出 Sheet。
     *
     * @param inputRow           输入的行
     * @param sheetName          Sheet 的名称，用于日志记录
     * @param rowIndex           行索引（0-based）
     * @param classMap           班级映射，键为班级名称，值为班级对象
     * @param teacherMap         教师映射，键为教师姓名，值为教师对象列表
     * @param gradeMap           列索引到年级的映射，键为列索引，值为年级
     * @param courses            课程列表，可能包含多个同名课程
     * @param originalCourseName 原始课程名称
     * @param outputSheet        输出的 Sheet，将生成的数据写入
     * @param headerRow          表头行，用于匹配班级信息
     */
    public void ProcessColumn(Row inputRow,
                              String sheetName,
                              int rowIndex,
                              Map<String, Class> classMap,
                              Map<String, List<Teacher>> teacherMap,
                              Map<Integer, String> gradeMap,
                              List<Course> courses,
                              String originalCourseName,
                              Sheet outputSheet,
                              Row headerRow) {

        // 获取当前行的最后一个单元格索引（0-based）
        int lastCellNum = inputRow.getLastCellNum();

        // 遍历从第二列（索引1）开始的每一列
        for (int colIndex = 1; colIndex < lastCellNum; colIndex++) {
            // 获取教师名称，调用 SymbolUtils 处理单元格值
            String teacherName = SymbolUtils.getCellValueAsString(inputRow.getCell(colIndex));

            // 如果教师名称为空，跳过该单元格
            if (teacherName == null || teacherName.isEmpty()) {
                log.debug("跳过空教师名称的单元格, Sheet: '{}', 行: {}, 列: {}", sheetName, rowIndex + 1, colIndex + 1);
                continue;
            }

            // 获取教师列表
            List<Teacher> teachers = teacherMap.get(teacherName);
            if (teachers == null || teachers.isEmpty()) {
                // 如果教师不存在，记录警告日志并跳过
                log.warn("未找到教师: '{}' 在Sheet: '{}', 列索引: {}", teacherName, sheetName, colIndex + 1);
                continue;
            }

            // 获取对应列的年级信息
            String grade = gradeMap.get(colIndex);
            if (grade == null || grade.isEmpty()) {
                // 如果年级信息不存在，记录警告日志并跳过
                log.warn("未找到Sheet: '{}', 列索引: {} 对应的年级信息", sheetName, colIndex + 1);
                continue;
            }

            // 匹配课程，调用 MatchService 的 MatchCourse 方法
            Optional<Course> matchedCourseOpt = matchService.MatchCourse(courses, grade, originalCourseName, sheetName);
            if (matchedCourseOpt.isEmpty()) {
                // 如果未匹配到课程，跳过该单元格
                continue;
            }
            Course matchedCourse = matchedCourseOpt.get(); // 获取匹配到的课程

            // 匹配班级，调用 MatchService 的 MatchClass 方法
            Class aClass = matchService.MatchClass(headerRow, sheetName, classMap, colIndex);
            if (aClass == null) {
                // 如果未匹配到班级，跳过该单元格
                continue;
            }

            // 处理教师列表，如果存在多个同名教师
            for (Teacher teacher : teachers) {
                // 在输出 Sheet 中创建新行并填充数据
                Row newRow = outputSheet.createRow(outputSheet.getLastRowNum() + 1);
                ExcelUtils.populateNewRow(newRow, teacher, matchedCourse, originalCourseName, aClass);
                log.debug("填充新行: Sheet: '{}', 行索引: {}", sheetName, newRow.getRowNum() + 1);
            }
        }
    }
}

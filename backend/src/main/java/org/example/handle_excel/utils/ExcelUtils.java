package org.example.handle_excel.utils;

import org.apache.poi.ss.usermodel.*;
import org.example.handle_excel.bean.Class;
import org.example.handle_excel.bean.Course;
import org.example.handle_excel.bean.Teacher;

import java.util.HashMap;
import java.util.Map;
@SuppressWarnings("all")

/**
 * Excel 工具类，提供辅助方法来处理 Excel 单元格数据和 Sheet 操作。
 * 包括解析表头、复制 Sheet、填充新行数据等功能。
 */
public class ExcelUtils {
    /**
     * 解析表头，提取班级对应的年级信息。
     * 处理步骤：
     * 1. 遍历表头行的每一列（从第二列开始）。
     * 2. 提取班级名称中的年级信息，假定年级为班级名称的第一个字符。
     * 3. 构建列索引到年级的映射。
     *
     * @param headerRow 表头行
     * @return 列索引到年级的映射，键为列索引，值为年级
     */
    public static Map<Integer, String> parseHeader(Row headerRow) {
        Map<Integer, String> gradeMap = new HashMap<>();

        // 获取表头的最后一个单元格索引（0-based）
        int lastCellNum = headerRow.getLastCellNum();

        // 从第二列（索引1）开始遍历每一列
        for (int colIndex = 1; colIndex < lastCellNum; colIndex++) {
            String classNameWithCode = SymbolUtils.getCellValueAsString(headerRow.getCell(colIndex));
            if (classNameWithCode != null && !classNameWithCode.isEmpty()) {
                // 提取班级名称中的年级信息，假定年级为第一个字符
                String grade = classNameWithCode.substring(0, 1);
                gradeMap.put(colIndex, grade);
            }
        }

        return gradeMap;
    }

    /**
     * 复制模板 Sheet 到输出 Sheet，包括内容和样式。
     * 处理步骤：
     * 1. 遍历模板 Sheet 中的每一行。
     * 2. 对于每一行，遍历每个单元格。
     * 3. 复制单元格的值和样式到输出 Sheet 对应的单元格。
     *
     * @param templateSheet 模板 Sheet
     * @param outputSheet   输出 Sheet
     */
    public static void copySheet(Sheet templateSheet, Sheet outputSheet) {
        // 遍历模板 Sheet 中的每一行
        for (int i = 0; i <= templateSheet.getLastRowNum(); i++) {
            Row templateRow = templateSheet.getRow(i); // 获取模板行
            if (templateRow == null) continue; // 跳过空行
            Row outputRow = outputSheet.createRow(i); // 在输出 Sheet 中创建对应的行

            // 遍历模板行中的每一个单元格
            for (int j = 0; j < templateRow.getLastCellNum(); j++) {
                Cell templateCell = templateRow.getCell(j); // 获取模板单元格
                if (templateCell == null) continue; // 跳过空单元格
                Cell outputCell = outputRow.createCell(j); // 在输出行中创建对应的单元格
                copyCell(templateCell, outputCell); // 复制单元格内容和样式
            }
        }
    }

    /**
     * 复制单元格内容和样式。
     * 处理步骤：
     * 1. 根据模板单元格的类型，复制其值到输出单元格。
     * 2. 复制模板单元格的样式到输出单元格。
     *
     * @param templateCell 模板单元格
     * @param outputCell   输出单元格
     */
    private static void copyCell(Cell templateCell, Cell outputCell) {
        // 复制单元格值
        switch (templateCell.getCellType()) {
            case STRING:
                outputCell.setCellValue(templateCell.getStringCellValue());
                break;
            case NUMERIC:
                outputCell.setCellValue(templateCell.getNumericCellValue());
                break;
            case BOOLEAN:
                outputCell.setCellValue(templateCell.getBooleanCellValue());
                break;
            case FORMULA:
                outputCell.setCellFormula(templateCell.getCellFormula());
                break;
            case BLANK:
                outputCell.setBlank();
                break;
            default:
                outputCell.setBlank();
                break;
        }

        // 复制单元格样式
        CellStyle newStyle = outputCell.getSheet().getWorkbook().createCellStyle();
        newStyle.cloneStyleFrom(templateCell.getCellStyle());
        outputCell.setCellStyle(newStyle);
    }

    /**
     * 填充新行的数据。
     * 处理步骤：
     * 1. 根据教师、课程和班级对象，填充新行的各个单元格。
     * 2. 设置固定值，如人数上限和下限。
     * 3. 处理可能为空的日期字段。
     *
     * @param newRow             新行
     * @param teacher            教师对象
     * @param course             课程对象
     * @param originalCourseName 原始课程名称
     * @param classEntity        班级对象
     */
    public static void populateNewRow(Row newRow, Teacher teacher, Course course, String originalCourseName, Class classEntity) {
        // 填充教师编号
        newRow.createCell(0).setCellValue(teacher.getTrNum()); // 教师编号

        // 填充教师姓名（姓 + 名）
        newRow.createCell(1).setCellValue(teacher.getTrFirstName() + teacher.getTrName()); // 教师姓名

        // 填充课程编号
        newRow.createCell(2).setCellValue(course.getCourNum()); // 课程编号

        // 填充课程名称
        newRow.createCell(3).setCellValue(originalCourseName); // 课程名称

        // 填充班级编号
        newRow.createCell(4).setCellValue(classEntity.getClassNum()); // 班级编号

        // 填充班级名称
        newRow.createCell(5).setCellValue(classEntity.getClassName()); // 班级名称

        // 填充人数上限（固定值）
        newRow.createCell(6).setCellValue(60); // 人数上限

        // 填充人数下限（固定值）
        newRow.createCell(7).setCellValue(30); // 人数下限

        // 填充开始日期，处理可能为空的情况
        newRow.createCell(8).setCellValue(course.getTtime() != null ? course.getTtime() : ""); // 开始日期

        // 填充结束日期，处理可能为空的情况
        newRow.createCell(9).setCellValue(course.getUtime() != null ? course.getUtime() : ""); // 结束日期

        // 填充备注（空值）
        newRow.createCell(10).setCellValue(""); // 备注
    }
}

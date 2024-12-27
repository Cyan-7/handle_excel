package org.example.handle_excel.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.example.handle_excel.bean.Class;
import org.example.handle_excel.bean.Course;
import org.example.handle_excel.utils.SymbolUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 匹配服务类，用于匹配课程和班级。
 * 提供方法根据特定条件匹配课程和班级，帮助处理 Excel 数据时进行数据关联。
 */
@Slf4j
@Service
public class MatchService {

    /**
     * 根据年级和原始课程名称匹配课程。
     * 匹配逻辑：
     * - 遍历课程列表，查找备注中包含特定年级（例如 "三上"）的课程。
     * - 假设所有课程都是上册，因此匹配条件为 grade + "上"。
     *
     * @param courses            课程列表
     * @param grade              年级
     * @param originalCourseName 原始课程名称
     * @param sheetName          当前处理的 Sheet 名称，用于日志记录
     * @return 匹配到的课程（可选），如果未找到则为空
     */
    public Optional<Course> MatchCourse(List<Course> courses, String grade, String originalCourseName, String sheetName) {
        // 使用 Stream API 查找符合条件的课程
        Optional<Course> optionalCourse = courses.stream()
                .filter(course -> course.getRemark() != null && // 确保备注不为空
                        course.getRemark().stream()
                                // 假定所有课程都是上册，匹配备注中第二个元素为 grade + "上"
                                .anyMatch(remark -> remark.size() > 1 && (grade + "上").equals(remark.get(1))))
                .findFirst(); // 找到第一个符合条件的课程

        if (optionalCourse.isEmpty()) {
            // 如果未找到匹配的课程，记录警告日志
            log.warn("未找到匹配的课程: '{}' 对应 grade: '{}' 在Sheet: '{}'", originalCourseName, grade, sheetName);
            return Optional.empty();
        }

        // 返回匹配到的课程
        return optionalCourse;
    }

    /**
     * 根据表头行和列索引匹配班级。
     * 匹配逻辑：
     * - 从表头行的指定列索引获取班级名称。
     * - 使用班级映射查找对应的班级对象。
     *
     * @param headerRow 表头行
     * @param sheetName 当前处理的 Sheet 名称，用于日志记录
     * @param classMap  班级映射，键为班级名称，值为班级对象
     * @param colIndex  列索引
     * @return 匹配到的班级对象，如果未找到则返回 null
     */
    public Class MatchClass(Row headerRow, String sheetName, Map<String, Class> classMap, int colIndex) {
        // 获取班级名称，调用 SymbolUtils 处理单元格值
        String className = SymbolUtils.getCellValueAsString(headerRow.getCell(colIndex));
        if (className == null || className.isEmpty()) {
            // 如果班级名称为空，记录警告日志
            log.warn("未找到班级名称在Sheet: '{}', 列索引: {}", sheetName, colIndex + 1);
            return null;
        }

        // 从班级映射中获取对应的班级对象
        Class classEntity = classMap.get(className);
        if (classEntity == null) {
            // 如果班级对象不存在，记录警告日志
            log.warn("未找到班级: '{}' 在Sheet: '{}'", className, sheetName);
            return null;
        }

        // 返回匹配到的班级对象
        return classEntity;
    }
}

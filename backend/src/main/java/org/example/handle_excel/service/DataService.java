package org.example.handle_excel.service;

import org.example.handle_excel.mapper.ClassMapper;
import org.example.handle_excel.mapper.CourseMapper;
import org.example.handle_excel.mapper.TeacherMapper;
import org.example.handle_excel.bean.Class;
import org.example.handle_excel.bean.Course;
import org.example.handle_excel.bean.Teacher;
import org.example.handle_excel.utils.SymbolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@SuppressWarnings("all")

/**
 * 数据服务类，用于从数据库中获取课程、班级、教师数据并进行处理。
 * 通过调用 Mapper 接口获取数据，并构建便于后续处理的映射结构。
 */
@Service
public class DataService {

    @Autowired
    private CourseMapper courseMapper; // 课程 Mapper

    @Autowired
    private ClassMapper classMapper; // 班级 Mapper

    @Autowired
    private TeacherMapper teacherMapper; // 教师 Mapper

    /**
     * 获取所有课程并创建基础课程名称到课程列表的映射。
     * 处理步骤：
     * 1. 调用 courseMapper 获取所有课程。
     * 2. 使用 Stream API 遍历每个课程，设置课程备注。
     * 3. 使用 SymbolUtils 去除课程名称中的符号，并按去除后的名称进行分组。
     *
     * @return 基础课程名称到课程列表的映射
     */
    public Map<String, List<Course>> getCourseMap() {
        List<Course> courseList = courseMapper.getAllCourses(); // 获取所有课程
        return courseList.stream()
                // 为每个课程设置备注，处理课程名称中的符号
                .peek(course -> course.setRemark(SymbolUtils.process(course.getCourName())))
                // 使用去除符号后的课程名称作为键，将同名的课程收集到一起
                .collect(Collectors.groupingBy(
                        course -> SymbolUtils.removeSymbol(course.getCourName())
                ));
    }

    /**
     * 获取所有班级并创建班级名称到班级对象的映射。
     * 处理步骤：
     * 1. 调用 classMapper 获取所有班级。
     * 2. 使用 Stream API 将班级列表转换为 Map，键为班级名称，值为班级对象。
     *
     * @return 班级名称到班级对象的映射
     */
    public Map<String, Class> getClassMap() {
        List<Class> classList = classMapper.getAllClasses(); // 获取所有班级
        return classList.stream()
                .collect(Collectors.toMap(
                        Class::getClassName, // 班级名称作为键
                        classEntity -> classEntity // 班级对象作为值
                ));
    }

    /**
     * 获取所有教师并创建教师姓名到教师列表的映射。
     * 处理步骤：
     * 1. 调用 teacherMapper 获取所有教师。
     * 2. 使用 Stream API 将教师列表转换为 Map，键为教师的姓和名组合，值为教师对象列表。
     *    这样可以处理同名教师的情况。
     *
     * @return 教师姓名到教师列表的映射
     */
    public Map<String, List<Teacher>> getTeacherMap() {
        List<Teacher> teacherList = teacherMapper.getAllTeachers(); // 获取所有教师
        return teacherList.stream()
                .collect(Collectors.groupingBy(
                        // 使用教师的姓和名组合作为键，将同名的教师收集到一起
                        teacher -> teacher.getTrFirstName() + teacher.getTrName()
                ));
    }
}

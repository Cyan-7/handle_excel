package org.example.handle_excel.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.handle_excel.bean.Course;

import java.util.List;

/**
 * MyBatis Mapper 接口，用于操作课程数据。
 * 通过 MyBatis 自动实现与数据库的交互。
 */
@Mapper
public interface CourseMapper {
    /**
     * 获取所有课程信息。
     *
     * @return 课程列表
     */
    List<Course> getAllCourses();
}

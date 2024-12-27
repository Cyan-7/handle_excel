package org.example.handle_excel.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.handle_excel.bean.Teacher;

import java.util.List;

/**
 * MyBatis Mapper 接口，用于操作教师数据。
 * 通过 MyBatis 自动实现与数据库的交互。
 */
@Mapper
public interface TeacherMapper {
    /**
     * 获取所有教师信息。
     *
     * @return 教师列表
     */
    List<Teacher> getAllTeachers();
}

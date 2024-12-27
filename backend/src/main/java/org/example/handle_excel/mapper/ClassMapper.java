package org.example.handle_excel.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.handle_excel.bean.Class;

import java.util.List;

/**
 * MyBatis Mapper 接口，用于操作班级数据。
 * 通过 MyBatis 自动实现与数据库的交互。
 */
@Mapper
public interface ClassMapper {
    /**
     * 获取所有班级信息。
     *
     * @return 班级列表
     */
    List<Class> getAllClasses();
}

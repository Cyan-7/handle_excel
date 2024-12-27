package org.example.handle_excel.bean;

import lombok.*;

/**
 * 代表一个班级的实体类。
 * 使用 Lombok 注解简化代码，如生成 getters、setters、构造函数等。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Class {
    /**
     * 班级编号
     */
    private String classNum;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 年级
     */
    private String grade;
}

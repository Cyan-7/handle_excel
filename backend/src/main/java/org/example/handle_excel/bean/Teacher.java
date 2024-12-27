package org.example.handle_excel.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 代表一个教师的实体类。
 * 使用 Lombok 注解简化代码，如生成 getters、setters、构造函数等。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    /**
     * 教师编号
     */
    private String trNum;

    /**
     * 教师的姓
     */
    private String trFirstName;

    /**
     * 教师的名
     */
    private String trName;
}

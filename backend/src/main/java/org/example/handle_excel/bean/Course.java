package org.example.handle_excel.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 代表一门课程的实体类。
 * 使用 Lombok 注解简化代码，如生成 getters、setters、构造函数等。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    /**
     * 课程编号
     */
    private String courNum;

    /**
     * 课程名称
     */
    private String courName;

    /**
     * 开始时间（可能指课程的上课时间）
     */
    private String ttime;

    /**
     * 结束时间（可能指课程的下课时间）
     */
    private String utime;

    /**
     * 课程备注，每个备注可能包含课程的额外信息，如学期、类型等。
     */
    private List<ArrayList<String>> remark;
}

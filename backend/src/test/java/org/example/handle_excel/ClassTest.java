package org.example.handle_excel;

import org.example.handle_excel.bean.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClassTest {

    @Test
    void test1(){
        Teacher teacher = new Teacher("123", "123", "123");
        System.out.println(teacher.getTrNum());
    }
}

package org.example.handle_excel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class CourseTest {

    // 修改后的正则表达式：匹配中英文括号内的内容
    private static final String BRACKET_CONTENT_REGEX = "[（(][^）)]*?[）)]";

    // 将括号中的内容拆分为 ArrayList
    public static List<ArrayList<String>> extractBracketContents(String input) {
        List<ArrayList<String>> resultList = new ArrayList<>();
        if (input == null || input.isEmpty()) {
            return resultList;
        }

        // 编译正则表达式
        Pattern pattern = Pattern.compile(BRACKET_CONTENT_REGEX);
        // 创建匹配器对象
        Matcher matcher = pattern.matcher(input);

        // 查找所有匹配项并添加到结果列表中
        while (matcher.find()) {
            // 获取括号中的内容
            String content = matcher.group();

            // 去掉左右括号
            String innerContent = content.substring(1, content.length() - 1);

            // 按空格拆分括号内的内容，这里你可以根据需求选择其他分隔符
            String[] parts = innerContent.split("\\s+");

            // 将拆分后的部分存入一个 ArrayList
            ArrayList<String> contentList = new ArrayList<>();
            contentList.add("(");
            Collections.addAll(contentList, parts);
            contentList.add(")");

            // 将拆分后的 ArrayList 添加到结果列表
            resultList.add(contentList);
        }

        return resultList;
    }

    @Test
    void test3() {
        // 测试用例
        String testString = "体育二（武术校本课程）（二上）";
        List<ArrayList<String>> result = extractBracketContents(testString);

        // 输出结果
        System.out.println("提取到的括号内容数量: " + result.size());
        for (int i = 0; i < result.size(); i++) {
            System.out.println("第" + (i + 1) + "个括号内容拆分后的结果: " + result.get(i));
        }
    }

    // 去掉括号及括号内的内容
    public static String removeBracketsAndContent(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // 编译正则表达式
        Pattern pattern = Pattern.compile(BRACKET_CONTENT_REGEX);
        Matcher matcher = pattern.matcher(input);

        // 替换所有匹配的括号及内容为空字符串
        String result = matcher.replaceAll("");

        return result;
    }

    @Test
    void test4(){
        String input = "体育二（武术校本课程）（二上 上学期）";
        String output = removeBracketsAndContent(input);

        System.out.println("去掉括号后的字符串: " + output);
    }
}

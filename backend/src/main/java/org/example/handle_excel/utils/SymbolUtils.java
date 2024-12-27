package org.example.handle_excel.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@SuppressWarnings("all")

/**
 * 符号处理工具类，用于处理字符串中的符号，如括号。
 * 提供方法提取括号内的内容、移除括号及其内容，以及获取单元格的字符串值。
 */
public class SymbolUtils {

    // 正则表达式，匹配中英文括号内的内容
    private static final String BRACKET_CONTENT_REGEX = "[（(][^）)]*?[）)]";

    /**
     * 处理字符串，提取括号内的内容并拆分。
     * 处理步骤：
     * 1. 使用正则表达式查找所有括号及其内部的内容。
     * 2. 去除括号，只保留内部内容。
     * 3. 按空格拆分内部内容。
     * 4. 将拆分后的内容存入 ArrayList，并添加括号标记。
     * 5. 将每个处理后的列表添加到结果列表中。
     *
     * @param input 输入字符串，可能包含括号及其内容
     * @return 括号内内容的列表，每个内容为一个字符串列表
     */
    public static List<ArrayList<String>> process(String input) {
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
            // 获取括号中的内容，包括括号
            String content = matcher.group();

            // 去掉左右括号，保留内部内容
            String innerContent = content.substring(1, content.length() - 1);

            // 按空格拆分括号内的内容，可以根据需要更改分隔符
            String[] parts = innerContent.split("\\s+");

            // 将拆分后的部分存入一个 ArrayList
            ArrayList<String> contentList = new ArrayList<>();
            contentList.add("("); // 添加左括号标记
            Collections.addAll(contentList, parts); // 添加拆分后的内容
            contentList.add(")"); // 添加右括号标记

            // 将拆分后的 ArrayList 添加到结果列表
            resultList.add(contentList);
        }

        return resultList;
    }

    /**
     * 移除字符串中的括号及其内容。
     * 处理步骤：
     * 1. 使用正则表达式匹配所有括号及其内部内容。
     * 2. 将匹配到的内容替换为空字符串。
     *
     * @param input 输入字符串，可能包含括号及其内容
     * @return 移除括号及内容后的字符串
     */
    public static String removeSymbol(String input){
        if (input == null || input.isEmpty()) {
            return input;
        }

        // 编译正则表达式
        Pattern pattern = Pattern.compile(BRACKET_CONTENT_REGEX);
        // 创建匹配器对象
        Matcher matcher = pattern.matcher(input);

        // 替换所有匹配的括号及内容为空字符串
        return matcher.replaceAll("");
    }

    /**
     * 获取单元格的字符串值，去除前后空格。
     * 处理步骤：
     * 1. 根据单元格的类型，提取其内容并转换为字符串。
     * 2. 对于数值类型，检查是否为日期格式或整数，适当转换。
     * 3. 对于公式类型，尝试获取公式的计算结果。
     *
     * @param cell 单元格对象
     * @return 单元格内容的字符串表示，去除前后空格
     */
    public static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim(); // 返回字符串内容，去除空格
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 如果是日期格式，返回日期字符串
                    return cell.getDateCellValue().toString();
                } else {
                    double numericValue = cell.getNumericCellValue();
                    // 检查是否为整数，去除小数点后多余的零
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue()); // 返回布尔值字符串
            case FORMULA:
                // 尝试获取公式结果
                try {
                    switch (cell.getCachedFormulaResultType()) {
                        case STRING:
                            return cell.getStringCellValue().trim(); // 返回字符串结果，去除空格
                        case NUMERIC:
                            double numericValue = cell.getNumericCellValue();
                            if (numericValue == (long) numericValue) {
                                return String.valueOf((long) numericValue); // 返回整数形式
                            } else {
                                return String.valueOf(numericValue); // 返回浮点数形式
                            }
                        case BOOLEAN:
                            return String.valueOf(cell.getBooleanCellValue()); // 返回布尔值字符串
                        default:
                            return ""; // 其他类型返回空字符串
                    }
                } catch (IllegalStateException e) {
                    // 如果无法获取公式结果，返回空字符串
                    return "";
                }
            case BLANK:
            default:
                return ""; // 空单元格或未知类型返回空字符串
        }
    }
}

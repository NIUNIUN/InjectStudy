package com.qinglianyun.buildsrc.utils;

import com.qinglianyun.buildsrc.Ad2;

import org.gradle.api.Project;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tang_xqing on 2020/8/26.
 */
public class Utils {
    private static Project mProject;

    public static void setProject(Project project) {
        mProject = project;
    }

    public static Project getmProject() {
        return mProject;
    }

    public static Ad2 getAd2Extension() {
        return mProject.getExtensions().getByType(Ad2.class);
    }

    private static String WILDCARD_STAR = "\\*{1,3}";

    public static boolean isEmpty(String text) {
        return text == null || text.trim().length() < 1;
    }

    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    private static boolean wildcardMatch(String pattern, String target) {
        if (isEmpty(pattern) || isEmpty(target)) {
            return false;
        }
        try {
            String[] split = pattern.split(WILDCARD_STAR);
            //
            /**
             * 如果以分隔符开头和结尾，第一位会为空字符串，最后一位不会为空字符，所以*Activity和*Activity*的分割结果一样。
             * 所以为了区分，需要手动添加一个空字符串。
             */
            if (pattern.endsWith("*")) {//因此需要在结尾拼接一个空字符
                List<String> strings = new LinkedList<>(Arrays.asList(split));
                strings.add("");
                split = new String[strings.size()];
                strings.toArray(split);
            }
            for (int i = 0; i < split.length; i++) {
                String part = split[i];
                System.out.println("part = " + part);
                if (isEmpty(target)) {
                    return false;
                }

                // 判断以pattern开头
                if (i == 0 && isNotEmpty(part)) {
                    if (!target.startsWith(part)) {
                        return false;
                    }
                }

                // 判断以pattern结尾
                if (i == split.length - 1 && isNotEmpty(part)) {
                    if (!target.endsWith(part)) {
                        return false;
                    } else {
                        return true;
                    }
                }

                // 如果part是空字符串则继续下一个
                if (part == null || part.trim().length() < 1) {
                    continue;
                }

                // 判断是否存在part部分，如果不存在则未匹配，存在则截取符合pattern的部分，用剩余的part继续判断。
                int index = target.indexOf(part);
                if (index < 0) {
                    return false;
                }
                int newStart = index + part.length() + 1;
                if (newStart < target.length()) {
                    target = target.substring(newStart);
                } else {
                    target = "";
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

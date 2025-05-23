package oo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangy
 * @date 2021-2-7 13:39
 */

public class StringReg {
    static String pattern1 = "ab.cd";
    static String pattern2 = "ab.cd.ef";
    static String pattern3 = "java.util.String";
    static String pattern4 = "ok";
    static String pattern5 = "/profile/upload/2020/09/15/03589.png";

    static String pattern6 = "hello@mary";

    /** 判断字符串是否匹配 */
    static String pattern12(String reg) {
        boolean b1 = Pattern.matches(reg, pattern1);
        boolean b2 = Pattern.matches(reg, pattern2);
        boolean b3 = Pattern.matches(reg, pattern3);
        boolean b4 = Pattern.matches(reg, pattern4);
        return b1 + ", " + b2 + ", " + b3 + ", " + b4;
    }

    /** 匹配并替换首字母 */
    static String replaceFirst(String reg) {
        // return pattern1
        // .replaceFirst(reg, String.valueOf(pattern1.charAt(0))
        // .toUpperCase());
        return Pattern
                .compile(reg)
                .matcher(pattern1)
                .replaceFirst(pattern1
                        .substring(0, 1)
                        .toUpperCase());
    }

    /** 获取文件的格式后缀 */
    static String getSuffix(String reg, String target) {
        Matcher matcher = Pattern.compile(reg).matcher(target);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(pattern12("^([a-z]+\\.)+[a-z]+$"));
        System.out.println(replaceFirst("\\w"));
        System.out.println(getSuffix("(\\.\\w{3,4})", pattern5));
        System.out.println(getSuffix("@(\\S+)", pattern6));
    }
}

/*
* true, true, false, false
* Ab.cd
* .png
* mary
*/
/// :~
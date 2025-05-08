package oo.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/16 / 19:59
 */
public class LambdaIntro {

    public static void main(String[] args) {
        String[] s = new String[] { "baidu", "ali", "kingdee" };

        Arrays.sort(s, new Comparator<String>() {
            @Override
            public int compare(String s3, String str) {
                return s3.compareToIgnoreCase(str);
            }
        });
        Arrays.sort(s, (s3, str) -> s3.compareToIgnoreCase(str));
        Arrays.sort(s, String::compareToIgnoreCase);

        Arrays.sort(s, LambdaIntro::localCompare);
        System.out.println(Arrays.toString(s));

        ArrayList<String> s1 = new ArrayList<>();
        s1.add("mercury");
        s1.add("venus");
        s1.add("earch");
        s1.add("mars");
        for (String s2 : s1) {
            System.out.println(s2);
        }
        s1.forEach(e -> System.out.println(e));
    }

    private static int localCompare(String o1, String o2) {
        if (o1.length() != o2.length())
            return o1.length() - o2.length();
        return o1.compareTo(o2);
    }

}

class StringLengthComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        // if (o1.length() != o2.length())
        //     return o1.length() - o2.length();
        // return o1.compareTo(o2);
        return o1.length() - o2.length();
    }
}

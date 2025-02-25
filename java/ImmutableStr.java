import java.lang.reflect.Field;

/**
 * How to understand string immutable in java?<br>
 * 
 * 1. 线程安全性<br>
 * 2. 性能提升（缓存）<br>
 * 3. HashCode不变，避免散列表异常<br>
 * <br>
 * 
 * 所有对字符串的更改，都会创建一个新的字符串。<br>
 * 
 * 实际上是Java语言的约束，所以String类以及value域才用final修饰。<br>
 * 
 * 可以通过反射修改字符串。但是这样会有副作用。<br>
 * 
 * 如果要使用可变字符串，使用<code>StringBuffer</code>。
 */

class ImmutableStr {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String str = "spam";
        System.out.printf("Before: literal: %s, hashcode: %s\n", str, str.hashCode());
        Class<? extends String> clazz = str.getClass();
        try {
            Field f = clazz.getDeclaredField("value");
            f.setAccessible(true);
            byte[] strByte = (byte[]) f.get(str);
            // Stream.of(strByte).forEach(System.out::print);
            strByte[0] = 'S';
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.printf("Modified by Reflection: literal: %s, hashcode: %s\n", str, str.hashCode());
        // 字面量已经更改了，但是hashcode没变
        // 真的是同一个字符串么？
        String otherString = "Spam";

        System.out.println(str.charAt(0) == otherString.charAt(0)); // true
        System.out.println(str == otherString); // false
        System.out.printf("hashCode of otherString: %s\n", otherString.hashCode());

    }
}
/*
 * 程序运行警告
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by ImmutableStr 
                    (file:/__/redhat.java/jdt_ws/snippets_5eaf7742/bin/) to field java.lang.String.value
WARNING: Please consider reporting this to the maintainers of ImmutableStr
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future releas
 */
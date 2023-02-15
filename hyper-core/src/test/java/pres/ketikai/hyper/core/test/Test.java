package pres.ketikai.hyper.core.test;

import java.util.*;

/**
 * <p>标题</p>
 *
 * <p>Created on 2023/2/12 12:44</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public class Test {

    public static void main(String[] args) throws Exception {
        Map<String, Boolean> map = new HashMap<>(16);
        map.put("0", false);
        map.put("1", false);
        map.put("2", false);
        map.put("3", false);
        map.put("4", false);
        map.put("5", false);
        map.forEach((k, v) -> System.out.print("(" + k + ", " + v + ")"));

        System.out.println();
        map = new LinkedHashMap<>(map);

        map.forEach((k, v) -> System.out.print("(" + k + ", " + v + ")"));

        Set<String> set = new HashSet<>();
        set.add("0");
        set.add("1");
        set.add("2");
        set.add("3");
        set.add("4");
        set.add("5");

        set.forEach(System.out::print);
    }
}

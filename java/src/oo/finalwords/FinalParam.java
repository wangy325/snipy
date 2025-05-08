package oo.finalwords;

import oo.references.Employee;

/**
 * simple example of final parameters
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/14 / 17:45
 */
public class FinalParam {

    static void with(final Employee e) {
        e.raiseSalary(3);
        System.out.println("salary of e = " + e.getSalary());
    }

    static void swap(final Employee j, final Employee k) {
        Employee tmp = j;
        // check error, final var assign is not allowed
        // k = temp;
        // j = k;
    }

    public static void main(String[] args) {
        Employee x = new Employee("ali", 1000);
        with(x);
        System.out.println("salary of x = " + x.getSalary());
    }
}

/* output:
 * salary of e = 3000
 * salary of x = 3000
 */// :~

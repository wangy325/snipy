package oo.references;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/14 / 15:22
 */
public class ByRef {

    static void raiseSalary(Employee o) {
        o.raiseSalary(3);
        System.out.println("after salary of o = " + o.getSalary());
    }

    static void swap(Employee j, Employee k) {
        System.out.println("before swap j.name = " + j.getName());
        System.out.println("before swap k.name = " + k.getName());
        Employee temp = j;
        j = k;
        k = temp;
        System.out.println("after swap j.name = " + j.getName());
        System.out.println("after swap k.name = " + k.getName());
    }

    public static void main(String[] args) {
        Employee a = new Employee("ali", 1200);
        System.out.println("before salary = " + a.getSalary());
        raiseSalary(a);
        System.out.println("after salary of a = " + a.getSalary());
        Employee b = new Employee("bad", 1300);
        swap(a, b);
        System.out.println("after swap a.name = " + a.getName());
        System.out.println("after swap b.name = " + b.getName());
    }
}


/*
 * before salary = 1200
 * after salary of o = 3600
 * after salary of a = 3600
 */// :~
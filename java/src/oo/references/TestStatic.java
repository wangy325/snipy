package oo.references;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/14 / 14:14
 */
class Employee {
    static int nextId = 0;
    private int id;
    private String name;
    private int salary;

    public Employee(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    public void setId() {
        this.id = nextId;
        nextId++;
    }

    // getters
    public static int getNextId() {
        return nextId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }
}

public class TestStatic {

    public static void main(String[] args) {
        Employee[] x = new Employee[3];
        x[0] = new Employee("alex", 5000);
        x[1] = new Employee("bob", 6000);
        x[2] = new Employee("cup", 7000);

        for (Employee e : x) {
            e.setId();
            System.out.println(
                    "id = " + e.getId() +
                            " name = " + e.getName() +
                            " salary = " + e.getSalary());
        }
        System.out.println("the nextId is:  " + Employee.getNextId());
    }
}

/*
 * output:
 * id = 0 name = alex salary = 5000
 * id = 1 name = bob salary = 6000
 * id = 2 name = cup salary = 7000
 * the nextId is: 3
 */// :~
package oo.references;

import java.util.Objects;

public class Employee {
    private String name;
    private int salary;

    public Employee(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }

    public void raiseSalary(int multiple) {
        this.salary = salary * multiple;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Employee employee2 = (Employee) o;

        return Objects.equals(name, employee2.name)
                && salary == employee2.salary;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + salary;
        return result;
    }
}

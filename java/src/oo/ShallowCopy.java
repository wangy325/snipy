package oo;

/**
 * a shallow copy (浅拷贝) may change original object reference
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/23 / 16:15
 */
public class ShallowCopy implements Cloneable {
    private String company;
    private Name name;

    public ShallowCopy(String company, Name name) {
        this.company = company;
        this.name = name;
    }

    public ShallowCopy(String company, String first, String last) {
        this.company = company;
        this.name = new Name(first, last);
    }

    public void changeCompany(String comp) {
        this.company = comp;
    }

    public void changeFirstName(String name) {
        this.name.changeFirstName(name);
    }

    /**
     * it's necessary to implement <code>clone()</code>
     * when you want to make a copy of an object
     *
     * @return the copy object
     * @throws CloneNotSupportedException
     */
    @Override
    protected ShallowCopy clone()
            throws CloneNotSupportedException {
        return (ShallowCopy) super.clone();
    }

    @Override
    public String toString() {
        return "Item{" +
                "company='" + company + '\'' +
                ", name=" + name +
                '}';
    }

    private static class Name {
        private String firstName;
        private String lastName;

        public Name(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        private void changeFirstName(String firstName) {
            this.firstName = firstName;
        }

        @Override
        public String toString() {
            return "Name{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    '}';
        }
    }

    static class Test {
        public static void main(String[] args)
                throws CloneNotSupportedException {
            ShallowCopy i = new ShallowCopy(
                    "app",
                    "steve",
                    "jobs");
            ShallowCopy copy = (ShallowCopy) i.clone();
            // String 是（final）不可变的
            copy.changeCompany("apple");
            // 浅拷贝时，原对象的可变引用也会随拷贝对象改变
            copy.changeFirstName("stephen");

            System.out.println(i);
            System.out.println(copy);
        }
    }
}

/*
 * Item{company='app', name=Name{firstName='stephen', lastName='jobs'}}
 * Item{company='apple', name=Name{firstName='stephen', lastName='jobs'}}
 */
/// :~

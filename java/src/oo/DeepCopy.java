package oo;

/**
 * 使用深拷贝，拷贝才有意义
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/23 / 16:57
 */
public class DeepCopy implements Cloneable {
    private String company;
    private Name name;

    public DeepCopy(String company, Name name) {
        this.company = company;
        this.name = name;
    }

    public DeepCopy(String company, String first, String last) {
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
    protected DeepCopy clone() throws CloneNotSupportedException {
        // 深拷贝所有的可变引用都需要拷贝
        DeepCopy clone = (DeepCopy) super.clone();
        clone.name = this.name.clone();
        return clone;
    }

    @Override
    public String toString() {
        return "Item2{" +
                "company='" + company + '\'' +
                ", name=" + name +
                '}';
    }

    private static class Name implements Cloneable {
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

        /**
         * 拷贝对象时，对象的可变引用也需要拷贝，知道所有对象都不可变为止
         * 实际上，<code>Name</code>类的域都是String，因此拷贝方法到此为止
         *
         * @return
         * @throws CloneNotSupportedException
         */
        @Override
        protected Name clone() throws CloneNotSupportedException {
            return (Name) super.clone();
        }
    }

    static class Test {
        public static void main(String[] args)
                throws CloneNotSupportedException {

            DeepCopy i = new DeepCopy(
                    "app",
                    "steve",
                    "jobs");

            DeepCopy copy = (DeepCopy) i.clone();
            // String 是（final）不可变的
            copy.changeCompany("apple");
            // 深拷贝时，拷贝对象改变和原对象相互独立
            copy.changeFirstName("stephen");

            System.out.println(i);
            System.out.println(copy);
        }
    }
}

/*
 * Item2{company='app', name=Name{firstName='steve', lastName='jobs'}}
 * Item2{company='apple', name=Name{firstName='stephen', lastName='jobs'}}
 */
/// :~
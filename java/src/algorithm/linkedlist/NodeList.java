package algorithm.linkedlist;

import java.util.Objects;

/**
 * 一个单链表，及其基本功能实现
 *
 * @author wangy
 * @version 1.0
 * @date 2021/6/21 / 22:23
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NodeList<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size;

    /*
     * 插入头部
     * 插入尾部
     * 根据index获取元素
     * 根据值删除
     * 根据index删除
     * 反转链表元素
     * 判断回文字符串
     */

    public NodeList() {
        this.head = this.tail = null;
        this.size = 0;
    }

    private Node<E> node(E e) {
        return new Node<>(e);
    }

    int size() {
        return size;
    }

    E getHead() {
        return head.data;
    }

    E getTail() {
        return tail.data;
    }

    void insertHead(E e) {
        Node<E> p = node(e);
        insertHead(p);
    }

    private void insertHead(Node node) {
        node.next = head;
        head = node;
        if (++size == 1)
            tail = head;
    }

    void insertTail(E e) {
        Node<E> p = node(e);
        insertTail(p);
    }

    private void insertTail(Node node) {
        tail.next = node;
        tail = node;
        if (++size == 1)
            head = tail;
    }

    /**
     * 在指定位置插入元素, 若index溢出，则自动将元素插入到链表的头部或者尾部。<br>
     * 若index&lt;0时插入头部，若index&gt;<code>size</code>时，插入尾部。
     * <p>
     * 平均时间复杂度 O(n)
     *
     * @param index 元素插入位置
     * @param e     待插入元素
     */
    void insert(int index, E e) {
        Node<E> i = node(e);
        if (index <= 0) {
            insertHead(i);
        } else if (index >= size) {
            insertTail(i);
        } else {
            Node<E> p = head;
            while (index > 1) {
                p = p.next;
                --index;
            }
            Node<E> next = p.next;
            p.next = i;
            i.next = next;
            ++size;
        }
    }

    E get(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException("index must >= 0 and < " + size);
        Node<E> p = head;
        while (index > 0) {
            p = p.next;
            --index;
        }
        return p.data;
    }

    boolean contains(E e) {
        Node<E> p = head;
        while (p != null) {
            if (p.data.equals(e))
                return true;
            p = p.next;
        }
        return false;
    }

    /**
     * 当链表中存在重复元素时，无法准确获取index。 获取的是该元素第一次出现的index。
     *
     * @param e element
     * @return index of first occurrence of element in single linked list.
     */
    int getIndex(E e) {
        Node<E> p = head;
        int pos = 0;
        while (p != null) {
            if (p.data.equals(e))
                return pos;
            p = p.next;
            pos++;
        }
        return -1;
    }

    /**
     * 移除链表中首次出现的指定元素，若元素不在链表中，则不作处理
     * <p>
     * 平均时间复杂度O(n^2)
     *
     * @param e 待移除的元素
     */
    void remove(E e) {
        Node<E> p = head;
        while (p != null) {
            if (p.data.equals(e)) {
                remove(p);
                return;
            }
            p = p.next;
        }
    }

    /**
     * remove element by index.
     * <p>
     * 时间复杂度 O(n)
     *
     * @param index index of element in linked list, Analogous to the index
     *              semantics of an array.
     */
    void remove(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException("index must >= 0 and < " + size);
        Node<E> del = head;
        Node<E> l = head;
        for (int i = 0; i < index; i++) {
            l = del;
            del = del.next;
        }
        remove(l, del);
    }

    /**
     * 删除指定节点
     * <p>
     * 平均时间复杂度 O(n)
     *
     * @param node 带删除的节点
     */
    private void remove(Node node) {
        Node<E> prev = head;
        Node<E> curr = head;

        while (!curr.equals(node)) {
            prev = curr;
            curr = curr.next;
            if (curr.equals(tail)) {
                if (node.equals(curr)) {
                    break;
                }
                return;
            }
        }
        if (curr.equals(head)) {
            head = prev.next;
        } else if (curr.equals(tail)) {
            tail = prev;
            prev.next = null;
        } else {
            prev.next = curr.next;
        }
        --size;
    }

    /**
     * 快速移除链表中首次出现的元素
     * <p>
     * 平均时间复杂度O(n)
     *
     * @param e 待删除的元素
     */
    void fastRemove(E e) {
        Node<E> l = null;
        Node<E> r = head;
        while (r != null) {
            if (r.data.equals(e)) {
                remove(l, r);
                return;
            }
            l = r;
            r = r.next;
        }
    }

    /**
     * 给定左节点，删除右节点
     * <p>
     * 时间复杂度 O(1)
     *
     * @param l 左节点
     * @param r 右节点
     */
    private void remove(Node<E> l, Node<E> r) {
        if (r.equals(head)) {
            head = r.next;
        } else if (r.equals(tail)) {
            tail = l;
            l.next = null;
        } else {
            l.next = r.next;
        }
        --size;
    }

    /**
     * 反转链表
     * <p>
     * 时间复杂度 O(n)
     */
    void reverse() {
        /*
         * 引入节点，该节点的next总是指向新反转过程中的最新节点；
         * 当所有的节点遍历完成时，其next指向反转后的head；
         */
        Node<E> lead = node(null);
        lead.next = head;
        // 从第二个节点开始，反转链表
        Node<E> cur = head.next;
        /*
         * 引入此节点用来记录当前遍历节点的下一个节点，
         * 避免反转过程中链表"断裂"
         */
        Node<E> tmp;
        while (cur != null) {
            tmp = cur.next;
            cur.next = lead.next;
            lead.next = cur;

            cur = tmp;
        }
        head.next = null;
        tail = head;
        head = lead.next;
    }

    /**
     * 反转head-node节点(不含)之间的元素
     *
     * @param node 标记节点
     */
    private void reverse(Node<E> node) {
        if (!contains(node.data))
            return;
        Node<E> h = new Node(null);
        h.next = node;
        Node<E> cur = head;
        Node next;
        while (!cur.equals(node)) {
            next = cur.next;
            cur.next = h.next;
            h.next = cur;

            cur = next;
        }
        head = h.next;
    }

    /**
     * 判断链表内容是否回文字符串
     * <p>
     * 回文字符串，即字符串从左往右和从右往左读是一样的，如【refer】就是回文字符串
     * <p>
     * 时间复杂度 O(n)
     *
     * @return true yes，false no
     */
    boolean isPalindrome() {
        if (head == null)
            return false;
        Node<E> slow = head;
        Node<E> fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        // 处理1、2个元素的情形
        if (slow.equals(fast)) {
            if (size == 1) {
                return true;
            }
            return head.data.equals(tail.data);
        }
        boolean b = false;
        if (fast.next != null) {
            // 偶数个元素
            slow = slow.next;
            b = true;
        }
        // 反转slow节点之前的元素
        Node<E> rev = slow;
        reverse(rev);
        System.out.println("++++++ after reverse ++++++");
        printAll();

        // 此head和h不一样！链表已被部分反转
        Node<E> pre = head;
        try {
            if (b) {
                while (slow != null) {
                    if (slow.data != pre.data)
                        return false;
                    slow = slow.next;
                    pre = pre.next;
                }
            } else {
                while (slow.next != null) {
                    if (slow.next.data != pre.data)
                        return false;
                    slow = slow.next;
                    pre = pre.next;
                }
            }
        } finally {
            // 还原链表
            reverse(rev);
            System.out.println("++++++ after restore ++++++");
            printAll();
        }
        return true;
    }

    void printAll() {
        System.out.print("LinkedList: [");
        if (size == 1) {
            System.out.print(getHead() + "/"
                    + getTail() + "/" + get(0));
        } else {
            System.out.print(getHead()
                    + "(" + getIndex(getHead()) + "), ");
            for (int i = 1; i < size - 1; i++) {
                System.out.print(get(i)
                        + "(" + getIndex(get(i)) + "), ");
            }
            System.out.print(getTail()
                    + "(" + getIndex(getTail()) + ")");
        }
        System.out.println("]");
    }

    static class Node<U> {
        U data;
        Node<U> next;

        public Node(U data) {
            this.data = data;
            this.next = null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Node<?> node = (Node<?>) o;

            if (!Objects.equals(data, node.data))
                return false;
            return Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            int result = data != null ? data.hashCode() : 0;
            result = 31 * result + (next != null ? next.hashCode() : 0);
            return result;
        }

    }

    public static void main(String[] args) {
        NodeList<Integer> nl = new NodeList<>();
        System.out.println("++++++ insert head 10 ++++++");
        nl.insertHead(10);

        System.out.println("++++++ insert tail 11 ++++++");
        nl.insertTail(11);

        System.out.println("++++++ insert head 9 ++++++");
        nl.insertHead(9);

        nl.printAll();

        System.out.println("contains 9? " + nl.contains(9));

        System.out.println("++++++ remove element ++++++");
        nl.remove(Integer.valueOf(11));
        nl.printAll();

        nl.fastRemove(10);
        nl.printAll();

        System.out.println("++++++ insert element ++++++");
        nl.insert(1, 99);
        nl.insert(1, 89);
        nl.insert(1, 79);
        nl.insert(100, 109);
        nl.insert(-1, -9);
        nl.printAll();

        nl.reverse();
        nl.printAll();

        System.out.println("++++++ judge palindrome ++++++");
        String[] s = new String[] { "r", "e", "f", "f", "e", "r" };
        // String[] s = new String[]{"r", "e", "f", "f", "e", "p"};
        // String[] s = new String[]{"r", "e", "f", "e", "r"};
        // String[] s = new String[]{"r", "e", "f", "e", "p"};
        // String[] s = new String[]{"r"};
        // String[] s = new String[]{"r", "s"};
        // String[] s = new String[]{"r", "r"};
        // String[] s = new String[]{"r", "s", "r"};
        // String[] s = new String[]{"r", "s", "t"};

        NodeList<String> stringNodeList = new NodeList<>();
        for (String str : s) {
            stringNodeList.insertHead(str);
        }
        stringNodeList.printAll();
        /*
         * Node<String> p = stringNodeList.head;
         * for (int i = 0; i < stringNodeList.size; i++) {
         * System.out.println(p + " --> " + p.next);
         * p = p.next;
         * }
         */
        System.out.println(stringNodeList.isPalindrome());

        System.out.println(stringNodeList.get(5));
        stringNodeList.remove(3);
        stringNodeList.printAll();

    }
}

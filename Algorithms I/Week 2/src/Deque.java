import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node head = null;
    private Node tail = null;
    private int size = 0;

    // construct an empty deque
    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        checkNewItem(item);

        if (head == null) {
            Node node = new Node(item);
            head = node;
            tail = node;
        }
        else {
            Node prev = head;
            head = new Node(item);
            head.next = prev;
            prev.prev = head;
        }

        size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        checkNewItem(item);

        if (head == null) {
            Node node = new Node(item);
            head = node;
            tail = node;
        }
        else {
            Node prev = tail;
            tail = new Node(item);
            tail.prev = prev;
            prev.next = tail;

        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkNotEmpty();

        Node firstNode = head;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        else {
            head.prev = null;
        }
        firstNode.next = null;

        size--;

        return firstNode.data;
    }

    // remove and return the item from the end
    public Item removeLast() {
        checkNotEmpty();

        Node lastNode = tail;
        tail = tail.prev;
        if (tail == null) {
            head = null;
        }
        else {
            tail.next = null;
        }
        lastNode.prev = null;

        size--;

        return lastNode.data;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();

        deque.addFirst(1);
        StdOut.println(deque.removeLast());
        deque.addFirst(2);
        deque.addFirst(3);
        StdOut.println(deque.removeLast());

        StdOut.print(deque.size());
        StdOut.println();

        for (Integer item : deque) {
            StdOut.print(item);
        }
    }

    private void checkNewItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item");
        }
    }

    private void checkNotEmpty() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
    }

    private class Node {
        public Item data;
        public Node next;
        public Node prev;

        public Node(Item data) {
            this.data = data;
            next = null;
            prev = null;
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.data;
            current = current.next;

            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}

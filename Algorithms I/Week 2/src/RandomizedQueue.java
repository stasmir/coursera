import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int head = 0;
    private int tail = 0;

    public RandomizedQueue() {
        items = (Item[]) new Object[2];
    }                // construct an empty randomized queue


    public boolean isEmpty() {
        return size() == 0;
    }                // is the queue empty?


    public int size() {
        return head - tail;
    }                       // return the number of items on the queue


    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item");
        }

        if (head == items.length) {
            resize(size() * 2);
        }

        items[head] = item;
        head++;
    }          // add the item


    public Item dequeue() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }

        Item item = items[tail];
        items[tail] = null;
        tail++;

        if (size() > 0 && head == items.length / 4) {
            resize(items.length / 2);
        }

        return item;
    }                   // remove and return a random item


    public Item sample() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        return items[tail + StdRandom.uniform(size())];
    }                    // return (but do not remove) a random item


    public Iterator<Item> iterator() {
        return new QueueIterator();
    }        // return an independent iterator over items in random order


    public static void main(String[] args) {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();

        q.enqueue(4);
        StdOut.println(q.dequeue());
        q.enqueue(3);
        StdOut.println(q.dequeue());
        q.enqueue(2);
        StdOut.println(q.dequeue());

        StdOut.println(q.size());
        for (Integer i : q) {
            StdOut.println(i);
        }

    }  // unit testing (optional)

    private void resize(int capacity) {
        if (capacity == 0) {
            capacity = 2;
        }

        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < size(); i++) {
            temp[i] = items[i + tail];
        }
        items = temp;
        head = size();
        tail = 0;
    }

    private class QueueIterator implements Iterator<Item> {
        private final Item[] array;
        private int currentIndex = 0;

        public QueueIterator() {
            Item[] temp = (Item[]) new Object[size()];
            for (int i = 0; i < size(); i++) {
                temp[i] = items[i + tail];
            }
            array = temp;
            StdRandom.shuffle(array);
        }

        @Override
        public boolean hasNext() {
            return currentIndex < array.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item item = array[currentIndex];
            currentIndex++;

            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
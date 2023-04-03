import java.util.*;

public class ArraySet<E> implements SortedSet<E> {
    private final int capacity;
    private final ArrayList<E> elementData;
    private final Comparator<? super E> comparator;

    public ArraySet() {
        this(Collections.EMPTY_LIST);
    }

    public ArraySet(Collection<? extends E> c, Comparator<? super E> co) {
        TreeSet<E> set = new TreeSet<>(co);
        set.addAll(c);
        this.elementData = new ArrayList<>(set);
        this.capacity = set.size();
        this.comparator = co;
    }

    public ArraySet(Collection<? extends E> c) {
        TreeSet<E> set = new TreeSet<>(c);
        this.elementData = new ArrayList<>(set);
        this.capacity = set.size();
        this.comparator = null;
    }

    public ArraySet(SortedSet<E> c) {
        TreeSet<E> set = new TreeSet<>(c.comparator());
        this.elementData = new ArrayList<>(set);
        this.capacity = set.size();
        this.comparator = set.comparator();
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        if (comparator == null) {
            throw new UnsupportedOperationException("comparator is null");
        }
        if (comparator.compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException();
        }
        TreeSet<E> subset = new TreeSet<>(comparator);
        int from = capacity;
        for(int i = 0; i < capacity; i++) {
            if (comparator.compare(fromElement, elementData.get(i)) <= 0) {
                from = i;
                break;
            }
        }
        for(int i = from; i < capacity; i++) {
            if (comparator.compare(toElement, elementData.get(i)) <= 0) {
                break;
            }
            subset.add(elementData.get(i));
        }
        return subset;
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        if (comparator == null) {
            throw new UnsupportedOperationException("comparator is null");
        }
        TreeSet<E> subset = new TreeSet<>(comparator);
        for(int i = 0; i < capacity; i++) {
            if (comparator.compare(toElement, elementData.get(i)) <= 0) {
                break;
            }
            subset.add(elementData.get(i));
        }
        return subset;
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        if (comparator == null) {
            throw new UnsupportedOperationException("comparator is null");
        }
        TreeSet<E> subset = new TreeSet<>(comparator);
        for(int i = capacity-1; i >= 0; i--) {
            if (comparator.compare(fromElement, elementData.get(i)) > 0) {
                break;
            }
            subset.add(elementData.get(i));
        }
        return subset;
    }

    @Override
    public E first() {
        if (capacity == 0) {
            throw new NoSuchElementException();
        }
        return elementData.get(0);
    }

    @Override
    public E last() {
        if (capacity == 0) {
            throw new NoSuchElementException();
        }
        return elementData.get(capacity-1);
    }

    @Override
    public int size() {
        return capacity;
    }

    @Override
    public boolean isEmpty() {
        return capacity == 0;
    }

    @Override
    public boolean contains(Object o) {
        return Collections.binarySearch(elementData,(E) o, comparator) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return elementData.iterator();
    }

    @Override
    public Object[] toArray() {
        return elementData.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return elementData.toArray(a);
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException("add");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("remove");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (comparator == null) {
            throw new UnsupportedOperationException("comparator is null");
        }
        TreeSet<E> set = new TreeSet<>(comparator);
        set.addAll(elementData);
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("addAll");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("retainAll");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("removeAll");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("clear");
    }

    @Override
    public String toString() {
        return elementData.toString();
    }
}

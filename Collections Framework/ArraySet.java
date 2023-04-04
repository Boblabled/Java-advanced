import java.util.*;

public class ArraySet<E> extends AbstractSet<E> implements SortedSet<E> {
    private final List<E> elementData;
    private final Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public ArraySet() {
        this(Collections.EMPTY_LIST);
    }

    public ArraySet(Collection<? extends E> c) {
        this(toList(c, null), null);
    }

    public ArraySet(Collection<? extends E> c, Comparator<? super E> co) {
        this.elementData = toList(c, co);
        this.comparator = co;
    }

    @SuppressWarnings("unchecked")
    public ArraySet(Comparator<E> c) {
        this(toList(Collections.EMPTY_LIST, c), c);
    }

    private static <E> ArrayList<E> toList(Collection<? extends E> c, Comparator<? super E> co) {
        TreeSet<E> set = new TreeSet<>(co);
        set.addAll(c);
        return new ArrayList<>(set);
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
        TreeSet<E> tailSet = (TreeSet<E>) tailSet(fromElement);
        TreeSet<E> headSet = (TreeSet<E>) headSet(toElement);
        tailSet.retainAll(headSet);
        return tailSet;
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        if (comparator == null) {
            throw new UnsupportedOperationException("comparator is null");
        }
        int to = Collections.binarySearch(elementData, toElement, comparator);
        if (to < 0) {
            to = -1 * to - 1;
        }
        return new TreeSet<>(elementData.subList(0, to));
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        if (comparator == null) {
            throw new UnsupportedOperationException("comparator is null");
        }
        int from = Collections.binarySearch(elementData, fromElement, comparator);
        if (from < 0) {
            from = -1 * from - 1;
        }
        return new TreeSet<>(elementData.subList(from, elementData.size()));
    }

    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return elementData.get(0);
    }

    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return elementData.get(elementData.size() - 1);
    }

    @Override
    public int size() {
        return elementData.size();
    }

    @Override
    public boolean isEmpty() {
        return elementData.size() == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return Collections.binarySearch(elementData, (E) o, comparator) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return elementData.iterator();
    }
}

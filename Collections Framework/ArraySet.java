import java.util.*;

public class ArraySet<E> extends AbstractSet<E> implements SortedSet<E> {
    private final List<E> elementData;
    private final Comparator<? super E> comparator;

    public ArraySet() {
        this(List.of());
    }

    public ArraySet(Collection<? extends E> c) {
        // :NOTE: Comparator.naturalOrder()
        this(c, null);
    }

    public ArraySet(Comparator<E> c) {
        this(List.of(), c);
    }

    public ArraySet(Collection<? extends E> c, Comparator<? super E> co) {
        this(toList(c, co), co);
    }

    private ArraySet(List<E> data, Comparator<? super E> c) {
        this.elementData = data;
        this.comparator = c;
    }

    private static <E> List<E> toList(Collection<? extends E> c, Comparator<? super E> co) {
        TreeSet<E> set = new TreeSet<>(co);
        set.addAll(c);
        return List.copyOf(set);
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        if (comparator.compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException();
        }
        return tailSet(fromElement).headSet(toElement);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        int to = Collections.binarySearch(elementData, toElement, comparator);
        if (to < 0) {
            to = -to - 1;
        }
        return new ArraySet<>(elementData.subList(0, to), comparator);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        int from = Collections.binarySearch(elementData, fromElement, comparator);
        if (from < 0) {
            from = -from - 1;
        }
        return new ArraySet<>(elementData.subList(from, elementData.size()), comparator);
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

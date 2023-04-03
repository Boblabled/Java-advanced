import java.util.*;

// :NOTE: AbstractSet
public class ArraySet<E> extends AbstractSet<E> implements SortedSet<E> {
    private final List<E> elementData;
    private final Comparator<? super E> comparator;

    public ArraySet() {
        this(Collections.EMPTY_LIST);
    }

    // :NOTE: duplicate code
    public ArraySet(Collection<? extends E> c) {
        TreeSet<E> set = new TreeSet<>(c);
        this.elementData = new ArrayList<>(set);
        this.comparator = null;
    }

    public ArraySet(Collection<? extends E> c, Comparator<? super E> co) {
        this(toList(c, co), co);
    }

    public ArraySet(Comparator<E> c) {
        TreeSet<E> set = new TreeSet<>(c);
        this.elementData = new ArrayList<>(set);
        this.comparator = set.comparator();
    }

    private ArraySet(List<E> data, Comparator<? super E> c) {
        this.elementData = data;
        this.comparator = c;
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

    // :NOTE: duplicate code
    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        if (comparator == null) {
            throw new UnsupportedOperationException("comparator is null");
        }
        if (comparator.compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException();
        }

        // 1. Коллекция уже отсортирована
        // 2. Коллекция не изменяемая
        TreeSet<E> subset = new TreeSet<>(comparator);
        int from = elementData.size();
        for (int i = 0; i < elementData.size(); i++) {
            if (comparator.compare(fromElement, elementData.get(i)) <= 0) {
                from = i;
                break;
            }
        }
        for (int i = from; i < elementData.size(); i++) {
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
        for (int i = 0; i < elementData.size(); i++) {
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
//     :NOTE:   Collections.binarySearch()
//     :NOTE:   List.subList()
        TreeSet<E> subset = new TreeSet<>(comparator);
        for (int i = elementData.size() - 1; i >= 0; i--) {
            if (comparator.compare(fromElement, elementData.get(i)) > 0) {
                break;
            }
            subset.add(elementData.get(i));
        }
        return subset;
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

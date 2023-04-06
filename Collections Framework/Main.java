import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        Car c1 = new Car(1);
        Car c2 = new Car(2);
        ArraySet<Object> set = new ArraySet<>(List.of(c1, c2));
//        TreeSet<Cat> objects = new TreeSet<>();
//        objects.add(new Cat());
        System.out.println(set.subSet(c1, c2));
    }

    private static class Cat {

    }

    private static class Car implements Comparable<Car> {
        private static final Comparator<Car> COMP = Comparator.comparingInt(Car::getId);
        private final int id;

        private Car(int id) {
            this.id = id;
        }

        @Override
        public int compareTo(Car o) {
            return COMP.compare(this, o);
        }

        public int getId() {
            return id;
        }
    }
}

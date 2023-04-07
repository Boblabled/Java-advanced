import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentDB implements StudentQuery {
    private static final Comparator<Student> NAME_COMPARATOR =
            Comparator.comparing(Student::getLastName)
                    .thenComparing(Student::getFirstName).reversed()
                    .thenComparing(Student::getId);

    private static final Comparator<Student> DEFAULT_COMPARATOR = Comparator.naturalOrder();

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return get(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return get(students, Student::getLastName);
    }
    @Override
    public List<GroupName> getGroups(List<Student> students) {
        return get(students, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return get(students, s -> s.getFirstName() + " " + s.getLastName());
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return students.stream().map(Student::getFirstName)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students.stream()
                .max(DEFAULT_COMPARATOR)
                .map(Student::getFirstName).orElse("");
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return sort(students.stream(), DEFAULT_COMPARATOR);
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return sort(students.stream(), NAME_COMPARATOR);
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return find(students, name, Student::getFirstName);
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return find(students, name, Student::getLastName);
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return find(students, group, Student::getGroup);
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return students.stream()
                .filter(s -> Objects.equals(s.getGroup(), group))
                .collect(Collectors.toUnmodifiableMap(
                        Student::getLastName,
                        Student::getFirstName,
                        BinaryOperator.minBy(Comparator.naturalOrder())
                ));
    }

    private static <T> List<T> get(List<Student> students, Function<Student, T> func) {
        return students.stream().map(func).collect(Collectors.toCollection(ArrayList::new));
    }

    private static List<Student> sort(Stream<Student> students, Comparator<Student> comparator) {
        return students.sorted(comparator)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static <T> List<Student> find(Collection<Student> students, T param, Function<Student, T> f) {
        return sort(students.stream().filter(s -> f.apply(s).equals(param)), NAME_COMPARATOR);
    }

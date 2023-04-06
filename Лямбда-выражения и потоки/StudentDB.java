import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentDB implements StudentQuery {
    // :NOTE: constant
    private static final Comparator<Student> NAME_COMPARATOR =
            Comparator.comparing(Student::getLastName)
                    .thenComparing(Student::getFirstName).reversed()
                    .thenComparing(Student::getId);

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return getCollect(students, Student::getFirstName);
    }

    // :NOTE: dupcode getLastNames, getGroups, getFullNames

    @Override
    public List<String> getLastNames(List<Student> students) {
        return streamToList(getStream(students, Student::getLastName));
    }
    @Override
    public List<GroupName> getGroups(List<Student> students) {
        return streamToList(getStream(students, Student::getGroup));
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return streamToList(getStream(students, s -> s.getFirstName() + " " + s.getLastName()));
    }

    private static ArrayList<String> getCollect(List<Student> students, Function<Student, String> func) {
        return students.stream().map(func).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return students.stream().map(Student::getFirstName)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students.stream()
                .max(Comparator.naturalOrder())
                .map(Student::getFirstName).orElse("");
    }

    // :NOTE: избавиться от кучи методов
    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return streamToList(students.stream().sorted());
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return streamToList(sort(students.stream()));
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        // :NOTE find(students, Student::getFirstName, name)
        return streamToList(sort(findStream(students, s -> s.getFirstName().equals(name))));
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return streamToList(sort(findStream(students, s -> s.getLastName().equals(name))));
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return streamToList(sort(findByGroupStream(students, group)));
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

    private <T> Stream<T> getStream(List<Student> students, Function<Student, T> mapper) {
        return students.stream().map(mapper);
    }

    private <T> List<T> streamToList(Stream<T> stream) {
        return stream.collect(Collectors.toCollection(ArrayList::new));
    }

    private Stream<Student> sort(Stream<Student> students) {
        return students.sorted(NAME_COMPARATOR);
    }

    private Stream<Student> findStream(Collection<Student> students, Predicate<Student> f) {
        return students.stream().filter(f);
    }

    private Stream<Student> findByGroupStream(Collection<Student> students, GroupName group) {
        return findStream(students, s -> s.getGroup().equals(group));
    }
}

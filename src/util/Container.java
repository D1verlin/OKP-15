package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Container<T> {
    private final List<T> items = new ArrayList<>();

    public void add(T item) {
        if (item != null) {
            items.add(item);
        }
    }

    public List<T> getAll() {
        return Collections.unmodifiableList(items);
    }

    public List<T> filter(Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public int size() {
        return items.size();
    }
}

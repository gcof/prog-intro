package base;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class Named<T> {
    private final String name;
    private final T value;

    private Named(final String name, final T value) {
        this.name = name;
        this.value = value;
    }

    public static <T> Named<T> of(final String name, final T f) {
        return new Named<>(name, f);
    }

    public String name() {
        return name;
    }

    public T value() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}

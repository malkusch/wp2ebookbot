package de.malkusch.wp2ebookbot.shared.infrastructure;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class StreamUtil {

    private StreamUtil() {

    }

    public static <T> Stream<T> optionalStream(Optional<T> opt) {
        if (opt.isPresent())
            return Stream.of(opt.get());
        else
            return Stream.empty();
    }

    public interface CheckedConsumer<T> {

        void accept(T t) throws Exception;

    }

    public static <T> Consumer<T> unchecked(CheckedConsumer<T> checked) {
        return t -> {
            try {
                checked.accept(t);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

}

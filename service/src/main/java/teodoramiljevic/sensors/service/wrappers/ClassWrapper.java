package teodoramiljevic.sensors.service.wrappers;

import java.util.Optional;

public class ClassWrapper {
    public static Optional<Class<?>> forName(final String className) {
        try {
            return Optional.of(Class.forName(className));
        } catch (final Exception ex) {
            //log

            return Optional.empty();
        }
    }
}

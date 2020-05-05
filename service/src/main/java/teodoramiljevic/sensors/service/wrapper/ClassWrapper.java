package teodoramiljevic.sensors.service.wrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ClassWrapper {

    private static final Logger logger = LogManager.getLogger(ClassWrapper.class);

    public static Optional<Class<?>> forName(final String className) {
        try {
            return Optional.of(Class.forName(className));
        } catch (final Exception ex) {
            logger.error(ex.getMessage(), ex);
            return Optional.empty();
        }
    }
}

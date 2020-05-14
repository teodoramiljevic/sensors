package teodoramiljevic.sensors.api.repository;

/**
 * Describes the common cache operations.
 */
public interface CacheRepository {

    String get(final String key);

    void set(final String key, final String value);
}

package teodoramiljevic.sensors.api.repository;

public interface CacheRepository {

    String get(final String key);

    void set(final String key, final String value);
}

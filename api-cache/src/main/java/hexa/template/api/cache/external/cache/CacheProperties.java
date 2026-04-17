package hexa.template.api.cache.external.cache;

public interface CacheProperties {
    /**
     * Get the maximum number of entries in the cache
     */
    int maximumSize();

    /**
     * Get the number of minutes before expiration after a write operation
     */
    int expireAfterWriteMinutes();

    /**
     * Get the number of minutes before refresh after a write operation
     */
    int refreshAfterWriteMinutes();
}

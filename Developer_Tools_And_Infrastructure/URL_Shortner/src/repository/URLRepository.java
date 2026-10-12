package repository;

import entities.ShortenedURL;

import java.util.Optional;

public interface URLRepository {
    void save(ShortenedURL url);
    Optional<ShortenedURL> findByKey(String key);
    Optional<String> findKeyByLongURL(String longURL);
    long getNextId();
    boolean existsByKey(String key);
}

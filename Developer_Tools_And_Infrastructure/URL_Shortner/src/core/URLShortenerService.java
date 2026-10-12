package core;

import entities.ShortenedURL;
import enums.EventType;
import observer.Observer;
import repository.URLRepository;
import strategy.KeyGenerationStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class URLShortenerService {
    private static volatile URLShortenerService INSTANCE;
    private URLRepository urlRepository;
    private KeyGenerationStrategy keyGenerationStrategy;
    private String domain;
    private static final int MAX_RETRIES = 10;
    private final List<Observer> observers = new ArrayList<>();

    private URLShortenerService() {}

    public static URLShortenerService getInstance() {
        if (INSTANCE == null) {
            synchronized (URLShortenerService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new URLShortenerService();
                }
            }
        }
        return INSTANCE;
    }

    public void configure(String domain, URLRepository repository, KeyGenerationStrategy strategy) {
        this.domain = domain;
        this.urlRepository = repository;
        this.keyGenerationStrategy = strategy;
    }

    public String shorten(String longURL) {
        Optional<String> existingKey = urlRepository.findKeyByLongURL(longURL);
        if (existingKey.isPresent()) {
            return domain + existingKey.get();
        }

        String shortKey = generateUniqueKey();

        ShortenedURL shortenedURL = new ShortenedURL.Builder(longURL, shortKey).build();
        urlRepository.save(shortenedURL);

        notifyObservers(EventType.URL_CREATED, shortenedURL);

        return domain + shortKey;
    }

    private String generateUniqueKey() {
        for (int i = 0; i < MAX_RETRIES; i++) {
            String potentialKey = keyGenerationStrategy.generateKey(urlRepository.getNextId());
            if (!urlRepository.existsByKey(potentialKey)) {
                return potentialKey;
            }
        }
        throw new RuntimeException("Failed to generate a unique short key after " + MAX_RETRIES + " attempts.");
    }

    public Optional<String> resolve(String shortURL) {
        if (!shortURL.startsWith(domain)) {
            return Optional.empty();
        }
        String shortKey = shortURL.replace(domain, "");

        Optional<ShortenedURL> found = urlRepository.findByKey(shortKey);
        if (found.isPresent()) {
            notifyObservers(EventType.URL_ACCESSED, found.get());
            return Optional.of(found.get().getLongURL());
        }

        return Optional.empty();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(EventType type, ShortenedURL url) {
        for (Observer observer : observers) {
            observer.update(type, url);
        }
    }
}

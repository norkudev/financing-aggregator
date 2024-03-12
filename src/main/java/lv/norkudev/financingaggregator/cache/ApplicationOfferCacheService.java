package lv.norkudev.financingaggregator.cache;

import lombok.RequiredArgsConstructor;
import lv.norkudev.financingaggregator.model.ApplicationOffer;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class ApplicationOfferCacheService {

    private final ReactiveRedisOperations<String, ApplicationOffer> redisTemplate;

    public Mono<List<ApplicationOffer>> getCachedOffers(UUID submitterUuid) {
        return redisTemplate.opsForList().range(submitterUuid.toString(), 0, -1)
                .collectList()
                .filter(Predicate.not(List::isEmpty));
    }

    public void cacheOffers(UUID submitterUuid, List<ApplicationOffer> offers) {
        Optional.ofNullable(offers)
                .filter(Predicate.not(List::isEmpty))
                .ifPresent(list -> redisTemplate.opsForList().leftPushAll(submitterUuid.toString(), list).subscribe());
    }

}

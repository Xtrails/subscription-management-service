package ru.aniscan.subscription.management.service.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, ru.aniscan.subscription.management.service.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, ru.aniscan.subscription.management.service.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, ru.aniscan.subscription.management.service.domain.Authority.class.getName());
            createCache(cm, ru.aniscan.subscription.management.service.domain.ExternalUser.class.getName());
            createCache(cm, ru.aniscan.subscription.management.service.domain.ClientSubscription.class.getName());
            createCache(cm, ru.aniscan.subscription.management.service.domain.SubscriptionType.class.getName());
            createCache(cm, ru.aniscan.subscription.management.service.domain.ReferralProgram.class.getName());
            createCache(cm, ru.aniscan.subscription.management.service.domain.Referral.class.getName());
            createCache(cm, ru.aniscan.subscription.management.service.domain.Payment.class.getName());
            createCache(cm, ru.aniscan.subscription.management.service.domain.PaymentSystem.class.getName());
            createCache(cm, ru.aniscan.subscription.management.service.domain.SourceApplication.class.getName());
            createCache(cm, ru.aniscan.subscription.management.service.domain.SourceApplication.class.getName() + ".referralPrograms");
            createCache(cm, ru.aniscan.subscription.management.service.domain.SourceApplication.class.getName() + ".subscriptionTypes");
            createCache(cm, ru.aniscan.subscription.management.service.domain.PaymentSystem.class.getName() + ".sourceApplications");
            createCache(cm, ru.aniscan.subscription.management.service.domain.SourceApplication.class.getName() + ".users");
            createCache(cm, ru.aniscan.subscription.management.service.domain.SourceApplication.class.getName() + ".paymentSystems");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}

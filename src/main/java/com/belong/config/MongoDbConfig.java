package com.belong.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAutoConfiguration
public class MongoDbConfig extends AbstractReactiveMongoConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.connectionTimeout}")
    private int connectionTimeout;

    @Value("${spring.data.mongodb.maxConnectionLifeTime}")
    private int maxConnectionLifeTime;

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener(final Validator validator) {
        return new ValidatingMongoEventListener(validator);
    }

    @Override
    public MongoClient reactiveMongoClient() {
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(mongoUri))
                .applyToSocketSettings(builder -> builder.connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS))
                .applyToConnectionPoolSettings(builder -> builder.maxConnectionLifeTime(maxConnectionLifeTime, TimeUnit.MILLISECONDS))
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Bean
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}

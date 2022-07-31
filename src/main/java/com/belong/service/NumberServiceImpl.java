package com.belong.service;

import com.belong.exception.DuplicateNumberException;
import com.belong.exception.NumberDbServiceException;
import com.belong.model.NumberV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class NumberServiceImpl implements NumberService {

    public static String FILE_PATH =   "src/main/resources/Numbers.csv";
    public static String TRUE = "true";

    private final ReactiveMongoOperations reactiveMongoOperations;

    @Value("${belong.collectionName}")
    private String collectionName;

    @Override
    public Mono<NumberV1> save(NumberV1 numberV1) {
        return  reactiveMongoOperations.save(numberV1, collectionName);
    }

    @Override
    public Mono<NumberV1> delete(String number) {
        Query query = new Query();
        query.addCriteria(Criteria.where(NumberV1.NUMBER).is(number));

        return reactiveMongoOperations.findAndRemove(query, NumberV1.class, collectionName);
    }

    @Override
    public Mono<NumberV1> update(String number, NumberV1 numberV1) {
        Query query = new Query()
                .addCriteria(Criteria.where(NumberV1.NUMBER).is(number));

        Update update = new Update();
        update.set(NumberV1.CUSTOMER, numberV1.getCustomer());
        update.set(NumberV1.IS_ACTIVE, numberV1.getIsActive());

        return reactiveMongoOperations.update(NumberV1.class).inCollection(collectionName)
                .matching(query)
                .apply(update)
                .withOptions(FindAndModifyOptions.options().upsert(false).returnNew(true))
                .findAndModify();
    }

    @Override
    public Flux<NumberV1> findAll() {
        return reactiveMongoOperations.findAll(NumberV1.class, collectionName);
    }

    @Override
    public Flux<NumberV1> findByCustomer(String customer) {
        Query query = new Query();
        query.addCriteria(Criteria.where(NumberV1.CUSTOMER).is(customer));

        return reactiveMongoOperations.find(query, NumberV1.class, collectionName);
    }

    @Override
    public Mono<NumberV1> activateNumber(String number) {
        Query query = new Query()
                .addCriteria(Criteria.where(NumberV1.NUMBER).is(number));

        Update update = new Update();
        update.set(NumberV1.IS_ACTIVE, TRUE);

        return reactiveMongoOperations.update(NumberV1.class).inCollection(collectionName)
                .matching(query)
                .apply(update)
                .withOptions(FindAndModifyOptions.options().upsert(false).returnNew(true))
                .findAndModify();
    }


}

package com.belong.service;

import com.belong.model.NumberV1;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NumberService {
    Mono<NumberV1> save(NumberV1 numberV1);
    Mono<NumberV1> delete(String number);
    Mono<NumberV1> update(String number, NumberV1 numberV1);
    Flux<NumberV1> findAll();
    Flux<NumberV1> findByCustomer(String customer);
    Mono<NumberV1> activateNumber(String number);
}

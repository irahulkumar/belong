package com.belong.controller;

import com.belong.exception.DuplicateNumberException;
import com.belong.model.NumberV1;
import com.belong.service.NumberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/numbers/")
@RequiredArgsConstructor
public class NumberController {
    private final NumberService numberService;

    @PostMapping({"/v1", "/v2"})
    private Mono<ResponseEntity<String>> save(@RequestBody NumberV1 numberV1) {
        return this.numberService.save(numberV1)
                .map(numberV1One -> ResponseEntity.status(HttpStatus.CREATED).body("Number Created :" + numberV1One.getNumber()))
                .onErrorResume(e -> {
                    if (e instanceof DuplicateNumberException) {
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()));
                    }
                });
    }

    @DeleteMapping("/v1/{number}")
    private Mono<ResponseEntity<String>> delete(@PathVariable("number") String number) {
        return this.numberService.delete(number)
                .flatMap(numberV1 -> Mono.just(ResponseEntity.ok("Deleted Successfully")))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Number not found : " + number)));
    }

    @PutMapping("/v1/{number}")
    private Mono<ResponseEntity<NumberV1>> update(@PathVariable("number") String number, @RequestBody NumberV1 numberV1) {
        return this.numberService.update(number, numberV1)
                .flatMap(numberV1One -> Mono.just(ResponseEntity.ok(numberV1One)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping({"/v1", "/v2"})
    private Flux<NumberV1> findAll() {
        return this.numberService.findAll();
    }

    @GetMapping(value = "/v1/{customer}")
    private Flux<NumberV1> findByCustomer(@PathVariable("customer") String customer) {
        return this.numberService.findByCustomer(customer);
    }

    @PutMapping("/v1/activate/{number}")
    private Mono<ResponseEntity<String>> activate(@PathVariable("number") String number) {
        return this.numberService.activateNumber(number)
                .flatMap(numberV1 -> Mono.just(ResponseEntity.ok("Number Activated : " + numberV1.getNumber())))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}

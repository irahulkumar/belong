package com.belong.util;

import com.belong.model.NumberV1;
import com.belong.service.NumberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    @Value("${belong.collectionName}")
    private String collectionName;
    private final ReactiveMongoOperations reactiveMongoOperations;

    public static String FILE_PATH = "src/main/resources/Numbers.csv";

    @Override
    public void run(String... args) throws Exception {

        log.info("Mongodb data initialization ...");
        loadData();

    }

    private void loadData() throws Exception {
        this.reactiveMongoOperations.remove(new Query(), NumberV1.class, collectionName)
                .thenMany(
                        Flux.fromIterable(CSVLoader.getAllNumbers(FILE_PATH))
                                .flatMap(numberV1 -> this.reactiveMongoOperations.save(numberV1, collectionName))
                )
                .log()
                .subscribe(
                        null,
                        null,
                        () -> log.info("Initialization done")
                );
    }

}

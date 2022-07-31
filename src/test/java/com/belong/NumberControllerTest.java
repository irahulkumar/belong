package com.belong;

import com.belong.model.NumberV1;
import com.belong.util.CSVLoader;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureMockMvc
@AutoConfigureDataMongo
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class NumberControllerTest {

    public static final String FILE_PATH = "src/main/resources/Numbers.csv";

    public static final String COLLECTION_NAME = "numbers_test";
    public static final String TRUE = "true";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReactiveMongoOperations reactiveMongoOperations;

    @BeforeAll
    public void init() {
        this.reactiveMongoOperations.remove(new Query(), NumberV1.class, COLLECTION_NAME);
        this.reactiveMongoOperations.indexOps(COLLECTION_NAME)
                .ensureIndex(new Index().on(NumberV1.NUMBER, Sort.Direction.ASC).unique()).block();
    }

    @BeforeEach
    public void setup() throws Exception {
        this.reactiveMongoOperations.remove(new Query(), NumberV1.class, COLLECTION_NAME)
                .thenMany(
                        Flux.fromIterable(CSVLoader.getAllNumbers(FILE_PATH))
                                .flatMap(numberV1 -> this.reactiveMongoOperations.save(numberV1, COLLECTION_NAME))
                )
                .log()
                .subscribe();
        this.reactiveMongoOperations.indexOps(COLLECTION_NAME)
                .ensureIndex(new Index().on(NumberV1.NUMBER, Sort.Direction.ASC).unique()).block();
    }

    @Test
    @Order(1)
    public void testFindAllSuccess() {
        webTestClient.get().uri("/numbers/v1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(NumberV1.class)
                .hasSize(6)
                .consumeWith(number -> {
                    List<NumberV1> numbers = number.getResponseBody();
                    numbers.forEach(numberV1 -> {
                        assertTrue(numberV1.getNumber() != null);
                    });
                });

    }

    @Test
    @Order(2)
    public void testUpdateSuccess() {
        String number = "1111111111";
        NumberV1 numberV1 = NumberV1.builder()
                .number("1111111111")
                .customer("test_user")
                .isActive(TRUE).build();
        webTestClient.put().uri("/numbers/v1".concat("/{number}"), number)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(numberV1), NumberV1.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.customer").isEqualTo(numberV1.getCustomer());
    }

    @Test
    @Order(3)
    public void testUpdateFailure() {
        String number = "1111111112";
        NumberV1 numberV1 = NumberV1.builder()
                .number("1111111112")
                .customer("test_user")
                .isActive(TRUE).build();
        webTestClient.put().uri("/numbers/v1".concat("/{number}"), number)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(numberV1), NumberV1.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(3)
    public void testFindByCustomerSuccess() {
        String customer = "abc";
        webTestClient.get().uri("/numbers/v1".concat("/{customer}"), customer).exchange()
                .expectStatus().isOk()
                .expectBodyList(NumberV1.class);
    }

    @Test
    @Order(4)
    public void testFindByCustomerFailure() {
        String customer = "abc2";
        webTestClient.get().uri("/numbers/v1".concat("/{customer}"), customer).exchange()
                .expectStatus().isOk()
                .expectBodyList(NumberV1.class)
                .hasSize(0);
    }

    @Test
    @Order(5)
    public void testActivateSuccess() {
        String number = "1111111111";
        webTestClient.put().uri("/numbers/v1/activate".concat("/{number}"), number).exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> Assertions.assertEquals(new String(response.getResponseBody()), "Number Activated : " + number));

    }

    @Test
    @Order(6)
    public void testActivateFailure() {
        String number = "1111111112";
        webTestClient.put().uri("/numbers/v1/activate".concat("/{number}"), number).exchange()
                .expectStatus().isNotFound();

    }

    @Test
    @Order(7)
    public void saveSuccess() {
        NumberV1 numberV1 = NumberV1.builder()
                .number("1111111112")
                .customer("test_user")
                .isActive(TRUE).build();
        webTestClient.post().uri("/numbers/v1")
                .body(Mono.just(numberV1), NumberV1.class).exchange()
                .expectStatus().isCreated();
    }

    @Test
    @Order(9)
    public void testDeleteSuccess() {
        String number = "1111111111";
        webTestClient.delete().uri("/numbers/v1".concat("/{number}"), number).exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    @Order(10)
    public void testDeleteFailure() {
        String number = "1111111113";
        webTestClient.delete().uri("/numbers/v1".concat("/{number}"), number).exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .consumeWith(response -> Assertions.assertEquals(new String(response.getResponseBody()), "Number not found : " + number));
    }
}

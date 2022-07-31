package com.belong.service;

import com.belong.model.NumberV1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.when;



@ExtendWith(SpringExtension.class)
public class NumberServiceTest {

    @InjectMocks
    private NumberServiceImpl numberService;

    @Mock
    private ReactiveMongoOperations reactiveMongoOperations;


    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(numberService, "reactiveMongoOperations", reactiveMongoOperations);
        ReflectionTestUtils.setField(numberService, "collectionName", "test_collection");
    }


    @Test
    public void FindByCustomerTest() {
        String customer = "abc";
        NumberV1 numberV1 = NumberV1.builder()
                .number("1122334455")
                .customer("abc")
                .isActive("true")
                .build();
        when(reactiveMongoOperations.find(any(), any(), any())).thenReturn(Flux.just(numberV1));

        Flux<NumberV1> numberV1Flux = numberService.findByCustomer(customer);
        StepVerifier.create(numberV1Flux)
                .expectNextMatches(numberV11 -> numberV1.getCustomer().equals(customer))
                .expectComplete()
                .verify();
    }

}

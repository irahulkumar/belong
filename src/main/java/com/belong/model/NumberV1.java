package com.belong.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@Getter
@Setter
@Builder
public class NumberV1 {

    public static final String NUMBER = "number";
    public static final String CUSTOMER = "customer";
    public static final String IS_ACTIVE = "isActive";
    @Indexed(unique = true)
    private String number;
    private String customer;
    private String isActive;

    public NumberV1(String number, String customer, String isActive) {
        super();
        this.number = number;
        this.customer = customer;
        this.isActive = isActive;
    }

    public NumberV1() {
        super();
    }

}

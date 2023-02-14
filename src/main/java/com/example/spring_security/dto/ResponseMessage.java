package com.example.spring_security.dto;

import lombok.Getter;

@Getter
public enum ResponseMessage {
    SUCCESS("transaction is success",0),
    ERROR_PRODUCT_NOT_FOUND("product not found", -100),
    ERROR_PRODUCT_LIST_IS_EMPTY("productList is empty", 202 ),
    ERROR_USER_NOT_FOUND("user not found", -200),
    ERROR_USER_ALREADY_EXIST("user already exist", -201),
    ERROR_USER_LIST_IS_EMPTY("userList is empty", 202 );

    private String message;
    private Integer statusCode;

    ResponseMessage(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}

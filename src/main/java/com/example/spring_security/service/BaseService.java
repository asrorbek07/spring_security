package com.example.spring_security.service;

import com.example.spring_security.dto.ApiResponse;

public interface BaseService<T, R> {
    ApiResponse add(T t);
    ApiResponse delete(R r);
    ApiResponse list();
    ApiResponse update(R r, T t);
}

package com.example.demo.presentation;

public record ApiResponse<T>(String message, T data) {
}

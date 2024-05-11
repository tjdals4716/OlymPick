package com.example.SoftwareEngineering_Project.Enum;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DeliveryStatus {
    상품접수,
    터미널입고,
    상품이동중,
    배송터미널도착,
    배송출발,
    배송완료
}
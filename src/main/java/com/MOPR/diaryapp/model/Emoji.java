package com.MOPR.diaryapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Emoji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;         // tên biểu tượng

    private String category;     // "emotion" hoặc "weather"

    private String imageUrl;     // URL ảnh
}
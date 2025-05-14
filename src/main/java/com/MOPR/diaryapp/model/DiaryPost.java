package com.MOPR.diaryapp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaryPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ElementCollection
    private List<String> imageUrls;

    private LocalDateTime timestamp;

    @ElementCollection
    private List<String> emojis;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
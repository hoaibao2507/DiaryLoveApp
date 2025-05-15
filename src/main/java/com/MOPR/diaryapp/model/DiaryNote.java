package com.MOPR.diaryapp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
public class DiaryNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 5000)
    private String content;

    private LocalDateTime timestamp;

    @ElementCollection
    @CollectionTable(name = "diary_note_emojis", joinColumns = @JoinColumn(name = "note_id"))
    @Column(name = "emoji_url")
    private List<String> emojiUrls;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
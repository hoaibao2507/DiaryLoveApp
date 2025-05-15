package com.MOPR.diaryapp.repository;

import com.MOPR.diaryapp.model.Emoji;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmojiRepository extends JpaRepository<Emoji, Long> {
    List<Emoji> findByCategory(String category);
}
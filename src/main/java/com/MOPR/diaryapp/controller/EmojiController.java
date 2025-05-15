package com.MOPR.diaryapp.controller;

import com.MOPR.diaryapp.model.Emoji;
import com.MOPR.diaryapp.repository.EmojiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emojis")
@RequiredArgsConstructor
public class EmojiController {

    private final EmojiRepository emojiRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addEmoji(@RequestBody Emoji emoji) {
        emojiRepository.save(emoji);
        return ResponseEntity.ok("Emoji saved");
    }

    @GetMapping
    public ResponseEntity<?> getAllEmojis() {
        return ResponseEntity.ok(emojiRepository.findAll());
    }

    @GetMapping("/category")
    public ResponseEntity<?> getByCategory(@RequestParam String category) {
        return ResponseEntity.ok(emojiRepository.findByCategory(category));
    }
}
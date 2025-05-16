package com.MOPR.diaryapp.controller;

import com.MOPR.diaryapp.DTO.DiaryNoteDTO;
import com.MOPR.diaryapp.model.DiaryNote;
import com.MOPR.diaryapp.model.User;
import com.MOPR.diaryapp.repository.DiaryNoteRepository;
import com.MOPR.diaryapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/diary-notes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DiaryNoteController {

    private final DiaryNoteRepository diaryNoteRepository;
    private final UserRepository userRepository;

    // Tạo hoặc cập nhật ghi chú trong ngày
    @PostMapping
    public ResponseEntity<?> createNote(@RequestBody DiaryNoteDTO dto) {
        Optional<User> userOpt = userRepository.findById(dto.getUserId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }

        User user = userOpt.get();
        LocalDateTime now = LocalDateTime.now();

        DiaryNote newNote = new DiaryNote();
        newNote.setUser(user);
        newNote.setTitle(dto.getTitle());
        newNote.setContent(dto.getContent());
        newNote.setTimestamp(now);
        newNote.setEmojiUrls(dto.getEmojiUrls());

        diaryNoteRepository.save(newNote);

        return ResponseEntity.ok(Map.of("message", "Note created"));
    }

    // Lấy ghi chú theo ngày
    @GetMapping
    public ResponseEntity<?> getNoteByDate(@RequestParam Long userId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        Optional<DiaryNote> note = diaryNoteRepository.findByUserIdAndDate(userId, localDate);
        return note.map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    // Lấy tất cả ngày đã có ghi chú
    @GetMapping("/dates")
    public ResponseEntity<?> getDatesWithNotes(@RequestParam Long userId) {
        List<DiaryNote> notes = diaryNoteRepository.findAllByUserId(userId);
        List<LocalDate> dates = notes.stream()
                .map(n -> n.getTimestamp().toLocalDate())
                .toList();
        return ResponseEntity.ok(dates);
    }

    // Lấy tất cả ghi chú của user
    @GetMapping("/all")
    public ResponseEntity<?> getAllNotesByUser(@RequestParam Long userId) {
        List<DiaryNote> notes = diaryNoteRepository.findAllByUserId(userId);
        return ResponseEntity.ok(notes);
    }

}
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
    public ResponseEntity<?> createOrUpdateNote(@RequestBody DiaryNoteDTO dto) {
        Optional<User> userOpt = userRepository.findById(dto.getUserId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOpt.get();
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        // Tìm ghi chú cũ nếu có
        Optional<DiaryNote> existingNote = diaryNoteRepository.findByUserIdAndDate(user.getId(), today);

        if (existingNote.isPresent()) {
            DiaryNote oldNote = existingNote.get();
            oldNote.setTitle(dto.getTitle());
            oldNote.setContent(dto.getContent());
            oldNote.setTimestamp(now);
            diaryNoteRepository.save(oldNote);
            return ResponseEntity.ok("Note updated");
        } else {
            DiaryNote newNote = new DiaryNote();
            newNote.setUser(user);
            newNote.setTitle(dto.getTitle());
            newNote.setContent(dto.getContent());
            newNote.setTimestamp(now);
            diaryNoteRepository.save(newNote);
            return ResponseEntity.ok("Note created");
        }
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
}
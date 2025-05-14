package com.MOPR.diaryapp.repository;

import com.MOPR.diaryapp.model.DiaryNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryNoteRepository extends JpaRepository<DiaryNote, Long> {

    @Query("SELECT d FROM DiaryNote d WHERE d.user.id = :userId AND FUNCTION('DATE', d.timestamp) = :date")
    Optional<DiaryNote> findByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    List<DiaryNote> findAllByUserId(Long userId);
}

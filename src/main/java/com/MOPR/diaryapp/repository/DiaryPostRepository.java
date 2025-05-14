package com.MOPR.diaryapp.repository;

import com.MOPR.diaryapp.model.DiaryPost;
import com.MOPR.diaryapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryPostRepository extends JpaRepository<DiaryPost, Long> {
    List<DiaryPost> findByUserIn(List<User> users);

    List<DiaryPost> findByUserId(Long userId);
}
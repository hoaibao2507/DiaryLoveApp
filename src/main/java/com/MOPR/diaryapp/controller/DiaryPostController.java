package com.MOPR.diaryapp.controller;

import com.MOPR.diaryapp.model.DiaryPost;
import com.MOPR.diaryapp.model.User;
import com.MOPR.diaryapp.repository.DiaryPostRepository;
import com.MOPR.diaryapp.repository.FriendshipRepository;
import com.MOPR.diaryapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class DiaryPostController {

    @Autowired
    private DiaryPostRepository postRepo;

    @Autowired
    private FriendshipRepository friendshipRepo;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/create")
    public DiaryPost createPost(@RequestBody DiaryPost post, @RequestParam Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        post.setUser(user);
        post.setTimestamp(LocalDateTime.now());
        return postRepo.save(post);
    }

    @GetMapping("/all")
    public List<DiaryPost> getAllPosts() {
        return postRepo.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<DiaryPost> getPostsByUser(@PathVariable Long userId) {
        return postRepo.findByUserId(userId);
    }

    @GetMapping("/friends/{userId}")
    public List<DiaryPost> getFriendsPosts(@PathVariable Long userId) {
        List<User> friends = friendshipRepo.findAcceptedFriends(userId);
        return postRepo.findByUserIn(friends);
    }
}
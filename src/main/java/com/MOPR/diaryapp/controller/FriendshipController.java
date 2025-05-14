package com.MOPR.diaryapp.controller;

import com.MOPR.diaryapp.model.Friendship;
import com.MOPR.diaryapp.model.User;
import com.MOPR.diaryapp.repository.FriendshipRepository;
import com.MOPR.diaryapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friendships")
public class FriendshipController {

    @Autowired
    private FriendshipRepository friendshipRepo;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/add")
    public String sendFriendRequest(@RequestParam Long fromUserId, @RequestParam Long toUserId) {
        User from = userRepo.findById(fromUserId).orElse(null);
        User to = userRepo.findById(toUserId).orElse(null);
        if (from == null || to == null) return "User not found";

        Friendship friendship = new Friendship(null, from, to, false);
        friendshipRepo.save(friendship);
        return "Friend request sent";
    }

    @PostMapping("/accept")
    public String acceptFriendRequest(@RequestParam Long requestId) {
        Friendship friendship = friendshipRepo.findById(requestId).orElse(null);
        if (friendship == null) return "Request not found";

        friendship.setAccepted(true);
        friendshipRepo.save(friendship);
        return "Friend request accepted";
    }
}
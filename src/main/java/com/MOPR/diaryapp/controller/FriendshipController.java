package com.MOPR.diaryapp.controller;

import com.MOPR.diaryapp.model.Friendship;
import com.MOPR.diaryapp.model.User;
import com.MOPR.diaryapp.repository.FriendshipRepository;
import com.MOPR.diaryapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/friendships")
public class FriendshipController {

    @Autowired
    private FriendshipRepository friendshipRepo;

    @Autowired
    private UserRepository userRepo;

    // Gửi lời mời kết bạn thông qua email
    @PostMapping("/send-request")
    public ResponseEntity<?> sendFriendRequest(@RequestParam Long fromUserId,
                                               @RequestParam String receiverEmail) {
        Optional<User> fromUserOpt = userRepo.findById(fromUserId);
        Optional<User> toUserOpt = userRepo.findByEmail(receiverEmail);

        if (fromUserOpt.isEmpty() || toUserOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Người gửi hoặc người nhận không tồn tại");
        }

        User from = fromUserOpt.get();
        User to = toUserOpt.get();

        // Kiểm tra nếu đã gửi yêu cầu rồi
        Optional<Friendship> existing = friendshipRepo.findByRequesterAndReceiver(from, to);
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("Bạn đã gửi lời mời kết bạn trước đó rồi");
        }

        // Kiểm tra nếu đã là bạn (đã được chấp nhận)
        Optional<Friendship> reverse = friendshipRepo.findByRequesterAndReceiver(to, from);
        if (reverse.isPresent() && reverse.get().isAccepted()) {
            return ResponseEntity.badRequest().body("Hai bạn đã là người yêu của nhau rồi 🥰");
        }

        Friendship friendship = new Friendship(null, from, to, false);
        friendshipRepo.save(friendship);
        return ResponseEntity.ok("Đã gửi lời mời kết bạn đến " + receiverEmail);
    }

    // Người nhận chấp nhận lời mời
    @PostMapping("/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestParam Long requestId) {
        Optional<Friendship> friendshipOpt = friendshipRepo.findById(requestId);
        if (friendshipOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Không tìm thấy lời mời");
        }

        Friendship friendship = friendshipOpt.get();
        friendship.setAccepted(true);
        friendshipRepo.save(friendship);
        return ResponseEntity.ok("Đã chấp nhận lời mời kết bạn");
    }

    // Người nhận từ chối lời mời (xóa lời mời)
    @DeleteMapping("/decline")
    public ResponseEntity<?> declineFriendRequest(@RequestParam Long requestId) {
        Optional<Friendship> friendshipOpt = friendshipRepo.findById(requestId);
        if (friendshipOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Không tìm thấy lời mời");
        }

        friendshipRepo.deleteById(requestId);
        return ResponseEntity.ok("Đã từ chối lời mời kết bạn");
    }

    // Lấy danh sách lời mời nhận được (dành cho user mở app lên và xem)
    @GetMapping("/requests")
    public ResponseEntity<?> getFriendRequests(@RequestParam Long userId) {
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Người dùng không tồn tại");
        }

        List<Friendship> requests = friendshipRepo.findByReceiverAndAcceptedFalse(userOpt.get());
        return ResponseEntity.ok(requests);
    }
}
package com.MOPR.diaryapp.repository;

import com.MOPR.diaryapp.model.Friendship;
import com.MOPR.diaryapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("SELECT f.receiver FROM Friendship f WHERE f.requester.id = :userId AND f.accepted = true " +
            "UNION " +
            "SELECT f.requester FROM Friendship f WHERE f.receiver.id = :userId AND f.accepted = true")
    List<User> findAcceptedFriends(Long userId);
    Optional<Friendship> findByRequesterAndReceiver(User requester, User receiver);

    List<Friendship> findByReceiverAndAcceptedFalse(User receiver);
}
package com.example.demo.repository;

import com.example.demo.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    //userIds로 받은 id에 해당하는 데이터를 blocked로 변경
    @Modifying
    @Query(
        "UPDATE User u SET u.status = 'BLOCKED' " +
            "WHERE u.id IN :userIds"
    )
    void updateStatusToBlocked(List<Long> userIds);
}

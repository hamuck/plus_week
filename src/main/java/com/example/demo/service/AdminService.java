package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO: 4. find or save 예제 개선
    //쿼리 사용으로 db접근 최소화(repository)
    @Transactional
    public void reportUsers(List<Long> userIds) {
        userRepository.updateStatusToBlocked(userIds);
    }
}

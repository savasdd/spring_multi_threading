package com.multi.thread.repostory;

import com.multi.thread.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepostory extends JpaRepository<User, Long> {
}

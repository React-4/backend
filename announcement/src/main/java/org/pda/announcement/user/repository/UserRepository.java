package org.pda.announcement.user.repository;

import org.pda.announcement.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {
    Optional<User> findByNickname(String nickname);

    Optional<Object> findByEmail(String email);
}

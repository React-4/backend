package org.pda.announcement.user.repository;

import org.pda.announcement.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {

}

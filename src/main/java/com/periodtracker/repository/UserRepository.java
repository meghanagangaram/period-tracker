
package com.periodtracker.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.periodtracker.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{
User findByEmail(String email);
}

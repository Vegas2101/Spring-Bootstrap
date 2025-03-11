package spring.springbootstrap.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.springbootstrap.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
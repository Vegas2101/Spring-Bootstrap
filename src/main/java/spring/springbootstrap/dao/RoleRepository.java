package spring.springbootstrap.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.springbootstrap.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

}
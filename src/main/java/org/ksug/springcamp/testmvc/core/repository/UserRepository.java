package org.ksug.springcamp.testmvc.core.repository;

import org.ksug.springcamp.testmvc.core.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
}

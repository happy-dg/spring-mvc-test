package org.ksug.springcamp.testmvc.core.repository;

import org.ksug.springcamp.testmvc.core.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface UserRepository extends JpaRepository<User, Long>, QueryDslPredicateExecutor<User>{
}

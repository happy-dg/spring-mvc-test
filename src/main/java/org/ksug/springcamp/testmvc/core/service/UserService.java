package org.ksug.springcamp.testmvc.core.service;

import org.ksug.springcamp.testmvc.core.domain.User;
import org.ksug.springcamp.testmvc.web.dto.UserDto;

import java.util.List;

public interface UserService {
    User add(UserDto dto);

    User update(UserDto dto);

    User delete(Long id);

    User findById(Long id);

    List<User> findAll();

}

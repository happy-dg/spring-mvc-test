package org.ksug.springcamp.testmvc.core.service;

import com.mysema.query.types.Predicate;
import org.ksug.springcamp.testmvc.core.domain.User;
import org.ksug.springcamp.testmvc.core.exception.UserNotFindException;
import org.ksug.springcamp.testmvc.core.repository.UserRepository;
import org.ksug.springcamp.testmvc.web.dto.UserDto;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    @Inject
    public DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User add(UserDto dto) {
        User.Builder builder =
                new User().getBuilder(dto.getName())
                        .age(dto.getAge())
                        .sex(dto.getSex());
        return save(builder.build());
    }

    @Override
    public User update(UserDto dto) {
        User updateUser = userRepository.findOne(dto.getId());
        updateUser.update(dto.getName(), dto.getAge(), dto.getSex());
        return save(updateUser);
    }

    @Override
    public User delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
        return user;
    }

    @Override
    public User findById(Long id) {
        User user = userRepository.findOne(id);
        if( user == null )
            throw new UserNotFindException("User Not find Exception");
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Iterable<User> findAll(Predicate predicate) {
        return userRepository.findAll(predicate);
    }

    private User save(User user) {
        return userRepository.save(user);
    }

}

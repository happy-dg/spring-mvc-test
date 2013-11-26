package org.ksug.springcamp.testmvc.core.service;

import com.google.common.collect.Lists;
import com.mysema.query.BooleanBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ksug.springcamp.testmvc.config.TestPersistenceContext;
import org.ksug.springcamp.testmvc.core.domain.QUser;
import org.ksug.springcamp.testmvc.core.domain.Sex;
import org.ksug.springcamp.testmvc.core.domain.User;
import org.ksug.springcamp.testmvc.core.exception.UserNotFindException;
import org.ksug.springcamp.testmvc.core.repository.UserRepository;
import org.ksug.springcamp.testmvc.web.dto.UserDto;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestPersistenceContext.class})
@Transactional
public class DefaultUserServiceTest {

    @Inject private UserRepository userRepository;

    private UserService userService;

    private User user;
    private UserDto dto;

    @Before
    public void setup() {
        userService = new DefaultUserService(userRepository);
        dto = newUserDto();
        user = createUser(dto);
    }

    @Test
    public void test() {
        assertCheckUserSize("이현", 1);
        assertCheckUserSize("empty", 0);
    }

    private void assertCheckUserSize(String findName, int resultSize) {
        QUser $user = QUser.user;
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and($user.name.contains(findName));

        List<User> users = Lists.newArrayList(userService.findAll(predicate));

        assertEquals(resultSize, users.size());
    }


    @Test
    public void list_userListIsNotEmpty() {
        List<User> users = userService.findAll();

        assertFalse(users.isEmpty());
    }


    @Test
    public void addUser() {

        assertNotNull(user.getId());
        assertThat(user.getName(), is(dto.getName()));
        assertThat(user.getAge(), is(dto.getAge()));
        assertThat(user.getSex(), is(dto.getSex()));
    }

    @Test
    public void updateUser() {

        UserDto updateDto = new UserDto();
        updateDto.setId(user.getId());
        updateDto.setName("문채원");
        updateDto.setAge(20);
        updateDto.setSex(Sex.FEMALE);

        User updateUser = userService.update(updateDto);

        assertThat(updateUser.getId(), is(updateDto.getId()));
        assertNotEquals(updateUser.getName(), is(dto.getName()));
        assertNotEquals(updateUser.getAge(), is(dto.getAge()));
        assertThat(updateUser.getSex(), is(dto.getSex()));

    }

    @Test
    public void deleteUser() {
        userService.delete(user.getId());

        User deleteUser = userRepository.findOne(user.getId());

        assertNull(deleteUser);
    }

    @Test(expected = UserNotFindException.class)
    public void delete_ShouldReturnUserNotFindExeption() {
        userService.delete(999l);
    }

    private User createUser(UserDto dto) {
        return userService.add(dto);
    }

    private UserDto newUserDto() {
        UserDto dto = new UserDto();
        dto.setName("이현아");
        dto.setAge(29);
        dto.setSex(Sex.FEMALE);
        return dto;
    }
}

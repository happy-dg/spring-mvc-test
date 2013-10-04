package org.ksug.springcamp.testmvc.core.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class UserBuilder {
    private User user;

    public UserBuilder() {
        user = new User();
    }

    public UserBuilder id(Long id) {
        ReflectionTestUtils.setField(user, "id", id);
        return this;
    }

    public UserBuilder name(String name) {
        user.update(name,user.getAge(), user.getSex());
        return this;
    }

    public UserBuilder age(Integer age) {
        user.update(user.getName(),age, user.getSex());
        return this;
    }

    public UserBuilder sex(Sex sex) {
        user.update(user.getName(),user.getAge(), sex);
        return this;
    }

    public User build() {
        return user;
    }
}

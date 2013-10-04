package org.ksug.springcamp.testmvc.core.dto;

import org.ksug.springcamp.testmvc.core.domain.Sex;
import org.ksug.springcamp.testmvc.web.dto.UserDto;

public class UserDtoBuilder {
    private UserDto dto;

    public UserDtoBuilder() {
        this.dto = new UserDto();
    }

    public UserDtoBuilder id(Long id) {
        dto.setId(id);
        return this;
    }
    public UserDtoBuilder name(String name) {
        dto.setName(name);
        return this;
    }
    public UserDtoBuilder age(Integer age) {
        dto.setAge(age);
        return this;
    }
    public UserDtoBuilder sex(Sex sex) {
        dto.setSex(sex);
        return this;
    }

    public UserDto build() {
        return dto;
    }
}

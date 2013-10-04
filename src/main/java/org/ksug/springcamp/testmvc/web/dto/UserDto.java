package org.ksug.springcamp.testmvc.web.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.ksug.springcamp.testmvc.core.domain.Sex;
import org.ksug.springcamp.testmvc.core.domain.User;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UserDto {

    private Long id;

    @NotEmpty
    private String name;

    @NotNull @Min(20) @Max(99)
    private Integer age;

    @NotNull
    private Sex sex;

    public UserDto() {}
    public UserDto(User user) {
        id = user.getId();
        name = user.getName();
        age = user.getAge();
        sex = user.getSex();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

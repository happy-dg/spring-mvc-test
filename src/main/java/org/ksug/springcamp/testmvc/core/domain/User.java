package org.ksug.springcamp.testmvc.core.domain;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Sex getSex() {
        return sex;
    }

    public void update(String name, Integer age, Sex sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public Builder getBuilder(String name) {
        return new Builder(name);
    }

    public static class Builder {

        private User user;

        private Long id;

        private String name;

        private Integer age;

        private Sex sex;

        public Builder(String name) {
            this.name = name;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder age(Integer age) {
            this.age = age;
            return this;
        }

        public Builder sex(Sex sex) {
            this.sex = sex;
            return this;
        }

        public User build() {
            user = new User();
            if( id != null )
                user.id = id;

            user.name = name;
            user.age = age;
            user.sex = sex;
            return user;
        }
    }
}

package org.ksug.springcamp.testmvc.core.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ksug.springcamp.testmvc.config.TestPersistenceContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestPersistenceContext.class})
public class UserRepositoryTest {

    @Inject private UserRepository userRepository;

    @Test
    public void nothing() {
        assertThat(0l, is(userRepository.count()));
    }
}

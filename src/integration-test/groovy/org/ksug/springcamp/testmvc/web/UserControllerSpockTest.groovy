package org.ksug.springcamp.testmvc.web

import org.ksug.springcamp.testmvc.config.TestContext
import org.ksug.springcamp.testmvc.core.service.UserService
import org.springframework.context.MessageSource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import javax.inject.Inject

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

@ContextConfiguration(classes = [TestContext.class])
class UserControllerSpockTest extends Specification{

    MockMvc mockMvc

    @Inject private MessageSource messageSource;
    @Inject private UserService userService;


    def setup () {
        mockMvc = standaloneSetup(new UserController(userService, messageSource)).build()
    }


    def "find all should add users to model and render user list view" () {
//        given : "given"
//            def lee = new UserBuilder().id(1l).name("이남희").age(36).sex(Sex.MALE).build();
//            def shin = new UserBuilder().id(2l).name("신재근").age(34).sex(Sex.MALE).build();
//            def kim = new UserBuilder().id(3l).name("김용훈").age(34).sex(Sex.MALE).build();
//            def son = new UserBuilder().id(4l).name("손지성").age(30).sex(Sex.MALE).build();
//
//        when : "when"
//            userService.findAll() << Lists.newArrayList(lee,shin,kim,son);
//            def response = mockMvc.perform(get("/user"));
//
//        then : "then"
//            response.andExpect(status().isOk())
//                    .andExpect(view().name(UserController.VIEW_USER_LIST))
//                    .andExpect(forwardedUrl("/WEB-INF/views/user/list.jsp"))
//                    .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_LIST, hasSize(4)))
//                    .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_LIST, hasItem(
//                    allOf(
//                            hasProperty("id", is(1l)),
//                            hasProperty("name", is("이남희")),
//                            hasProperty("sex", is(Sex.MALE))
//                    )
//            )));




    }



}

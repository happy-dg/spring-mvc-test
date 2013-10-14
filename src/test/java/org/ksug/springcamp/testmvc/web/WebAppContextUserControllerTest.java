package org.ksug.springcamp.testmvc.web;


import com.google.common.collect.Lists;
import groovy.util.Eval;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ksug.springcamp.testmvc.TestUtil;
import org.ksug.springcamp.testmvc.config.TestContext;
import org.ksug.springcamp.testmvc.config.WebAppContext;
import org.ksug.springcamp.testmvc.core.domain.Sex;
import org.ksug.springcamp.testmvc.core.domain.User;
import org.ksug.springcamp.testmvc.core.domain.UserBuilder;
import org.ksug.springcamp.testmvc.core.dto.UserDtoBuilder;
import org.ksug.springcamp.testmvc.core.service.UserService;
import org.ksug.springcamp.testmvc.web.dto.UserDto;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebAppContext.class})
@WebAppConfiguration
public class WebAppContextUserControllerTest {

    MockMvc mockMvc;

    @Inject private MessageSource messageSource;
    @Inject private UserService userService;

    @Inject private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void redirect_indexAccess_ShouldRedirectUserList() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(redirectedUrl("/user"));
    }

    @Test
    public void findAll_ShouldAddUsersToModelAndRenderUserListView() throws Exception {

        User lee = new UserBuilder().id(1l).name("이남희").age(36).sex(Sex.MALE).build();
        User shin = new UserBuilder().id(2l).name("신재근").age(34).sex(Sex.MALE).build();
        User kim = new UserBuilder().id(3l).name("김용훈").age(34).sex(Sex.MALE).build();
        User son = new UserBuilder().id(4l).name("손지성").age(30).sex(Sex.MALE).build();

        when(userService.findAll()).thenReturn(Lists.<User>newArrayList(lee,shin,kim,son));

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_USER_LIST))
                .andExpect(forwardedUrl("/WEB-INF/views/user/list.jsp"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_LIST, hasSize(4)))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_LIST, hasItem(
                allOf(
                        hasProperty("id", is(1l)),
                        hasProperty("name", is("이남희")),
                        hasProperty("sex", is(Sex.MALE))
                )
                )));

        verify(userService, times(1)).findAll();
        verifyNoMoreInteractions(userService);

    }

    @Test
    public void rest_FindAll_ShouldAddUsersToJsonList() throws Exception {

        User lee = new UserBuilder().id(1l).name("박용권").age(34).sex(Sex.MALE).build();
        User shin = new UserBuilder().id(2l).name("김효영").age(34).sex(Sex.FEMALE).build();
        User kim = new UserBuilder().id(3l).name("김지헌").age(33).sex(Sex.MALE).build();
        User son = new UserBuilder().id(4l).name("장원호").age(28).sex(Sex.MALE).build();

        when(userService.findAll()).thenReturn(Lists.<User>newArrayList(lee,shin,kim,son));

        mockMvc.perform(get("/user/api"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("박용권")))
                .andExpect(jsonPath("$[0].age", is(34)))
                .andExpect(jsonPath("$[0].sex", is("MALE")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("김효영")))
                .andExpect(jsonPath("$[1].age", is(34)))
                .andExpect(jsonPath("$[1].sex", is("FEMALE")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].name", is("김지헌")))
                .andExpect(jsonPath("$[2].age", is(33)))
                .andExpect(jsonPath("$[2].sex", is("MALE")))
                .andExpect(jsonPath("$[3].id", is(4)))
                .andExpect(jsonPath("$[3].name", is("장원호")))
                .andExpect(jsonPath("$[3].age", is(28)))
                .andExpect(jsonPath("$[3].sex", is("MALE")));

        verify(userService, times(1)).findAll();
        verifyZeroInteractions(userService);
    }


    @Test
    public void add_EmptyUser_ShouldRenderformViewAndReturnValidationErrorForName() throws Exception {
        UserDto formUser = new UserDto();

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(formUser))
                .sessionAttr(UserController.MODEL_ATTRIBUTE_USER_FORM, formUser)
        )
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/user/form.jsp"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE_USER_FORM, "name"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("id", nullValue())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("age", isEmptyOrNullString())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("sex", isEmptyOrNullString())));

        verifyZeroInteractions(userService);
    }

    @Test
    public void add_NewUser_ShouldAddUserAndRenderViewUserView() throws Exception {
        UserDto formUser = new UserDtoBuilder().name("이현아").age(22).sex(Sex.FEMALE).build();
        User addedUser = new UserBuilder().id(1l)
                .name(formUser.getName())
                .age(formUser.getAge())
                .sex(formUser.getSex())
                .build();

        when(userService.add(formUser)).thenReturn(addedUser);

        String expectedRedirectViewPath = "redirect:/user";
        String message = getMessage("user.add", new Object[]{formUser.getName()});

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(formUser))
                .sessionAttr(UserController.MODEL_ATTRIBUTE_USER_FORM, formUser)
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(redirectedUrl("/user"))
                .andExpect(flash().attribute(UserController.FLASH_MESSAGE_KEY_FEEDBACK, is(message)));

        verify(userService, times(1)).add(formUser);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void showUpdateUserForm_UserFound_ShouldCreateUserFormAndRenderUpdateUserView() throws Exception {
        User tempUser = new UserBuilder().id(5l).name("이현아").age(22).sex(Sex.FEMALE).build();

        when(userService.findById(5l)).thenReturn(tempUser);

        mockMvc.perform(get("/user/update/{id}", 5l))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/user/form.jsp"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM,
                        hasProperty("name", is(tempUser.getName()))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM,
                        hasProperty("age", is(tempUser.getAge()))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM,
                        hasProperty("sex", is(tempUser.getSex()))));

        verify(userService, times(1)).findById(5l);
        verifyZeroInteractions(userService);
    }


    @Test
    public void update_EmptyUser_ShouldRenderFormViewAndReturnValidationErrorForName() throws Exception {
        UserDto formUser = new UserDto();
        formUser.setName("김효영");

        mockMvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(formUser))
                .sessionAttr(UserController.MODEL_ATTRIBUTE_USER_FORM, formUser)
        )
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/user/form.jsp"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("name", is(formUser.getName()))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("age", is(formUser.getAge()))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("sex", is(formUser.getSex()))));

        verifyZeroInteractions(userService);
    }

    @Test
    public void update_UserFound_ShouldUpdateUserAndRenderViewUserView() throws Exception {
        UserDto formUser = new UserDtoBuilder().id(5l).name("이현아").age(29).sex(Sex.FEMALE).build();
        User user = new UserBuilder().id(formUser.getId()).name(formUser.getName()).age(formUser.getAge()).sex(formUser.getSex()).build();

        when(userService.findById(5l)).thenReturn(user);
        when(userService.update(formUser)).thenReturn(user);

        String expectedRedirectViewPath = TestUtil.createRedirectViewPath("/user");
        mockMvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(formUser))
                .sessionAttr(UserController.MODEL_ATTRIBUTE_USER_FORM, formUser)
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute(UserController.FLASH_MESSAGE_KEY_FEEDBACK,
                        is(getMessage("user.update", new Object[]{formUser.getName()}))));
    }

    @Test
    public void delete_UserFound_ShouldDeleteUserAndRenderUserListView() throws Exception {
        User user = new UserBuilder().id(1l).name("문채원").age(27).sex(Sex.FEMALE).build();

        when(userService.delete(1l)).thenReturn(user);

        mockMvc.perform(delete("/user/{id}", 1l))
                .andExpect(status().isMovedTemporarily())
                .andExpect(view().name(TestUtil.createRedirectViewPath("/user")))
                .andExpect(flash().attribute(UserController.FLASH_MESSAGE_KEY_FEEDBACK,
                        is(getMessage("user.delete", new Object[]{user.getName()}))));

        verify(userService, times(1)).delete(1l);
        verifyZeroInteractions(userService);

    }


    @Test
    public void pageNotFound() throws Exception {
         mockMvc.perform(get("/im-so-happy"))
                .andExpect(forwardedUrl("default"));
    }

    private String getMessage(String messageCode, Object[] objects) {
        return messageSource.getMessage(messageCode, objects, LocaleContextHolder.getLocale());
    }
}

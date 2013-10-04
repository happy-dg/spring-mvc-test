package org.ksug.springcamp.testmvc.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ksug.springcamp.testmvc.TestUtil;
import org.ksug.springcamp.testmvc.config.AppContext;
import org.ksug.springcamp.testmvc.core.domain.Sex;
import org.ksug.springcamp.testmvc.core.dto.UserDtoBuilder;
import org.ksug.springcamp.testmvc.web.dto.UserDto;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppContext.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@WebAppConfiguration
@DatabaseSetup("userData.xml")
public class ITUserControllerTest {

    @Inject
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }


    @Test
    @ExpectedDatabase("userData.xml")
    public void findAll_ShouldAddUsersToModelAndRenderUserListView() throws Exception {

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_USER_LIST))
                .andExpect(forwardedUrl("/WEB-INF/views/user/list.jsp"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_LIST, hasSize(5)))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(1l)),
                                hasProperty("name", is("이남희")),
                                hasProperty("sex", is(Sex.MALE))
                        )
                )));
    }


    @Test
    @ExpectedDatabase("userData.xml")
    public void showAddUserForm_ShouldCreateUserFormAndRederAddUserForm() throws Exception {
        mockMvc.perform(get("/user/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_USER_FORM))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("id", nullValue())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("name", isEmptyOrNullString())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("age", nullValue())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("sex", isEmptyOrNullString())));
    }


    @Test
    @ExpectedDatabase("userData.xml")
    public void add_EmptyUser_ShouldRenderformViewAndReturnValidationErrorForFields() throws Exception {
        UserDto formUser = new UserDto();

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(formUser))
                .sessionAttr(UserController.MODEL_ATTRIBUTE_USER_FORM, formUser)
        )
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/user/form.jsp"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE_USER_FORM, "name"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE_USER_FORM, "age"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE_USER_FORM, "sex"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("id", nullValue())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("age", isEmptyOrNullString())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("sex", isEmptyOrNullString())));

    }


    @Test
    @ExpectedDatabase("userData-add.xml")
    public void add_NewUser_ShouldAddUserAndRenderViewUserView() throws Exception {
        UserDto formUser = new UserDtoBuilder().name("이현아").age(22).sex(Sex.FEMALE).build();
        String expectedRedirectViewPath = TestUtil.createRedirectViewPath("/user");
        String message = String.format("[%s] 사용자가 등록 되었습니다.", formUser.getName());

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(formUser))
                .sessionAttr(UserController.MODEL_ATTRIBUTE_USER_FORM, formUser)
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(redirectedUrl("/user"))
                .andExpect(flash().attribute(UserController.FLASH_MESSAGE_KEY_FEEDBACK, is(message)));
    }

    @Test
    @ExpectedDatabase("userData.xml")
    public void showUpdateUserForm_UserFound_ShouldCreateUserFormAndRenderUpdateUserView() throws Exception {
        mockMvc.perform(get("/user/update/{id}", 2l))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/user/form.jsp"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("name", is("신재근"))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("age", is(34))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("sex", is(Sex.MALE))));

    }

    @Test
    @ExpectedDatabase("userData.xml")
    public void update_EmptyUser_ShouldRenderFormViewAndReturnValidationErrorForAgeAndSex() throws Exception {
        UserDto formUser = new UserDto();
        formUser.setName("김효영");

        mockMvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(formUser))
                .sessionAttr(UserController.MODEL_ATTRIBUTE_USER_FORM, formUser)
        )
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/user/form.jsp"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE_USER_FORM, "age"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE_USER_FORM, "sex"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("name", is(formUser.getName()))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("age", is(formUser.getAge()))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_USER_FORM, hasProperty("sex", is(formUser.getSex()))));

    }


    @Test
    @ExpectedDatabase("userData-update.xml")
    public void update_UserFound_ShouldUpdateUserAndRenderViewUserView() throws Exception {
        UserDto formUser = new UserDtoBuilder().id(5l).name("장원호").age(28).sex(Sex.MALE).build();

        String expectedRedirectViewPath = TestUtil.createRedirectViewPath("/user");
        String message = "[장원호] 사용자의 정보가 수정 되었습니다.";

        mockMvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(formUser))
                .sessionAttr(UserController.MODEL_ATTRIBUTE_USER_FORM, formUser)
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute(UserController.FLASH_MESSAGE_KEY_FEEDBACK, is(message)));
    }

    @Test
    @ExpectedDatabase("userData-delete.xml")
    public void delete_UserFound_ShouldDeleteUserAndRenderUserListView() throws Exception {

        String message = "[문채원] 사용자가 삭제 되었습니다.";

        mockMvc.perform(delete("/user/{id}", 5l))
                .andExpect(status().isMovedTemporarily())
                .andExpect(view().name(TestUtil.createRedirectViewPath("/user")))
                .andExpect(flash().attribute(UserController.FLASH_MESSAGE_KEY_FEEDBACK, is(message)));
    }

}

package org.ksug.springcamp.testmvc.web;

import com.google.common.collect.Lists;
import org.ksug.springcamp.testmvc.core.domain.User;
import org.ksug.springcamp.testmvc.core.service.UserService;
import org.ksug.springcamp.testmvc.web.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/user")
@SessionAttributes({"userForm"})
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public static final String VIEW_USER_LIST = "user/list";
    public static final String VIEW_USER_FORM = "user/form";

    public static final String MODEL_ATTRIBUTE_USER_FORM = "userForm";
    public static final String MODEL_ATTRIBUTE_USER_LIST = "users";

    public static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";
    public static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";

    public static final String PARAMETER_USER_ID = "id";


    private final UserService userService;
    private final MessageSource messageSource;

    @Inject
    public UserController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @ModelAttribute(MODEL_ATTRIBUTE_USER_FORM)
    public UserDto createUserDto() {
        return new UserDto();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        log.debug("사용자 리스트 반환");
        model.addAttribute(MODEL_ATTRIBUTE_USER_LIST,userService.findAll());
        return VIEW_USER_LIST;
    }


    @RequestMapping(value = "api", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<UserDto> apiList() {
        List<UserDto> userDtos = Lists.newArrayList();
        for( User user : userService.findAll() )
            userDtos.add(new UserDto(user));

        return userDtos;
    }

    @RequestMapping(value="new", method = RequestMethod.GET)
    public String viewAddForm(Model model) {
        log.debug("사용자 등록 화면 반환");
        model.addAttribute(MODEL_ATTRIBUTE_USER_FORM, new UserDto());
        return VIEW_USER_FORM;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(
            @ModelAttribute(MODEL_ATTRIBUTE_USER_FORM) @Valid UserDto form,
            BindingResult result,
            SessionStatus status,
            RedirectAttributes attributes
    ) {
        log.debug("사용자 등록 : {}", form);
        if( result.hasErrors() ) {
            log.debug("사용자 등록 데이터가 유효하지 않음 : {}", result.getFieldErrors());
            return VIEW_USER_FORM;
        }

        User addedUser = userService.add(form);
        addFeedbackMessage(attributes, "user.add", addedUser.getName());
        status.setComplete();

        return createRedirectViewPath("/user");
    }

    @RequestMapping(value="update/{id}", method = RequestMethod.GET)
    public String viewUpdateForm(@PathVariable Long id, Model model) {

        User updateUser = userService.findById(id);
        model.addAttribute(MODEL_ATTRIBUTE_USER_FORM, new UserDto(updateUser));
        return VIEW_USER_FORM;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(
            @ModelAttribute(MODEL_ATTRIBUTE_USER_FORM) @Valid UserDto form,
            BindingResult result,
            SessionStatus status,
            RedirectAttributes attributes
    ) {

        if( result.hasErrors() )
            return VIEW_USER_FORM;

        User updatedUser = userService.update(form);
        addFeedbackMessage(attributes, "user.update", updatedUser.getName());
        status.setComplete();
        return createRedirectViewPath("/user");
    }

    @RequestMapping(value="{id}", method=RequestMethod.DELETE)
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        User deletedUser = userService.delete(id);

        addFeedbackMessage(attributes, "user.delete", deletedUser.getName());
        return createRedirectViewPath("/user");
    }

    private void addFeedbackMessage(RedirectAttributes attributes, String messageCode, Object... messageParameters) {
        log.debug("Adding feedback message with code: {} and params: {}", messageCode, messageParameters);
        String localizedFeedbackMessage = getMessage(messageCode, messageParameters);
        log.debug("Localized message is: {}", localizedFeedbackMessage);
        attributes.addFlashAttribute(FLASH_MESSAGE_KEY_FEEDBACK, localizedFeedbackMessage);
    }

    private String getMessage(String messageCode, Object... messageParameters) {
        Locale current = LocaleContextHolder.getLocale();
        log.debug("Current locale is {}", current);
        return messageSource.getMessage(messageCode, messageParameters, current);
    }

    private String createRedirectViewPath(String requestMapping) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(requestMapping);
        return redirectViewPath.toString();
    }
}

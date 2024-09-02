package com.spring.jwt.validator.controller;

import com.google.gson.Gson;
import com.spring.jwt.validator.exception.configuration.DelegatedAuthenticationEntryPoint;
import com.spring.jwt.validator.fixture.ObjectsFixture;
import com.spring.jwt.validator.model.DTO.LoginUserDto;
import com.spring.jwt.validator.service.AuthenticationService;
import com.spring.jwt.validator.service.JwtService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.spring.jwt.validator.fixture.ObjectsFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false) // TODO: turn it true to test securityFilterChain
class JwtValidadorControllerTest {


    @MockBean
    private AuthenticationService authenticationService;


    @MockBean
    private DelegatedAuthenticationEntryPoint delegatedAuthenticationEntryPoint;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private JwtValidadorController userController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;


    @Test
    public void shouldAuthenticateUser_Success() throws Exception {
        // Arrange

        var loginUserFixture = loginUserFixture();
        var loginResponseFixture = ObjectsFixture.loginResponseFixture();
        var userDtoFixture = userDtoFixture();


        when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(userDtoFixture);
        when(jwtService.generatedAuthenticatedUser(userDtoFixture)).thenReturn(loginResponseFixture);


        mockMvc.perform(MockMvcRequestBuilders.post("/v1/base/login")
                        .content(gson.toJson(loginUserFixture))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE));

        // Assert
        verify(authenticationService).authenticate(any(LoginUserDto.class));
        verify(jwtService).generatedAuthenticatedUser(userDtoFixture);

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should throw an exception if login email is invalid")
    public void shouldValidateAuthenticateUser() throws Exception {
        // Arrange

        var registerUserFixture = registerUserDtoFixture();
        registerUserFixture.setEmail("wrongEmailWithoutATSign.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/base/login")
                        .content(gson.toJson(registerUserFixture))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is("email not valid")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE));

    }


   //@Test //Todo: verify NP exception on log
    public void registerUser_Success() throws Exception {
        // Arrange

        var registerUserFixture = registerUserDtoFixture();
        var signupResponseFixture = signupResponseFixture();


        when(authenticationService.signup(registerUserFixture)).thenReturn(signupResponseFixture);


        mockMvc.perform(MockMvcRequestBuilders.post("/v1/base/signup")
                        .content(gson.toJson(registerUserFixture))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE));


        // Assert

        verify(authenticationService).signup(registerUserFixture);
    }

    @Test
    @DisplayName("Should throw an exception if an email is invalid")
    public void shouldValidateEmail() throws Exception {
        // Arrange

        var registerUserFixture = registerUserDtoFixture();

        registerUserFixture.setEmail("wrongEmailWithoutATSign.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/base/signup")
                        .content(gson.toJson(registerUserFixture))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is("email not valid")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE));

    }

    @Test
    @DisplayName("Should throw an exception if name size is smaller than 3 characters")
    public void shouldValidateName() throws Exception {
        // Arrange

        var registerUserFixture = registerUserDtoFixture();
        var signupResponseFixture = signupResponseFixture();

        registerUserFixture.setFullName("ja");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/base/signup")
                        .content(gson.toJson(registerUserFixture))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName", Is.is("name cannot be bigger than 256 characters and lower than 3 characters")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("Should throw an exception if name size is smaller than 256 characters")
    public void shouldValidateName_2() throws Exception {
        // Arrange

        var registerUserFixture = registerUserDtoFixture();

        registerUserFixture.setFullName("Lorem ipsum dolor sit amet " +
                "consectetuer adipiscing elit Aenean commodo ligula eget dolor" +
                " Aenean massa Cum sociis natoque penatibus et magnis dis parturient montes" +
                " nascetur ridiculus mus Donec quam felis ultricies necpellentesque eu" +
                " pretium quis sem Nulla consequat massa quis enim Donec pede justo" +
                " fringilla velaliquet nec vulputate eget arcu In enim justo" +
                " rhoncus ut imperdiet a venenatis vitae justo Nullam dictum felis eu pede mollis pretium Integer tincidunt Cras dapibus Vivamus elementum semper nisi Aenean vulputate eleifend tellus Aenean leo ligula" +
                " porttitor eu consequat vitae eleifend ac enim Aliquam lorem ante dapibus in viverra quis feugiat a tellus Phasellus viverra nulla ut metus varius laoreet Quisque rutrum Aenean imperdiet Etiam ultricies " +
                "nisi vel augue Curabitur ullamcorper ultricies nisi Nam eget dui Etiam rhoncus Maecenas tempus tellus eget condimentum rhoncus sem quam semper libero sit amet adipiscing sem neque sed ipsum Nam quam nunc blandit vel" +
                " luctus pulvinar hendrerit id lorem Maecenas nec odio et ante tincidunt tempus Donec vitae sapien ut libero venenatis faucibus Nullam quis ante Etiam sit amet orci eget eros faucibus tincidunt Duis leo Sed fringilla mauris sit" +
                " amet nibh Donec sodales sagittis magna Sed consequat leo eget bibendum sodales augue velit cursus nunc quis gravida magna mi a libero Fusce vulputate eleifend sapien Vestibulum purus quam scelerisque ut mollis sed nonummy id metus" +
                " Nullam accumsan lorem in dui Cras ultricies mi eu turpis hendrerit fringilla Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae In ac dui quis mi consectetuer lacinia Nam pretium turpis et");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/base/signup")
                        .content(gson.toJson(registerUserFixture))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("Should throw an exception if name contains numbers or special characters")
    public void shouldValidateName_3() throws Exception {
        // Arrange

        var registerUserFixture = registerUserDtoFixture();

        registerUserFixture.setFullName("M4ria");
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/base/signup")
                        .content(gson.toJson(registerUserFixture))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName", Is.is("name only can have alphabetic letters")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE));
    }


}
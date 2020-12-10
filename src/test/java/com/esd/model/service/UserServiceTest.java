package com.esd.controller.authentication;

import com.esd.model.dao.UserDao;
import com.esd.model.data.persisted.User;
import com.esd.model.data.UserGroup;
import com.esd.model.exceptions.InvalidUserCredentialsException;
import com.esd.model.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    public static final int ID = -1;
    public static final String USERNAME = "user";
    public static final String PASSWORD = "pass";
    public static final Boolean ACTIVE = true;
    public static final UserGroup USER_GROUP = UserGroup.ADMIN;

    private UserDao mockUserDao = mock(UserDao.class);
    private UserService target = UserService.getTestInstance(mockUserDao);
    private User user;

    @Before
    public void setup() throws SQLException, InvalidUserCredentialsException {
        user = new User(ID, USERNAME, PASSWORD, USER_GROUP, ACTIVE);
        when(mockUserDao.getUserByUsername(USERNAME)).thenReturn(user);
    }

    @Test
    public void testValidCredentials() throws SQLException, InvalidUserCredentialsException {
        assertThat(target.validateCredentials(USERNAME, PASSWORD))
                .as("User should be returned by service if user is found and passwords match")
                .isEqualTo(user);
    }

    @Test
    public void testInvalidCredentialsPasswordsDontMatch()  {
        assertThatThrownBy(() -> target.validateCredentials(USERNAME, "invalid"))
                .as("InvalidUserCredentialsException should be thrown when passwords do not match for user")
                .isInstanceOf(InvalidUserCredentialsException.class)
                .hasMessage("Passwords don't match");
    }
}

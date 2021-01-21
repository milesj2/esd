package com.esd.model.dao;


import com.esd.model.data.persisted.SystemUser;
import com.esd.model.data.UserGroup;
import com.esd.model.exceptions.InvalidUserCredentialsException;
import org.junit.Test;


import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SystemSystemUserDaoTest {

    //TODO this test class needs updating to run in an in memory derbydb as opposed to production db,
    //this will require changes to the ConnectionManager function in some form.

    private static final String VALID_USERNAME = "testusername";
    private static final String VALID_PASSWORD = "testpass";
    private static final UserGroup VALID_USERGROUP = UserGroup.NHS_PATIENT;
    private static final String INVALID_USERNAME = "sdfsdf";

    private SystemUserDao target = SystemUserDao.getInstance();

    @Test
    public void validUsernameTest() throws SQLException, InvalidUserCredentialsException {
        assertThat(target.getUserByUsername(VALID_USERNAME))
                .as("User for valid username should be returned")
        .isInstanceOf(SystemUser.class)
        .hasFieldOrPropertyWithValue("username", VALID_USERNAME)
        .hasFieldOrPropertyWithValue("userGroup", VALID_USERGROUP)
        .hasFieldOrPropertyWithValue("password", VALID_PASSWORD);
    }

    @Test
    public void invalidUsernameTest() {
        assertThatThrownBy(() -> target.getUserByUsername(INVALID_USERNAME))
                .as("InvalidUserCredentialsException should be thrown when passwords do not match for user")
                .isInstanceOf(InvalidUserCredentialsException.class)
                .hasMessage("no user found for username");
    }
}

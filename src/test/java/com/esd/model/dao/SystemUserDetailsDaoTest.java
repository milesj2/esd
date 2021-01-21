package com.esd.model.dao;

import com.esd.model.data.UserGroup;
import org.junit.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class SystemUserDetailsDaoTest {

    private UserDetailsDao target = UserDetailsDao.getInstance();

    @Test
    public void testValidateUserDetailsExistByIdAndUserGroup() throws SQLException {
        boolean result1 = target.validateUserDetailsExistByIdAndUserGroup(1, UserGroup.ADMIN);
        boolean result2 = target.validateUserDetailsExistByIdAndUserGroup(3, UserGroup.DOCTOR, UserGroup.NURSE);
        boolean result3 = target.validateUserDetailsExistByIdAndUserGroup(3, UserGroup.DOCTOR);

        assertThat(result1).as("User details id 1 should be an admin").isTrue();
        assertThat(result2).as("User details id 3 should be an doctor").isTrue();
        assertThat(result3).as("User details id 3 should be an doctor").isTrue();
    }
}

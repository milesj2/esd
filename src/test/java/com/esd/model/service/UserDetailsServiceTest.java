package com.esd.model.service;

import com.esd.model.dao.UserDetailsDao;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidUserIDException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceTest {
    public static final int ID = 1;
    public static final int USERID = 1;
    public static final String FIRSTNAME = "FIRST";
    public static final String LASTNAME = "NAME";
    public static final String ADDRESS_LINE_1 = "Flat 1";
    public static final String ADDRESS_LINE_2 = "Street Street";
    public static final String ADDRESS_LINE_3 = "District";
    public static final String TOWN = "Bristol";
    public static final String POSTCODE = "BS1 0AA";
    public static final String DOB = "01/01/2000";


    private UserDetailsDao mockUserDetailsDao = mock(UserDetailsDao.class);
    private UserDetailsService target = UserDetailsService.getTestInstance(mockUserDetailsDao);
    private UserDetails userDetails;

    @Before
    public void setup() throws SQLException, InvalidUserIDException {
        userDetails = new UserDetails(ID, USERID, FIRSTNAME, LASTNAME, ADDRESS_LINE_1, ADDRESS_LINE_2, ADDRESS_LINE_3, TOWN, POSTCODE, DOB);
        when(mockUserDetailsDao.getUserDetailsByUserId(USERID)).thenReturn(userDetails);
    }
}

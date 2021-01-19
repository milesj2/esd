package com.esd.model.service;

import com.esd.model.dao.ThirdPartyDao;
import com.esd.model.data.persisted.ThirdParty;
import com.esd.model.exceptions.InvalidIdValueException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Original Author: Angela Jackson
 * Use: This class is a singleton, used to access third party data objs
 */
public class ThirdPartyService {
    
    private static ThirdPartyService instance;
    private ThirdPartyDao thirdPartyDao = ThirdPartyDao.getInstance();
    
    private ThirdPartyService() {
    }
    
    public List<ThirdParty> getThirdParties()  {
        try {
            return thirdPartyDao.getThirdParties();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    public boolean createThirdParty(ThirdParty thirdParty) throws SQLException, InvalidIdValueException {
       thirdPartyDao.getInstance().createThirdParty(thirdParty);
        return true;
    }
    
    public ThirdParty getThirdPartyByID(int id) throws SQLException, InvalidIdValueException {
        return thirdPartyDao.getThirdPartyByID(id);
    }
    
    public boolean updateThirdParty(ThirdParty thirdParty) throws SQLException, InvalidIdValueException {
        return thirdPartyDao.updateThirdParty(thirdParty);
    }
    
    public synchronized static ThirdPartyService getInstance(){
        if(instance == null){
            instance = new ThirdPartyService();
        }
        return instance;
    }
    
}

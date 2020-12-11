package com.esd.model.dao;

import com.esd.model.data.persisted.Prescription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Original Author: Angela Jackson
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to prescriptions
 */

public class PrescriptionDao {
    
    private static PrescriptionDao instance;
    
    private static final String GET_All_PRESCRIPTIONS = "SELECT * FROM PRESCRIPTIONS";
    
    
    
    private PrescriptionDao() {
    }
    
    public synchronized static PrescriptionDao getInstance(){
        if(instance == null){
            instance = new PrescriptionDao();
        }
        return instance;
    }
    
}

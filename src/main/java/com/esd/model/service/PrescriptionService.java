package com.esd.model.service;

import com.esd.model.dao.PrescriptionDao;

/**
 * Original Author: Angela Jackson
 * Use: This class is a singleton, used to access prescription data objs
 */

public class PrescriptionService {
    
    private static PrescriptionService instance;
    private PrescriptionDao prescriptionDao;

    private PrescriptionService(PrescriptionDao prescriptionDao) {
        if(prescriptionDao == null){
            throw new IllegalArgumentException("prescriptionDao must not be null");
        }
        this.prescriptionDao = prescriptionDao;
    }

    public synchronized static PrescriptionService getInstance(){
        if(instance == null){
            instance = new PrescriptionService(PrescriptionDao.getInstance());
        }
        return instance;
    }

 
}

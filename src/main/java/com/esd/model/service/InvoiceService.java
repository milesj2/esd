package com.esd.model.service;

import com.esd.model.dao.UserDetailsDao;
import com.esd.model.data.persisted.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class InvoiceService {

    private InvoiceService(){
    }

    public static ArrayList<UserDetails> getInvoiceFromFilteredRequest(ArrayList<String> formKeys,
                                                                           HttpServletRequest request) {
        ArrayList<UserDetails> userDetails = UserDetailsDao.getFilteredDetails(formKeys, request);
        return userDetails;
    }

}

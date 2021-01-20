package com.esd.controller.pagecontrollers.prescriptions;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.pagecontrollers.GenericSearchController;
import com.esd.controller.pagecontrollers.search.SearchColumn;
import com.esd.controller.pagecontrollers.search.searchrow.PrescriptionSearchRow;
import com.esd.controller.pagecontrollers.search.searchrow.SearchRow;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Prescription;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.service.PrescriptionService;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet("/prescriptions/search")
@Authentication(userGroups = {UserGroup.ALL})
public class PrescriptionSearchController extends GenericSearchController {

    public PrescriptionSearchController() {
        columns = Arrays.asList(
            new SearchColumn(DaoConsts.PRESCRIPTION_ISSUE_DATE, "Prescription Date", "date")
        );

        selectedKey = "selectedInvoiceId";
    }

    @Override
    public List<SearchRow> getSearchResults(SystemUser currentUser, Map<String, Object> args) {
        List<Prescription> prescriptions = PrescriptionService.getInstance().getPrescriptionFromFilteredRequest(args);
        List<SearchRow> searchRows = new ArrayList<>();
        for(Prescription prescription : prescriptions){
            searchRows.add(new PrescriptionSearchRow(prescription));
        }
        return searchRows;
    }
}

package com.esd.controller.pagecontrollers.referals;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.pagecontrollers.GenericSearchController;
import com.esd.controller.pagecontrollers.search.SearchColumn;
import com.esd.controller.pagecontrollers.search.searchrow.AppointmentSearchRow;
import com.esd.controller.pagecontrollers.search.searchrow.ReferalSearchRow;
import com.esd.controller.pagecontrollers.search.searchrow.SearchRow;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.ReferalSearchDto;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.service.AppointmentsService;
import com.esd.model.service.InvoiceService;
import com.esd.model.service.ThirdPartyService;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet("/referals/search")
@Authentication(userGroups = {UserGroup.ALL})
public class ReferalSearchController extends GenericSearchController {
    private InvoiceService invoiceService = InvoiceService.getInstance();

    public ReferalSearchController() {
        columns = Arrays.asList(
                new SearchColumn("P." + DaoConsts.USERDETAILS_FIRSTNAME, "Patient first name", "text"),
                new SearchColumn("P." + DaoConsts.USERDETAILS_LASTNAME, "Patient last name", "text"),
                new SearchColumn("E." + DaoConsts.USERDETAILS_FIRSTNAME, "Practitioner first name", "text"),
                new SearchColumn("E." + DaoConsts.USERDETAILS_LASTNAME, "Practitioner last name", "text"),
                new SearchColumn("T." + DaoConsts.THIRDPARTY_ADDRESS1, "Third Party Address", "text"),
                new SearchColumn("T." + DaoConsts.THIRDPARTY_NAME, "Third Party name", "text"),
                new SearchColumn("A." + DaoConsts.APPOINTMENT_DATE, "Appointment Date", "date"),
                new SearchColumn("A." + DaoConsts.APPOINTMENT_TIME, "Appointment Time", "time")
        );

        selectedKey = "selectedReferalId";
    }

    @Override
    public List<SearchRow> getSearchResults(SystemUser currentUser, Map<String, Object> args) {
        List<ReferalSearchDto> invoices = ThirdPartyService.getInstance().getReferalsByFilteredResults(currentUser, args);
        List<SearchRow> searchRows = new ArrayList<>();
        for(ReferalSearchDto dto : invoices){
            searchRows.add(new ReferalSearchRow(dto));
        }
        return searchRows;
    }
}

package com.esd.controller.pagecontrollers.search.searchrow;

import com.esd.model.data.ReferalSearchDto;
import com.esd.model.data.persisted.Invoice;

import java.util.Arrays;
import java.util.HashMap;

public class ReferalSearchRow extends SearchRow {

    public ReferalSearchRow(ReferalSearchDto dto) {
        this.id = dto.getId();
        this.columns = Arrays.asList(
                dto.getPatientFirstName(),
                dto.getPatientLastName(),
                dto.getPractitionerFirstName(),
                dto.getPractitionerLastName(),
                dto.getThirdPartyAdress1(),
                dto.getThirdPartyName(),
                dto.getAppointmentDate().toString(),
                dto.getAppointmentTime().toString()
        );
        searchActions = new HashMap<>();
        searchActions.put("/appointments/viewAppointment?selectedAppointmentId="+dto.getId(), "View Appointment");
    }
}

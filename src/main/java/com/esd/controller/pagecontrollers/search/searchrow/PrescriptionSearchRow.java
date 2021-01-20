package com.esd.controller.pagecontrollers.search.searchrow;

import com.esd.model.data.persisted.Prescription;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.service.UserDetailsService;

import java.util.Arrays;
import java.util.HashMap;

public class PrescriptionSearchRow extends SearchRow {

    public PrescriptionSearchRow(Prescription prescription) {
        UserDetails patientDetails = UserDetailsService.getInstance().getUserDetailsByID(prescription.getPatientId());

        this.id = prescription.getId();
        this.columns = Arrays.asList(
                patientDetails.getFirstName(),
                patientDetails.getLastName(),
                patientDetails.getPostCode(),
                prescription.getIssueDate().toString()
        );
        this.searchActions = new HashMap<>();
        searchActions.put("/prescriptions/view?selectedPrescriptionId=" + this.id, "View Prescription");
    }
}

package com.esd.controller.pagecontrollers.search.searchrow;

import com.esd.model.data.persisted.Prescription;

import java.util.Arrays;
import java.util.HashMap;

public class PrescriptionSearchRow extends SearchRow {

    public PrescriptionSearchRow(Prescription prescription) {
        this.id = prescription.getId();
        this.columns = Arrays.asList(
                prescription.getIssueDate().toString()
        );
        this.searchActions = new HashMap<>();
        searchActions.put("/prescriptions/view?selectedPrescriptionId=" + this.id, "View Prescription");
    }
}

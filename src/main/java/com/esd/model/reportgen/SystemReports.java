package com.esd.model.reportgen;

import com.esd.model.reportgen.reports.SystemOverviewReport;

import java.util.HashMap;
import java.util.Map;

/**
 * Original author: Jordan Hellier
 *
 * use: The use of this file is to feed the available reports back to the jsp page for selection and rendering
 */
public class SystemReports {

    public static final Map<String, Report> availableReports = new HashMap<String, Report>(){{
        put("main", new SystemOverviewReport());
    }};
}

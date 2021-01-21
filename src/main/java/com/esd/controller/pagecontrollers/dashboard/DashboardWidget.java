package com.esd.controller.pagecontrollers.dashboard;

import java.util.Arrays;
import java.util.List;

public class DashboardWidget {

    public static DashboardWidget MANAGE_USERS_WIDGET = new DashboardWidget("Manage Users", "users/manage", "res/icons/users.png");
    public static DashboardWidget SEARCH_USERS_WIDGET = new DashboardWidget("Search Users", "users/search", "res/icons/user-search.png");
    public static DashboardWidget REPORTS_WIDGET = new DashboardWidget("Admin Reports", "admin/reports", "res/icons/clipboard.png");
    public static DashboardWidget INVOICE_SEARCH_WIDGET = new DashboardWidget("Search Invoices", "invoices/search", "res/icons/receipt-search.png");
    public static DashboardWidget APPOINTMENT_BOOKING_WIDGET = new DashboardWidget("Appointment Booking", "appointments/book", "res/icons/calendar-plus.png");
    public static DashboardWidget APPOINTMENT_SEARCH_WIDGET = new DashboardWidget("Appointments", "appointments/search", "res/icons/calendar.png");
    public static DashboardWidget APPOINTMENT_SCHEDULE_WIDGET = new DashboardWidget("Appointments", "appointments/schedule", "res/icons/calendar.png");
    public static DashboardWidget MANAGE_SYSTEM_SETTINGS_WIDGET = new DashboardWidget("Manage System Settings", "admin/settings", "res/icons/settings.png");
    public static DashboardWidget MANAGE_THIRD_PARTY_WIDGET = new DashboardWidget("Manage Third Party", "thirdPartyManagement/manage", "res/icons/settings.png");
    public static DashboardWidget PRESCRIPTION_SEARCH_WIDGET = new DashboardWidget("Prescriptions", "prescriptions/search", "res/icons/heart.png");
    public static DashboardWidget MY_ACCOUNT_WIDGET = new DashboardWidget("My Account", "users/my-account", "res/icons/users.png");
    public static DashboardWidget REFERAL_SEARCH_WIDGET = new DashboardWidget("ReferalSearch", "referals/search", "res/icons/users.png");

    public static final List<DashboardWidget> COMMON_WIDGETS  = Arrays.asList(
            MY_ACCOUNT_WIDGET,
            APPOINTMENT_BOOKING_WIDGET,
            APPOINTMENT_SCHEDULE_WIDGET,
            INVOICE_SEARCH_WIDGET,
            PRESCRIPTION_SEARCH_WIDGET,
            REFERAL_SEARCH_WIDGET
    );

    public static final List<DashboardWidget> PATIENT_WIDGETS  = Arrays.asList(
    );

    public static final List<DashboardWidget> COMMON_EMPLOYEE_WIDGETS  = Arrays.asList(
            SEARCH_USERS_WIDGET
    );

    public static final List<DashboardWidget> ADMIN_WIDGETS = Arrays.asList(
            MANAGE_USERS_WIDGET,
            REPORTS_WIDGET,
            APPOINTMENT_SEARCH_WIDGET,
            MANAGE_SYSTEM_SETTINGS_WIDGET,
            MANAGE_THIRD_PARTY_WIDGET
    );


    String name;
    String link;
    String icon;

    private DashboardWidget(String name, String link, String icon) {
        this.name = name;
        this.link = link;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getIcon() {
        return icon;
    }
}

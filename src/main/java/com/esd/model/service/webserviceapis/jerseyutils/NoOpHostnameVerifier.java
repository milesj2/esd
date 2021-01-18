package com.esd.model.service.webserviceapis.jerseyutils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Original Author: Trent Meier
 * Use: Host name verifier (currently accepts all)
 */

public class NoOpHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }
}

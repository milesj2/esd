package com.esd.model.service.webserviceapis.jerseyutils;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * Original Author: Trent Meier
 * Use: trust manager (trusts all)
 */

public class NoOpTrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}

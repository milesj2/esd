package com.esd.model.service.webserviceapis;

import com.esd.model.dao.ConnectionManager;
import com.esd.model.data.apis.Address;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.service.webserviceapis.jerseyutils.JerseyHttpClientFactory;
import com.esd.model.data.apis.PostcodeLookup;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Original Author: Trent Meier
 * Use: service to manages API calls to postcode validation
 */

public class AddressLookupService {
    private static AddressLookupService instance;
    private static Properties properties = new Properties();

    public AddressLookupService(){
        try {
            InputStream in = ConnectionManager.class.getResourceAsStream("/api.conf");
            properties.load(in);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static AddressLookupService getInstance() {
        if(instance == null){
            instance = new AddressLookupService();
        }
        return instance;
    }

    public static Boolean validatePostCodeExists(String postCode){
        try {
            JerseyHttpClientFactory jerseyHttpClientFactory = new JerseyHttpClientFactory();
            Client client = jerseyHttpClientFactory.getJerseyHTTPSClient();

            String url = properties.getProperty("api-url");
            Response response = client
                    .target(url)
                    .path(postCode)
                    .queryParam("api-key", properties.getProperty("api-key"))
                    .queryParam("expand", "true")
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            if(response.getStatus() == 200){
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<UserDetails> getUserDetailsAvailableAddresses(String postCode) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        ArrayList<UserDetails> userDetailsList = new ArrayList<>();

        try {
            JerseyHttpClientFactory jerseyHttpClientFactory = new JerseyHttpClientFactory();
            Client client = jerseyHttpClientFactory.getJerseyHTTPSClient();

            String url = properties.getProperty("api-url");
            Response response = client
                    .target(url)
                    .path(postCode)
                    .queryParam("api-key", properties.getProperty("api-key"))
                    .queryParam("expand", "true")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            PostcodeLookup postcodeLookup = response.readEntity(PostcodeLookup.class);

            List<Address> addresses = postcodeLookup.getAddresses();

            for(Address address: addresses) {
                UserDetails userDetails = new UserDetails();
                userDetails.setAddressLine1(address.getLine1());
                userDetails.setAddressLine2(address.getLine2());
                userDetails.setAddressLine3(address.getLine3()+address.getLine4());
                userDetails.setTown(address.getTownOrCity());
                userDetails.setPostCode(postcodeLookup.getPostcode());
                userDetailsList.add(userDetails);
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return  userDetailsList;
    }
}

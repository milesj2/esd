package com.esd.model.service.webserviceapis;

import com.esd.model.data.persisted.UserDetails;
//import com.esd.model.service.webserviceapis.jerseyutils.JerseyHttpClientFactory;
import com.esd.model.data.apis.PostcodeLookup;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;


public class AddressLookupService {
    private static String BaseAddressLookupUrl = "https://api.getAddress.io/find/bs15dq?api-key=m7YvOrA1UUCMKcjXK8Zuvw29841";
    private String AuthString = "?api-key=m7YvOrA1UUCMKcjXK8Zuvw29841";
    private static AddressLookupService instance;

    public AddressLookupService(){
    }

    public synchronized static AddressLookupService getInstance() {
        if(instance == null){
            instance = new AddressLookupService();
        }
        return instance;
    }

    public static ArrayList<UserDetails> getUserDetailsAvailableAddresses(String postCode){

        //JerseyHttpClientFactory jerseyHttpClientFactory = new JerseyHttpClientFactory();

        try {

            //Client client = jerseyHttpClientFactory.getJerseyHTTPSClient();
            Client client = null;
            Response response = client
                    .target(BaseAddressLookupUrl)
                    .path(postCode)
                    .queryParam("api-key", "m7YvOrA1UUCMKcjXK8Zuvw29841")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            int rescode = response.getStatus();

            PostcodeLookup postcodeLookup = response.readEntity(PostcodeLookup.class);
            System.out.println(postcodeLookup.toString()+rescode);

        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<UserDetails> userDetailsList = new ArrayList<>();
        return  userDetailsList;
    }
}

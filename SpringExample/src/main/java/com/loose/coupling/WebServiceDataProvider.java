package com.loose.coupling;

public class WebServiceDataProvider implements UserDataProvider{

    @Override
    public String getUserDetails() {
        return "User details from webservice ";
    }
}

package com.shortthirdman.primekit.restcountries.clientsdk;

import com.shortthirdman.primekit.restcountries.clientsdk.config.RestCountriesAPIConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestCountriesEndpointManager {

    @Autowired
    private RestCountriesAPIConfig apiConfig;

    public Object allCountries() {
        return null;
    }

    public Object countryByName(String name, boolean fullName) {
        return null;
    }
}

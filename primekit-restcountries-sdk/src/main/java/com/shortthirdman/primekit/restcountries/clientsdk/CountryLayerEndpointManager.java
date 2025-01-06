package com.shortthirdman.primekit.restcountries.clientsdk;

import com.shortthirdman.primekit.restcountries.clientsdk.config.CountryLayerAPIConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CountryLayerEndpointManager {

    @Autowired
    private CountryLayerAPIConfig apiConfig;

    public Object allCountries() {
        return null;
    }

    public Object countryByName(String name) {
        return null;
    }
}

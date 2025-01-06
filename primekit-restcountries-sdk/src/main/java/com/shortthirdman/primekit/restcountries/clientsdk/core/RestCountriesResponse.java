package com.shortthirdman.primekit.restcountries.clientsdk.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestCountriesResponse {

    private String commonName;
    private String officialName;
    private Map<String, Map<String, String>> nativeNames;
    private List<String> topLevelDomains;
    private String alpha2Code;
    private String alpha3Code;
    private String alphaNumCode;
    private String olympicCode;
    private List<String> continents;
    private List<String> currencies;
    private String region;
    private String subregion;
    private boolean isIndependent;
    private String status;
    private boolean isUnitedNationsMember;
    private boolean isLandlocked;
    private List<Integer> coordinates;
    private List<String> borders;
    private List<String> languages;
}

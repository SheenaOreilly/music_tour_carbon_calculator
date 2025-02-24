package com.example.music_tour_carbon_calculator;

import java.util.Map;

public class offsetCompanies {

    public static double atmosfair = 6.0/240.0;
    public static double carbonBalanced = 18.15/1000;
    public static double myClimate = 26.0/1000;

    public static double nativeEco = 17.2/1000;

    public static double sustainableTravelInternational = 15.28/1000;

    public static double planetair = 16.77/1000;

    public static Map<String, String> offsetLinks = Map.of(
            "Atmosfair", "https://www.atmosfair.de/en/donate/" + " " + atmosfair,
            "Carbon Balanced", "https://www.worldlandtrust.org/carbon-calculator/individual/fixed/fixed-quick-offset/" + " " + carbonBalanced,
            "My Climate", "https://co2.myclimate.org/en/contribution_calculators/new" + " " + myClimate,
            "Sustainable Travel International", "https://sustainabletravel.org/our-work/carbon-offsets/calculate-footprint/offset-by-amount/" + " " + sustainableTravelInternational,
            "Planetair", "https://app.planetair.ca/calculator/tonnage" + " " + planetair,
            "Native Energy", "https://native.eco/product/from-waste-to-fuel-improving-agriculture-and-livelihoods-in-uganda/" + " " + nativeEco
    );

    public offsetCompanies(){}
    public static Map<String, String> getOffsetLinks() {
        return offsetLinks;
    }
}

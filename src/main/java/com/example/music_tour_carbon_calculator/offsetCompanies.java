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
            "Atmosfair", "https://www.atmosfair.de/en/donate/" + "@" + atmosfair  +  "@ Currently, 90 % of atmosfair’s carbon offset projects adhere to the CDM Gold Standard, they have projects in Efficient Cook-stoves, Solar Energy, Biogas & Biomass, Hydro Power, Environmental Education, Rebuilding Tourism in Nepal and Negative Emissions.",
            "Carbon Balanced", "https://www.worldlandtrust.org/carbon-calculator/individual/fixed/fixed-quick-offset/" + "@" + carbonBalanced + "@ World Land Trust (WLT) Carbon Balanced program specialise in forest protection and restoration.",
            "My Climate", "https://co2.myclimate.org/en/contribution_calculators/new" + "@" + myClimate + "@ Myclimate has developed and supported nearly 200 climate protection projects in 46 countries around the world since its foundation in 2002. These have included reducing emissions by replacing fossil fuels with renewable energies, implementing local afforestation measures with small farmers and introducing energy-efficient technologies.",
            "Sustainable Travel International", "https://sustainabletravel.org/our-work/carbon-offsets/calculate-footprint/offset-by-amount/" + "@" + sustainableTravelInternational + "@ Sustainable Travel Internationals missions is to protect and conserve our planet’s most vulnerable destinations by transforming tourism’s impact on nature and people. They focus on Safeguarding Nature, Combating Climate Change, Empowering Communities and Tackling Waste & Pollution.",
            "Planetair", "https://app.planetair.ca/calculator/tonnage" + "@" + planetair + "@ By Supporting Planetair, your contribution finances impactful projects such as: Energy efficiency, Renewable energies, Water management, and Community initiatives",
            "Native Energy", "https://native.eco/product/from-waste-to-fuel-improving-agriculture-and-livelihoods-in-uganda/" + "@" + nativeEco + "@ Take climate action! Offset carbon emissions by supporting Native’s Waste to Fuel Project: Improving Agriculture and Livelihoods in East Africa."
    );

    public offsetCompanies(){}
    public static Map<String, String> getOffsetLinks() {
        return offsetLinks;
    }
}

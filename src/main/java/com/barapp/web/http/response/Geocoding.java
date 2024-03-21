package com.barapp.web.http.response;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Google Maps Response from Geocoding API
 */
@ToString
public class Geocoding {

    public List<Result> results;
    public String status;

    public class Result {
        public ArrayList<AddressComponent> address_components;
        public String formatted_address;
        public Geometry geometry;
        public String place_id;
        public PlusCode plus_code;
        public ArrayList<String> types;
    }

    public class AddressComponent {
        public String long_name;
        public String short_name;
        public ArrayList<String> types;
    }

    public class Geometry {
        public Location location;
        public String location_type;
        public Viewport viewport;
    }

    public class Location {
        public double lat;
        public double lng;
    }

    public class Northeast {
        public double lat;
        public double lng;
    }

    public class PlusCode {
        public String compound_code;
        public String global_code;
    }

    public class Root {
        public ArrayList<Result> results;
        public String status;
    }

    public class Southwest {
        public double lat;
        public double lng;
    }

    public class Viewport {
        public Northeast northeast;
        public Southwest southwest;
    }
}







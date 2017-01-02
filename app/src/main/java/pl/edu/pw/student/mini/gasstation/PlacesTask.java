package pl.edu.pw.student.mini.gasstation;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by PUSSY MAGNET on 25-Dec-16.
 */

@SuppressWarnings("ALL")
public class PlacesTask extends AsyncTask<String, Integer, String> {

    String data = null;
    GoogleMap GoogleMap;
    List<HashMap<String, String>> places;
    HashMap<String,String> gasStationsFromDatabase;
    HashMap<Location,String> optimalStations;



    public PlacesTask(GoogleMap gmaps, HashMap<String,String> gasStationsFromDB)
    {

        this.gasStationsFromDatabase = gasStationsFromDB;
        this.GoogleMap=gmaps;
        this.optimalStations=new HashMap<Location, String>();

    }




    // Invoked by execute() method of this object
    @Override
    protected String doInBackground(String... url) {
        try {
            data = downloadUrl(url[0]);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    // Executed after the complete execution of doInBackground() method
    @Override
    protected void onPostExecute(String result) {
        ParserTask parserTask = new ParserTask();

        // Start parsing the Google places in JSON format
        // Invokes the "doInBackground()" method of the class ParserTask
        parserTask.execute(result);



    }


    
//------------------------------put it here----------------------------
    public void findoptimalStation(){
        Location currLoc= this.GoogleMap.getMyLocation();

        for(int i =0;i<optimalStations.size();i++)
        {

        }
    }






    public void zoomNeareststation()
    {
        LatLng nearestStation = new LatLng(Double.parseDouble(places.get(0).get("lat")),Double.parseDouble(places.get(0).get("lng")));
        LatLng currLocation = new LatLng(GoogleMap.getMyLocation().getLatitude(),GoogleMap.getMyLocation().getLongitude());

        Log.d("nearest station: ",nearestStation.latitude + "," + nearestStation.longitude);
        Log.d("current location: ",currLocation.latitude + "," + currLocation.longitude);

        LatLngBounds theZoom = new LatLngBounds(currLocation,nearestStation);
        GoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(theZoom,250));

    }

    @SuppressLint("LongLogTag")
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url

            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            places = null;
            Place_JSON placeJson = new Place_JSON();

            try {
                jObject = new JSONObject(jsonData[0]);

                places = placeJson.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            Log.d("Map", "list size: " + list.size());
            // Clears all the existing markers;
            GoogleMap.clear();

            //zoomNeareststation();


            for (int i = 0; i < list.size(); i++) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);


                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("place_name");

                Log.d("the name is", name +" id:"+hmPlace.get("id"));

                // Getting vicinity
                String vicinity = hmPlace.get("vicinity");
                String markerTitle = name + " : " + vicinity;
                LatLng latLng = new LatLng(lat, lng);

                // Setting the position for the marker
                markerOptions.position(latLng);
                markerOptions.title(markerTitle);
                if(gasStationsFromDatabase.containsKey(markerTitle)){
                    markerOptions.snippet("Price: " + gasStationsFromDatabase.get(markerTitle));

                    Location tempLocation = new Location(LocationManager.NETWORK_PROVIDER);
                    tempLocation.setLatitude(lat);
                    tempLocation.setLongitude(lng);
                    String tempPrice=gasStationsFromDatabase.get(markerTitle);
                    optimalStations.put(tempLocation,tempPrice);
                    Log.d("location & price",tempLocation.getLatitude()+":"+tempLocation.getLongitude()+":"+tempPrice);


                }

                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                // Placing a marker on the touched position
                Marker m = GoogleMap.addMarker(markerOptions);

            }




        }
    }

    public class Place_JSON {

        /**
         * Receives a JSONObject and returns a list
         */
        public List<HashMap<String, String>> parse(JSONObject jObject) {

            JSONArray jPlaces = null;
            try {
                /** Retrieves all the elements in the 'places' array */
                jPlaces = jObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /** Invoking getPlaces with the array of json object
             * where each json object represent a place
             */
            return getPlaces(jPlaces);
        }

        private List<HashMap<String, String>> getPlaces(JSONArray jPlaces) {
            int placesCount = jPlaces.length();
            List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> place = null;

            /** Taking each place, parses and adds to list object */
            for (int i = 0; i < placesCount; i++) {
                try {
                    /** Call getPlace with place JSON object to parse the place */
                    place = getPlace((JSONObject) jPlaces.get(i));
                    placesList.add(place);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return placesList;
        }

        /**
         * Parsing the Place JSON object
         */
        private HashMap<String, String> getPlace(JSONObject jPlace) {

            HashMap<String, String> place = new HashMap<String, String>();
            String placeName = "-NA-";
            String vicinity = "-NA-";
            String latitude = "";
            String longitude = "";
            String reference = "";
            String id="";


            try {
                // Extracting Place name, if available
                if (!jPlace.isNull("name")) {
                    placeName = jPlace.getString("name");
                }

                // Extracting Place Vicinity, if available
                if (!jPlace.isNull("vicinity")) {
                    vicinity = jPlace.getString("vicinity");
                }

                if (!jPlace.isNull("id")) {
                    id = jPlace.getString("id");
                }

                latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");
                reference = jPlace.getString("reference");


                place.put("id",id);
                place.put("place_name", placeName);
                place.put("vicinity", vicinity);
                place.put("lat", latitude);
                place.put("lng", longitude);
                place.put("reference", reference);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return place;
        }
    }
}

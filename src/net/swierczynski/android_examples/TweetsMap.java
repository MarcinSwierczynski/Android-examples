package net.swierczynski.android_examples;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.maps.*;
import net.swierczynski.android_examples.model.TwitterEntry;
import net.swierczynski.android_examples.model.TwitterResults;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class TweetsMap extends MapActivity implements View.OnClickListener {
    protected final static String TAG = TweetsMap.class.getSimpleName();
    public final static String ENDPOINT = "http://search.twitter.com/search.json?q={query}&geocode={coordinates}";

    private MapView map;
    private MapController mapController;
    private GeoPoint currentLocation;
    private TweetsOverlay tweetsOverlay;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        attachSendRequestButtonListener();
        initMap();
        initTweetsOverlay();
        initMyLocation();
    }

    private void attachSendRequestButtonListener() {
        Button sendRequest = (Button) findViewById(R.id.send_request);
        sendRequest.setOnClickListener(this);
    }

    private void initMap() {
        map = (MapView) findViewById(R.id.map);
        mapController = map.getController();
        map.setSatellite(true);
        map.setBuiltInZoomControls(true);
    }

    private void initTweetsOverlay() {
        Drawable tweetIcon = this.getResources().getDrawable(R.drawable.androidmarker);
        tweetsOverlay = new TweetsOverlay(tweetIcon, this);
    }

    private void initMyLocation() {
        final MyLocationOverlay overlay = new MyLocationOverlay(this, map);
        overlay.enableMyLocation();
        overlay.runOnFirstFix(new Runnable() {
            public void run() {
                mapController.setZoom(7);
                currentLocation = overlay.getMyLocation();
                mapController.animateTo(currentLocation);
            }
        });
        map.getOverlays().add(overlay);
    }

    public void onClick(View view) {
        String query = ((EditText) findViewById(R.id.query)).getText().toString();

        boolean allParametersReady = query != null && query.length() > 0 && currentLocation != null;
        if (allParametersReady) {
            new TwitterSearchTask(query).execute();
        }
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private void showResults(TwitterResults results) {
        for (TwitterEntry twitterEntry : results.getResults()) {
            if (twitterEntry.getLocationPoint() != null) {
                OverlayItem item = new OverlayItem(twitterEntry.getLocationPoint(), twitterEntry.getFromUser(), twitterEntry.getText());
                tweetsOverlay.addOverlay(item);
            }
        }
        if (tweetsOverlay.size() > 0) {
            map.getOverlays().add(tweetsOverlay);
        }
    }

    private class TwitterSearchTask extends AsyncTask<Void, Void, TwitterResults> {
        private String query;

        public TwitterSearchTask(String query) {
            try {
                this.query = URLEncoder.encode(query, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        @Override
        protected TwitterResults doInBackground(Void... voids) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                return restTemplate.getForObject(ENDPOINT, TwitterResults.class, getRequestParameters());
            } catch (RestClientException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }

        private Map<String, String> getRequestParameters() {
            Map<String, String> requestParameters = new HashMap<String, String>();
            requestParameters.put("query", query);
            requestParameters.put("coordinates", convertCoordinatesToString());
            return requestParameters;
        }

        private String convertCoordinatesToString() {
            return currentLocation.getLatitudeE6()/1000000 + "," +
                   currentLocation.getLongitudeE6()/1000000 + "," +
                   "50km";
        }

        @Override
        protected void onPostExecute(TwitterResults results) {
            showResults(results);
        }
    }
}
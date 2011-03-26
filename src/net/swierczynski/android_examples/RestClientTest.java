package net.swierczynski.android_examples;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import net.swierczynski.android_examples.model.TwitterEntry;
import net.swierczynski.android_examples.model.TwitterResults;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class RestClientTest extends Activity implements View.OnClickListener {
    protected final static String TAG = RestClientTest.class.getSimpleName();
    public final static String ENDPOINT = "http://search.twitter.com/search.json?q={query}";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button sendRequest = (Button) findViewById(R.id.send_request);
        sendRequest.setOnClickListener(this);
    }

    public void onClick(View view) {
        String query = ((EditText)findViewById(R.id.query)).getText().toString();
        new TwitterSearchTask(query).execute();
    }

    private void showResults(TwitterResults results) {
        StringBuilder text = new StringBuilder();
        for (TwitterEntry twitterEntry : results.getResults()) {
            text.append("From: ").append(twitterEntry.getFromUser()).append("\n");
            text.append("Message: ").append(twitterEntry.getText()).append("\n");
            text.append("\n\n");
        }

        EditText responseEditText = (EditText) findViewById(R.id.response);
        responseEditText.setText(text);
    }

    private class TwitterSearchTask extends AsyncTask<Void, Void, TwitterResults> {
        private String query;

        public TwitterSearchTask(String query) {
            this.query = query;
        }

        @Override
        protected TwitterResults doInBackground(Void... voids) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                return restTemplate.getForObject(ENDPOINT, TwitterResults.class, query);
            } catch (RestClientException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(TwitterResults results) {
            showResults(results);
        }
    }
}
package net.swierczynski.android_examples.model;

import com.google.android.maps.GeoPoint;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitterEntry {
    @JsonProperty("from_user")
    private String fromUser;

    @JsonProperty
    private String text;

    @JsonProperty
    private String location;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public GeoPoint getLocationPoint() {
        if (location != null) {
            String[] split = location.split(" ");
            if (split.length == 2) {
                String[] coordinates = split[1].split(",");
                if (coordinates.length == 2) {
                    int latitude = (int) (Double.parseDouble(coordinates[0]) * 1000000); //convert to microdegree
                    int longitude = (int) (Double.parseDouble(coordinates[1]) * 1000000); //convert to microdegree
                    return new GeoPoint(latitude, longitude);
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("From: ").append(this.getFromUser()).append("\n");
        stringBuilder.append("Message: ").append(this.getText()).append("\n");

        GeoPoint locationPoint = this.getLocationPoint();
        if (locationPoint != null) {
            stringBuilder.append("Location: ").append(locationPoint.toString()).append("\n");
        }

        stringBuilder.append("\n\n");
        return stringBuilder.toString();
    }
}

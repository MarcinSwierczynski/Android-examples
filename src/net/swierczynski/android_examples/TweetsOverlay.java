package net.swierczynski.android_examples;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;
import java.util.List;

public class TweetsOverlay extends ItemizedOverlay {
    private Context context;
    private List<OverlayItem> overlays = new ArrayList<OverlayItem>();

    public TweetsOverlay(Drawable defaultMarker, Context context) {
        super(boundCenterBottom(defaultMarker));
        this.context = context;
    }

    public void addOverlay(OverlayItem overlay) {
        overlays.add(overlay);
        populate();
    }

    @Override
    protected OverlayItem createItem(int i) {
        return overlays.get(i);
    }

    @Override
    public int size() {
        return overlays.size();
    }

    @Override
    protected boolean onTap(int i) {
        OverlayItem item = overlays.get(i);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(item.getTitle());
        dialog.setMessage(item.getSnippet());
        dialog.show();
        return true;
    }
}

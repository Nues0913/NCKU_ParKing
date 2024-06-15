package com.example.java_final_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View infoWindowView;
    private Context context;

    public CustomInfoWindowAdapter(Context context){
        this.context = context;
        infoWindowView = LayoutInflater.from(context).inflate(R.layout.custom_marker_infowindow, null);
    }

    @Override
    public View getInfoContents(Marker marker) {
        TextView title = infoWindowView.findViewById(R.id.title);
        TextView snippet = infoWindowView.findViewById(R.id.snippet);
        View separatingLine = infoWindowView.findViewById(R.id.separatingLine);
        if(marker.getTitle() != null){
            Log.v("infoWindowAdapter","get title : " + marker.getTitle());
            title.setText(marker.getTitle());
            title.setVisibility(View.VISIBLE);
        } else {
            title.setText("");
            title.setVisibility(View.GONE);
        }
        if(marker.getSnippet() != null){

            Log.v("infoWindowAdapter", "get Snippet : " + marker.getSnippet());
            snippet.setText(marker.getSnippet());
            snippet.setVisibility(View.VISIBLE);
            separatingLine.setVisibility(View.VISIBLE);
        } else {
            snippet.setText("");
            snippet.setVisibility(View.GONE);
            separatingLine.setVisibility(View.GONE);
        }
        return infoWindowView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // use default frame
        return null;
    }
}

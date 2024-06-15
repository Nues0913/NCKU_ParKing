package com.example.java_final_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

public class CustomMarkerAdapter implements GoogleMap.InfoWindowAdapter {
    private final View infoWindowView;
    private Context context;

    public CustomMarkerAdapter(Context context){
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

    public static BitmapDescriptor setIcon(Context context, int parkingLeft){
        // this part set and draw the icon
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.baseline_location_on_24);
        drawable.setBounds(0, 60, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight() + 60);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()+60, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);

        // this part set and draw text
        Paint text = new Paint();
        text.setColor(ContextCompat.getColor(context, android.R.color.black));
        text.setTextSize(40);
        text.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        text.setTextAlign(Paint.Align.CENTER);
        float x = (float) (drawable.getIntrinsicWidth() / 2.0);
        float y = (float) ((drawable.getIntrinsicHeight() / 4.0) - ((text.descent() + text.ascent()) / 2));
        canvas.drawText(String.valueOf(parkingLeft), x, y, text);

        // this part set and draw text backGround
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(ContextCompat.getColor(context, android.R.color.white));
        backgroundPaint.setAlpha(230);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setAntiAlias(true);
        float textWidth = text.measureText(String.valueOf(parkingLeft));
        float backgroundPadding = 10;
        float rectLeft = x - textWidth / 2 - backgroundPadding;
        float rectTop = y + text.ascent() - backgroundPadding;
        float rectRight = x + textWidth / 2 + backgroundPadding;
        float rectBottom = y + text.descent() + backgroundPadding;
        RectF rect = new RectF(rectLeft, rectTop, rectRight, rectBottom);
        canvas.drawRoundRect(rect, 20, 20, backgroundPaint);
        canvas.drawText(String.valueOf(parkingLeft), x, y, text);


        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}

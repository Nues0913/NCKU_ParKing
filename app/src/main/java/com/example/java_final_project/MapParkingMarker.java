package com.example.java_final_project;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.AdvancedMarkerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapParkingMarker implements GoogleMap.OnMarkerClickListener {
    private static final Map<String, LatLng> parkingLatLugMap = new HashMap<String, LatLng>() {{
        put("光復前門地下機車停車場", new LatLng(22.996650521677473, 120.21649264621436));
        put("管理學院機車停車場", new LatLng(22.997099398841215, 120.21820272879255));
        put("雲平東側機車停車場", new LatLng(22.99865018697364, 120.21816602721896));
        put("都計系地下機車停車場", new LatLng(23.00082362591929, 120.21685035029125));
        put("修齊大樓地下機車停車場", new LatLng(23.000697926941047, 120.21714583579018));
        put("三系館地下機車停車場", new LatLng(22.997633186419254, 120.22176169709287));
        put("理學大樓地下機車停車場", new LatLng(22.99999361490511, 120.21853839576583));
        put("成功前門地下機車停車場", new LatLng(22.99623522343396, 120.22046711417113));
        put("新園平面機車停車場", new LatLng(22.99862109721078, 120.21839934632277));
        put("土木系平面機車停車場", new LatLng(22.999071160534335, 120.22209508616241));
        put("奇美樓地下機車停車場", new LatLng(22.996489810935536, 120.22200367917468));
        put("成杏平面機車停車場", new LatLng(23.001014953624665, 120.22084261124223));
        put("生醫卓群地下機車停車場", new LatLng(23.002244794511352, 120.22226037677827));
        put("社科院平面機車停車場", new LatLng(23.00135733088485, 120.21701404964543));
        put("生科大樓地下機車停車場", new LatLng(23.00345537381647, 120.21652518844802));
    }};
    private GoogleMap map;
    private final List<Marker> markerList = new ArrayList<>();
    private static final int PARKING_MARKER_TAG = 617;
    private Context context;
    private static Marker showingInfoWindowMarker;

    public MapParkingMarker(GoogleMap map, Context context) {
        this.map = map;
        this.context = context;
        map.setOnMarkerClickListener(this);
    }

    public void addAllParkingMarkers() {
        map.setInfoWindowAdapter(new CustomInfoWindowAdapter(this.context));
        for (Map.Entry<String, LatLng> entry : parkingLatLugMap.entrySet()) {
            String parkingLotName = entry.getKey();
            LatLng latLng = entry.getValue();
            Marker aMarker = map.addMarker(new AdvancedMarkerOptions()
                    .position(latLng)
                    .title(parkingLotName)
                    .snippet("null")
            );
            assert aMarker != null;
            aMarker.setTag(PARKING_MARKER_TAG);
            markerList.add(aMarker);
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Log.v("marker", "onMarkerClick");
        showingInfoWindowMarker = marker;
        if (Objects.equals(marker.getTag(), PARKING_MARKER_TAG)) {
            Map<String, Integer> parkingLeftMap = ParkingCrawler.getParkingLeftMap();
            marker.setSnippet("剩餘空位：" + parkingLeftMap.get(marker.getTitle()).toString());
            return false;
        }
        return false;
    }

    public static boolean isInfoWindowShown(){
        if(showingInfoWindowMarker == null){
            return false;
        }
        if(showingInfoWindowMarker.isInfoWindowShown()){
            return true;
        } else {
            showingInfoWindowMarker = null;
            return false;
        }
    }
}
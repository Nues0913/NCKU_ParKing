package com.example.java_final_project;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.*;

public class MapMarker {
    Map<String, LatLng> parkingLatLugMap = new HashMap<String, LatLng>() {{
        put("光復前門地下機車停車場", new LatLng(22.996650521677473, 120.21649264621436));
        put("管理學院機車停車場", new LatLng(22.997099398841215, 120.21820272879255));
        put("雲平東側機車停車場", new LatLng(22.998636098083466, 120.21849078812498));
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

    Marker NCKUmarker = map.addMarker(new MarkerOptions()
            .position(NCKUCSIE)
            .title("Marker in NCKUCSIE")
            .snippet("test context")
    );
        NCKUmarker.showInfoWindow();
}

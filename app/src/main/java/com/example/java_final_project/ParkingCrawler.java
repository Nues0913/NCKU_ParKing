package com.example.java_final_project;


import android.os.HandlerThread;
import android.util.Log;
import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParkingCrawler {
    private boolean isCrawling = false;
    private static final int CRAWLER_INTERVAL = 120000; // 2 mins
    private HandlerThread handlerThread;
    private Handler handler;
    private Runnable runnable;
    private static HashMap<String, Integer> parkingLeftMap;

    public ParkingCrawler() {
        handlerThread = new HandlerThread("CrawlerHandlerThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                updateData();
                handler.postDelayed(this, CRAWLER_INTERVAL);
            }
        };
    }

    public static Map<String, Integer> getParkingLeftMap() {
        return parkingLeftMap;
    }

    public void startCrawler() {
        if (!isCrawling) {
            handler.post(runnable);
            isCrawling = true;
            Log.v("crawler", "parkingCrawler start");
        }
    }

    public void stopCrawler() {
        handler.removeCallbacks(runnable);
        isCrawling = false;
        Log.v("crawler", "parkingCrawler killed");
    }

    private void updateData() {
        try {
            Map<String, String> payload = new HashMap<String, String>() {{
                put("campus", "all");
                put("tab", "moto");
            }};
            Document doc = Jsoup.connect("https://apss.oga.ncku.edu.tw/park/index.php/park11215/read")
                    .data(payload)
                    .post();

            Elements parkingLocations = doc.getElementsByClass("mb-2");
            Elements remainingPlaces = doc.getElementsByClass("number");

            if (parkingLocations.size() == remainingPlaces.size()) {
                HashMap<String, Integer> tmpParkingLeftMap = new HashMap<>();
                for (int i = 0; i < parkingLocations.size(); i++) {
                    String parkingLocation = parkingLocations.get(i).text();
                    Integer remainingPlace = -1;
                    try {
                        remainingPlace = Integer.parseInt(remainingPlaces.get(i).text());
                    } catch ( NumberFormatException e ){
                        Log.wtf("crawler", "crawler result error");
                    }
                    tmpParkingLeftMap.put(parkingLocation, remainingPlace);
                }
                this.parkingLeftMap = tmpParkingLeftMap;

                for (Map.Entry<String, Integer> i : tmpParkingLeftMap.entrySet()) {
                    Log.v("crawler", i.getKey() + " " + i.getValue());
                }
            }

        } catch (IOException e) {
            Log.wtf("crawler", "crawler error");
        }
    }
}

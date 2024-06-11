package com.example.java_final_project;


import android.os.HandlerThread;
import android.util.Log;
import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParkingCrawler {
    private final Map<String, Integer> scooterLeftMap = new HashMap<>();
    private boolean isCrawling = false;
    private static final int CRAWLER_INTERVAL = 5000;
    private HandlerThread handlerThread;
    private Handler handler;
    private Runnable runnable;


    public ParkingCrawler(){
    handlerThread = new HandlerThread("handlerThread");
    handlerThread.start();
    handler =new Handler(handlerThread.getLooper());
    runnable = new Runnable() {
        @Override
        public void run() {
            updateData();
            handler.postDelayed(this, CRAWLER_INTERVAL);
        }
    };
    }

    public void startCrawler(){
        if(!isCrawling){
            handler.post(runnable);
            isCrawling = true;
        }
    }

    public void stopCrawler(){
        handler.removeCallbacks(runnable);
        isCrawling = false;
    }

    private void updateData(){
        try {
            Map<String, String> payload = new HashMap<String, String>(){{
                put("campus", "all");
                put("tab", "moto");
            }};
            Document doc = Jsoup.connect("https://apss.oga.ncku.edu.tw/park/index.php/park11215/read")
                    .data(payload)
                    .post();
            String html = doc.html();

            Log.v("crawler", html);
//            String parkingSpaceName = doc.select("body > table > tbody > tr:nth-child(1)").first().text();
//            String stockPrices = doc.select("body > table > tbody > tr:nth-child(2)").first().text();
//            dataMap.put("day", day);
//            dataMap.put("stockNames", stockNames);
//            dataMap.put("stockPrices", stockPrices);
        } catch (IOException e) {
            Log.e("crawler", "crawler error");
        }
    }
}

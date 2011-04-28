package com.commonsensenet.realfarm;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class realFarm extends MapActivity {
	
	private MapController myMapController;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        myMapController = mapView.getController();
        mapView.setSatellite(true);

        List<Overlay> mapOverlays = mapView.getOverlays();
        
        Drawable drawable = this.getResources().getDrawable(R.drawable.icon);
        MyOverlay itemizedoverlay = new MyOverlay(drawable);
        
        GeoPoint ckPura = new GeoPoint(13194200,78225000);
        myMapController.animateTo(ckPura);
        OverlayItem overlayitem = new OverlayItem(ckPura, "Hello!", "I'm in ckPura!");
        itemizedoverlay.addOverlay(overlayitem);
        
        mapOverlays.add(itemizedoverlay);
                
    }
 
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
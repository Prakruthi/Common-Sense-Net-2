package com.commonsensenet.realfarm.overlay;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;

import com.commonsensenet.realfarm.PlotEditor;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class PlotOverlay extends Overlay {
	
	private Map<Integer, Polygon> hm; 
	
	public PlotOverlay(){
		hm = new HashMap<Integer, Polygon>(); 
	}
	
	public void addOverlay(Polygon polygon){
		hm.put(polygon.getId(), polygon);
	}
	
	public Polygon getOverlay(int id){
		return hm.get(id);
	}
	
	@Override public void draw(Canvas canvas, MapView mapView, boolean shadow){
		super.draw(canvas, mapView, shadow);
		
        /*
         * Define style of overlay
         */
        Paint paint = new Paint();
        paint.setStrokeWidth(7);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(3);
        
       /*
        * Load set of points to draw in overlays
        */

        for (Integer key : hm.keySet()){
        	Polygon mPolygon = hm.get(key);// get Polygon
//            Path path = new Path(); // Draw path
//            path.setFillType(Path.FillType.EVEN_ODD);
            
            paint.setARGB(100, 55, 175, 35);
            if (mPolygon.getOwner() == 1)
            	paint.setARGB(100, 228, 29, 29);
            
            	
//            int[] x = mPolygon.getX(mapView);
//			int[] y = mPolygon.getY(mapView);
//			
//			for (int i=0; i<x.length; i++){
//				
//				
//	            if (i==0)
//	                path.moveTo(x[i],y[i]); // for first point, move to it
//	            else
//	            	path.lineTo(x[i],y[i]); // For rest, add lines
//	            
//			}
//        
//            path.close();
//
        	Path path = mPolygon.getDrawable(mapView);
        	
        	// Change paint style depending on user
            PathShape pShape = new PathShape(path, (float) 100, (float) 100);
            ShapeDrawable mShape = new ShapeDrawable(pShape); 
            mShape.getPaint().set(paint);
            mShape.setBounds(0, 0, 100, 100);
            
            
            mShape.draw(canvas);
            
            
            
            
        	}
   	
	}
	
	@Override public boolean onTap(GeoPoint p, MapView mapView){
		
        Projection mProjection = mapView.getProjection();
        Point touched = new Point();
        mProjection.toPixels(p, touched);
        
        if (hm.size()>0){
        
            for (Integer key : hm.keySet()){
            	Polygon mPolygon = hm.get(key);// get Polygon
	        	if (mPolygon.contains(touched.x, touched.y, mapView)){
	        	
//	        		final QuickAction qa = new QuickAction(mapView);
//	        		ActionItem first = new ActionItem();
//	        		first.setTitle(Integer.toString(mPolygon.getId()));
//	        		first.setIcon(mapView.getResources().getDrawable(R.drawable.ic_dialog_map));
//	        		first.setId(1);
//	        		qa.addActionItem(first);	        		
//	        		qa.show(mPolygon.getCoordinates(mapView));

	        		Intent myIntent = new Intent();
	        		myIntent.setClass(mapView.getContext(), PlotEditor.class);
	        		int test = mPolygon.getId();
	        		myIntent.putExtra("ID", Integer.toString(test));
	        		
	        		
	        		// draw in bitmap
	        		Bitmap myBitmap = Bitmap.createBitmap(100, 100, Config.ARGB_8888);
	        		Canvas myCanvas = new Canvas(myBitmap);

//	        		ShapeDrawable mShape = mPolygon.getDrawable(mapView);

//	        		myCanvas.drawColor(Color.WHITE);
	        		Paint paint = new Paint();
	        		paint.setStrokeWidth(7);
	        		paint.setAntiAlias(true);
	        		paint.setDither(true);
	        		paint.setStrokeWidth(3);
	        		paint.setARGB(100, 55, 175, 35);
	        		
	        		
	        		
	        		Path path = mPolygon.getDrawable(mapView);
	            	
	            	// Change paint style depending on user
//	                PathShape pShape = new PathShape(path, (float) 100, (float) 100);
//	                ShapeDrawable mShape = new ShapeDrawable(pShape); 
//	                mShape.getPaint().set(paint);
//	                mShape.setBounds(0, 0, 100, 100);
	                
	                myCanvas.drawPath(path, paint);
	                
//	        		mShape.draw(myCanvas);
	        		
	                

	        		
	        		
//	        		Drawable d = mShape.getCurrent();
//	        		Bitmap myBitmap= ((BitmapDrawable) d).getBitmap();
	        			
	                
	        		myIntent.putExtra("Bitmap", myBitmap);
	        		mapView.getContext().startActivity(myIntent);
	        		
//	        		
//	        		final QuickPlot qa = new QuickPlot(mapView);
//	        		
//	        		ActionItem first = new ActionItem();
//	        		first.setTitle(Integer.toString(mPolygon.getId()));
//	        		first.setIcon(mapView.getResources().getDrawable(R.drawable.ic_dialog_map));
//	        		first.setId(1);
//	        		qa.addActionItem(first);
//	        		
//	        		ActionItem second = new ActionItem();
//	        		second.setTitle(Integer.toString(mPolygon.getId()));
//	        		second.setIcon(mapView.getResources().getDrawable(R.drawable.ic_title_home_default));
//	        		second.setId(2);
//	        		qa.addDiaryItem(second);
//	        		
//	        		qa.show(mPolygon.getCoordinates(mapView));

	        		
	        	}
	        }
        }
        
		return true;
	}
	
}

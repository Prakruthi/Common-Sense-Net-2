package com.commonsensenet.realfarm;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsensenet.realfarm.realFarm.PopupPanel;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class PlotOverlay extends Overlay {
	private Context mContext;
	private ManageDatabase db;
	private String user;
	private int time;
	private int x1, y1, x2, y2, x3, y3, x4, y4;
	private Polygon mPoly[] = new Polygon[4];
	private PopupPanel panel;


	PlotOverlay(ManageDatabase database, Context context, PopupPanel RPanel){
		this.db = database;
		mContext = context;
		panel = RPanel;
		
		// Define database
    	db = new ManageDatabase(mContext);
		db.open();
		db.initValues();
		db.close();
		
	}

	
	@Override public void draw(Canvas canvas, MapView mapView, boolean shadow){
		super.draw(canvas, mapView, shadow);
		
		 Cursor c;
	        
        /*
         * Define style of overlay
         */
        Paint paint = new Paint();
        paint.setStrokeWidth(7);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        

        /*
         * Define points of figure to draw
         */
       // Point screenCoords = new Point();
        Point screenCoords1 = new Point();
        Point screenCoords2 = new Point();
        Point screenCoords3 = new Point();
        
		
       /*
        * Load set of points to draw in overlays
        */
        
        db.open();
        
        // Get all plots
        c = db.GetAllEntries("plots", new String[] {"id", "userID"});
        
        if ((!c.isClosed()) && (c.getCount() > 0)) // if there are users in the table
        {
        	int i = 0;
        	c.moveToFirst();
        	do { // for each plot, draw them
        		
        		int id = c.getInt(0);

        		//Toast.makeText(mContext, "count:" + c.getCount() + " id: " +id + ", i: "+i,Toast.LENGTH_SHORT).show();
        		
        		// Draw path
                Path path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
        		
        		// Read points from database for each user
        		Cursor c2 = db.GetEntries("points", new String[] {"x", "y"}, "plotID ="+ id, null, null, null, null);
        		
        		if (c2.getCount() > 0) // if there are points to plot
        		{
        			int j = 0;
        			c2.moveToFirst();
                    int[] polyX = new int[c2.getCount()];
                    int[] polyY= new int[c2.getCount()];

        			do { // for each point in the plot, draw it
        				
        				int x1 = c2.getInt(0);
            			int y1 = c2.getInt(1);
            			
                		// Draw overlays
                        GeoPoint ckPura = new GeoPoint(x1,y1);
                        Point screenCoords = new Point();
                        mapView.getProjection().toPixels(ckPura, screenCoords);
                        paint.setARGB(100, 100, 100, 100);
                        
                        if (j==0)
                            path.moveTo(screenCoords.x,screenCoords.y); // for first point, move to it
                        else
                        	path.lineTo(screenCoords.x,screenCoords.y); // For rest, add lines
                        
                        polyX[j] = screenCoords.x;
                        polyY[j] = screenCoords.y;

                		//Toast.makeText(mContext, "x: " +screenCoords.x,Toast.LENGTH_SHORT).show();

                        j = j + 1;
                        
                        
        			}
        			while (c2.moveToNext());
        			
                    path.close();

                    // Change paint style depending on user

                    PathShape pShape = new PathShape(path, (float) 100, (float) 100);
                    ShapeDrawable mShape = new ShapeDrawable(pShape); 
                    mShape.getPaint().set(paint);
                    mShape.setBounds(0, 0, 100, 100);
                    
                    mShape.draw(canvas);

                    // Change overlay depending on user
                    
                    mPoly[i] = new Polygon(polyX, polyY, polyX.length);

        		}

                i = i + 1;
        	} 
        	while (c.moveToNext());
        } 

        // Close database
        db.close();
		
	}
	
	@Override public boolean onTap(GeoPoint p, MapView mapView){
		
        Projection mProjection = mapView.getProjection();
        Point touched = new Point();
        mProjection.toPixels(p, touched);
        
        if (mPoly[0]!=null){

        	for (int i=0; i < 3 ;i++ ){
	        	if (mPoly[i].contains(touched.x, touched.y)){
//	        		Toast.makeText(mContext, "Hell yeah",Toast.LENGTH_SHORT).show();
	        	
	        		View view = panel.getView();
	        		

	        		((TextView) view.findViewById(R.id.latitude)).setText(String.valueOf(p.getLatitudeE6() / 1000000.0));
	        		((TextView) view.findViewById(R.id.longitude)).setText(String.valueOf(p.getLongitudeE6() / 1000000.0));
	        		((TextView) view.findViewById(R.id.x)).setText(String.valueOf(touched.x));
	        		((TextView) view.findViewById(R.id.y)).setText(String.valueOf(touched.y));
	        		
	        		panel.show(touched.y * 2 > mapView.getHeight());

	        	}
	        }
        }
		return true;
	}
	
}

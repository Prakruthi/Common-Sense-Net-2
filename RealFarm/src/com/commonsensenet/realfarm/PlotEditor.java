package com.commonsensenet.realfarm;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonsensenet.realfarm.dataaccess.RealFarmDatabase;
import com.commonsensenet.realfarm.dataaccess.RealFarmProvider;

public class PlotEditor extends Activity {

	private RealFarmProvider mDataProvider;
	private int actionID = 0;
	private int[][] seeds;
	private int lastActionID ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plot_editor);

		realFarm mainApp = ((realFarm) getApplicationContext());
		RealFarmDatabase db = mainApp.getDatabase();
		mDataProvider = new RealFarmProvider(db);

		/*
		 * Display information about plot
		 */
		updatePlotInformation();

		/*
		 * Display set of possible actions
		 */
		updateRecommendations();

		/*
		 * Display set of possible actions
		 */
		updateActions();

		/*
		 * Display diary
		 */
		updateDiary();

	}

	public void updatePlotInformation() {
		LinearLayout container0 = (LinearLayout) findViewById(R.id.linearLayout0);

		container0.removeAllViews();

		TextView tv = new TextView(this);
		tv.setText(R.string.plot);
		tv.setTextSize(30);
		container0.addView(tv);

		String plotID = null;
		Bitmap mBitmap = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			plotID = extras.getString("ID");
			mBitmap = (Bitmap) extras.getParcelable("Bitmap");
		}

		ImageView ima = new ImageView(this);
		ima.setImageBitmap(mBitmap);
		container0.addView(ima);

		seeds = mDataProvider.getPlotSeed(Integer.parseInt(plotID));
		String[] name = mDataProvider.getPlotOwner(Integer.parseInt(plotID));

		TextView nameView = new TextView(this);
		if (name[0] != null)
			nameView.setText(name[0] + " " + name[1]);
		else
			nameView.setText("Unknown");

		container0.addView(nameView);

		for (int i = 0; i < seeds.length; i++) {
			TextView nameView1 = new TextView(this);
			String seedName = mDataProvider.getSeedName(seeds[i][1]);
			nameView1.setText(seedName);
			container0.addView(nameView1);
		}

		// String lastActionName =
		// int lastActionID =
		// mDataProvider.getLastAction(Integer.parseInt(plotID));

	}

	public void updateRecommendations() {
		LinearLayout container0 = (LinearLayout) findViewById(R.id.linearLayout01);
		container0.removeAllViews();

		TextView tv = new TextView(this);
		tv.setText(R.string.recommendation);
		tv.setTextSize(30);
		container0.addView(tv);
		
	}

	public void updateActions() {
		LinearLayout container = (LinearLayout) findViewById(R.id.linearLayout1);
		container.removeAllViews();

		TextView tv = new TextView(this);
		tv.setText(R.string.actions);
		tv.setTextSize(30);
		container.addView(tv);

		// get all possible actions
		Map<Integer, String> tmpMap = mDataProvider.getActions();

		// Get all executed actions
		int[][] res = mDataProvider.getDiary(seeds[0][0]);

		// for each possible action
		for (Integer key : tmpMap.keySet()) {

			String action = tmpMap.get(key);

			// Remove actions already executed
			if ((res==null) || (!arrayContains(res[1], key))) {

				Button b = new Button(this);
				b.setText(action);
				actionID = key;

				b.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						mDataProvider.setAction(actionID, seeds[0][0]);
						updateDiary();
					}
				});
				container.addView(b);
			}
		}

	}

	private boolean arrayContains(int[] res, int key) {

		if (res == null)
			return false;
		
		for (int i = 0; i < res.length; i++) {

			if (res[i] == key)
				return true;
		}
		return false;
	}

	public void updateDiary() {

		LinearLayout container2 = (LinearLayout) findViewById(R.id.linearLayout2);

		container2.removeAllViews();

		TextView tv = new TextView(this);
		tv.setText(R.string.diary);
		tv.setTextSize(30);
		container2.addView(tv);

		int[][] res = mDataProvider.getDiary(seeds[0][0]);

		if (res != null){
			for (int i = 0; i < res[0].length; i++) {
				TextView nameView1 = new TextView(this);
	
				nameView1.setText(i + " " + mDataProvider.getActionName(res[1][i])
						+ " " + res[2][i]);
				lastActionID = res[0][i];
				
				nameView1.setOnClickListener(new OnClickListener() {
	
					public void onClick(View v) {
						mDataProvider.removeAction(lastActionID);
						updateActions();
						updateDiary();
					}
				});
	
				container2.addView(nameView1);
	
			}
		}
		updateActions();

	}

	@Override
	public void onBackPressed() {
		finish();
	}
}

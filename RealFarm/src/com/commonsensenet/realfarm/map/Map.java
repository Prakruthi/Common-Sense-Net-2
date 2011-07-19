package com.commonsensenet.realfarm.map;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;

import com.commonsensenet.realfarm.MapCrawler;
import com.commonsensenet.realfarm.R;

public class Map {

	/** Default zoom level of the map. */
	public static int DEFAULT_ZOOM_LEVEL = 17;

	public static Map createDefaultMap(View view) {

		Map tmpMap = new Map();

		final int[][] tiles = {
				{ R.drawable.tile_0_0_14056179_77164847_,
						R.drawable.tile_0_1_14056179_77169159_ },
				{

				R.drawable.tile_1_0_14052007_77164847_,
						R.drawable.tile_1_1_14052007_77169159_ } };

		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				if (tiles[x][y] != -1) {
					tmpMap.mTiles.add(new MapTile(BitmapFactory.decodeResource(
							view.getResources(), tiles[x][y]),
							MapCrawler.TILE_SIZE, MapCrawler.TILE_SIZE, y, x,
							new GeoPoint(0, 0), 17, "satellite"));
				}
			}
		}

		// sets the size of the map.
		tmpMap.mWidth = MapCrawler.TILE_SIZE * tiles.length;
		tmpMap.mHeight = MapCrawler.TILE_SIZE * tiles.length;

		return tmpMap;
	}

	public static Map createMapFromCoordinate(GeoPoint center, View view) {
		Map tmpMap = new Map();

		try {

			// gets the path from the system
			String externalDirectoryPath = Environment
					.getExternalStorageDirectory().toString()
					+ MapCrawler.MAPS_FOLDER;

			// gets the file that represents the location
			File mapDir = new File(externalDirectoryPath + center.getLatitude()
					+ "," + center.getLongitude() + "/");

			Bitmap tmp = BitmapFactory.decodeResource(view.getResources(),
					R.drawable.tile_0_0_14056179_77164847_);

			// if the folder exists loads the map from it.
			if (mapDir.exists()) {
				String[] fileNames = mapDir.list();

				int numberOfTiles = (int) Math.sqrt(fileNames.length);

				int xValue, yValue;

				// !! x and y are inverted when loading.
				for (int x = 0; x < fileNames.length; x++) {
					String[] fileName = fileNames[x].split("_");
					String imagePath = mapDir.getPath() + "/" + fileNames[x];

					// position in the grid (x and y values are inverted)
					xValue = Integer.parseInt(fileName[2]);
					yValue = Integer.parseInt(fileName[1]);

					// BitmapFactory.decodeFile(imagePath)
					tmpMap.mTiles.add(new MapTile(imagePath, tmp,
							MapCrawler.TILE_SIZE, MapCrawler.TILE_SIZE, xValue,
							yValue, new GeoPoint(Integer.parseInt(fileName[3]),
									Integer.parseInt(fileName[4])), 17,
							"satellite"));
				}

				// sets the size of the map.
				tmpMap.mWidth = MapCrawler.TILE_SIZE * numberOfTiles;
				tmpMap.mHeight = MapCrawler.TILE_SIZE * numberOfTiles;

				// sorts the images according to the grid position to be able to
				// render them correctly.
				Collections.sort(tmpMap.mTiles);

				return tmpMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	/** Total height of the map in pixels. */
	private int mHeight;
	/** Initial center coordinate of the map. */
	private GeoPoint mMapCenter;
	/** Matrix containing the tiles of the map. */
	private ArrayList<MapTile> mTiles;
	/** Total width of the map in pixels. */
	private int mWidth;
	/** Current zoom of the map. */
	private int mZoom;

	public Map() {
		mZoom = DEFAULT_ZOOM_LEVEL;
		mTiles = new ArrayList<MapTile>();
	}

	public void dispose() {
		for (int x = 0; x < mTiles.size(); x++) {
			mTiles.get(x).dispose();
		}
	}

	public int getHeight() {
		return mHeight;
	}

	public GeoPoint getMapCenter() {
		return mMapCenter;
	}

	public ArrayList<MapTile> getTiles() {
		return mTiles;
	}

	public int getWidth() {
		return mWidth;
	}

	public int getZoom() {
		return mZoom;
	}
}

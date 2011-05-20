package com.commonsensenet.realfarm.map;

import android.graphics.Bitmap;

public class MapTile {

	/** Bitmap image contained by the tile. */
	private Bitmap mBitmap;
	/** Position of the tile in the x coordinate in the map. */
	private int mX;
	/** Position of the tile in the y coordinate in the map. */
	private int mY;

	// /** Geographical coordinates of the center of the image. */
	// private GeoPoint _center;

	/**
	 * Creates a new MapTile instance.
	 * 
	 * @param bitmap
	 *            bitmap that the tile will contain.
	 * @param x
	 *            position in the x coordinate.
	 * @param y
	 *            position in the y coordinate.
	 */
	public MapTile(Bitmap bitmap, int x, int y) {

		mBitmap = bitmap;
		mX = x;
		mY = y;
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	public int getHeight() {
		return mBitmap.getHeight();
	}

	public int getWidth() {
		return mBitmap.getWidth();
	}

	public int getX() {
		return mX;
	}

	public int getY() {
		return mY;
	}
}

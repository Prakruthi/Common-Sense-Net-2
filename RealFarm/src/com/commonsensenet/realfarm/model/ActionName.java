package com.commonsensenet.realfarm.model;

public class ActionName {

	private int mAudio;
	private int mId;
	// private int mLayout;
	private String mName;
	private int mRes;

	public ActionName(int id, String name, int resource, int audio) {
		mId = id;
		mName = name;
		mRes = resource;
		mAudio = audio;
	}

	public int getAudio() {
		return mAudio;
	}

	public int getId() {
		return mId;
	}

	public String getName() {
		return mName;
	}

	public int getRes() {
		return mRes;
	}
}

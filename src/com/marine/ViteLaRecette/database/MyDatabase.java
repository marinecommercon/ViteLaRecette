package com.marine.ViteLaRecette.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDatabase extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "cookeasybdd";
	private static final int DATABASE_VERSION = 3;

	public MyDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		setForcedUpgradeVersion(4);
	}
}

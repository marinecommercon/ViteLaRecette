package com.marine.ViteLaRecette.dao;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.Property;
import com.marine.ViteLaRecette.dao.Recette;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * DAO for table RECETTE.
 */
public class RecetteDao extends AbstractDao<Recette, Long> {

	public static final String TABLENAME = "RECETTE";

	/**
	 * Properties of entity Recette.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 */
	public static class Properties {
		public final static Property Id = new Property(0, Long.class, "id",
				true, "_id");
		public final static Property Nom = new Property(1, String.class, "nom",
				false, "NOM");
		public final static Property Type = new Property(2, String.class,
				"type", false, "TYPE");
		public final static Property Cuisson = new Property(3, Integer.class,
				"cuisson", false, "CUISSON");
		public final static Property Preparation = new Property(4,
				Integer.class, "preparation", false, "PREPARATION");
		public final static Property Description = new Property(5,
				String.class, "description", false, "DESCRIPTION");
		public final static Property Prix = new Property(6, Integer.class,
				"prix", false, "PRIX");
		public final static Property Difficulte = new Property(7,
				Integer.class, "difficulte", false, "DIFFICULTE");
		public final static Property Score = new Property(8, Integer.class,
				"score", false, "SCORE");
		public final static Property Nombre = new Property(9, Integer.class,
				"nombre", false, "NOMBRE");
		public final static Property Favoris = new Property(10, Integer.class,
				"favoris", false, "FAVORIS");
	};

	private DaoSession daoSession;

	public RecetteDao(DaoConfig config) {
		super(config);
	}

	public RecetteDao(DaoConfig config, DaoSession daoSession) {
		super(config, daoSession);
		this.daoSession = daoSession;
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'RECETTE' (" + //
				"'_id' INTEGER PRIMARY KEY ," + // 0: id
				"'NOM' TEXT," + // 1: nom
				"'TYPE' TEXT," + // 2: type
				"'CUISSON' INTEGER," + // 3: cuisson
				"'PREPARATION' INTEGER," + // 4: preparation
				"'DESCRIPTION' TEXT," + // 5: description
				"'PRIX' INTEGER," + // 6: prix
				"'DIFFICULTE' INTEGER," + // 7: difficulte
				"'SCORE' INTEGER," + // 8: score
				"'NOMBRE' INTEGER," + // 9: nombre
				"'FAVORIS' INTEGER);"); // 10: favoris
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'RECETTE'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, Recette entity) {
		stmt.clearBindings();

		Long id = entity.getId();
		if (id != null) {
			stmt.bindLong(1, id);
		}

		String nom = entity.getNom();
		if (nom != null) {
			stmt.bindString(2, nom);
		}

		String type = entity.getType();
		if (type != null) {
			stmt.bindString(3, type);
		}

		Integer cuisson = entity.getCuisson();
		if (cuisson != null) {
			stmt.bindLong(4, cuisson);
		}

		Integer preparation = entity.getPreparation();
		if (preparation != null) {
			stmt.bindLong(5, preparation);
		}

		String description = entity.getDescription();
		if (description != null) {
			stmt.bindString(6, description);
		}

		Integer prix = entity.getPrix();
		if (prix != null) {
			stmt.bindLong(7, prix);
		}

		Integer difficulte = entity.getDifficulte();
		if (difficulte != null) {
			stmt.bindLong(8, difficulte);
		}

		Integer score = entity.getScore();
		if (score != null) {
			stmt.bindLong(9, score);
		}

		Integer nombre = entity.getNombre();
		if (nombre != null) {
			stmt.bindLong(10, nombre);
		}

		Integer favoris = entity.getFavoris();
		if (favoris != null) {
			stmt.bindLong(11, favoris);
		}
	}

	@Override
	protected void attachEntity(Recette entity) {
		super.attachEntity(entity);
		entity.__setDaoSession(daoSession);
	}

	/** @inheritdoc */
	@Override
	public Long readKey(Cursor cursor, int offset) {
		return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
	}

	/** @inheritdoc */
	@Override
	public Recette readEntity(Cursor cursor, int offset) {
		Recette entity = new Recette(
				//
				cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
				cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // nom
				cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // type
				cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // cuisson
				cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // preparation
				cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // description
				cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // prix
				cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // difficulte
				cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // score
				cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // nombre
				cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10) // favoris
		);
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, Recette entity, int offset) {
		entity.setId(cursor.isNull(offset + 0) ? null : cursor
				.getLong(offset + 0));
		entity.setNom(cursor.isNull(offset + 1) ? null : cursor
				.getString(offset + 1));
		entity.setType(cursor.isNull(offset + 2) ? null : cursor
				.getString(offset + 2));
		entity.setCuisson(cursor.isNull(offset + 3) ? null : cursor
				.getInt(offset + 3));
		entity.setPreparation(cursor.isNull(offset + 4) ? null : cursor
				.getInt(offset + 4));
		entity.setDescription(cursor.isNull(offset + 5) ? null : cursor
				.getString(offset + 5));
		entity.setPrix(cursor.isNull(offset + 6) ? null : cursor
				.getInt(offset + 6));
		entity.setDifficulte(cursor.isNull(offset + 7) ? null : cursor
				.getInt(offset + 7));
		entity.setScore(cursor.isNull(offset + 8) ? null : cursor
				.getInt(offset + 8));
		entity.setNombre(cursor.isNull(offset + 9) ? null : cursor
				.getInt(offset + 9));
		entity.setFavoris(cursor.isNull(offset + 10) ? null : cursor
				.getInt(offset + 10));
	}

	/** @inheritdoc */
	@Override
	protected Long updateKeyAfterInsert(Recette entity, long rowId) {
		entity.setId(rowId);
		return rowId;
	}

	/** @inheritdoc */
	@Override
	public Long getKey(Recette entity) {
		if (entity != null) {
			return entity.getId();
		} else {
			return null;
		}
	}

	/** @inheritdoc */
	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

}

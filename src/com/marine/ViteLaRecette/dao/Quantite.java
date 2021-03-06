package com.marine.ViteLaRecette.dao;


import de.greenrobot.dao.DaoException;
import com.marine.ViteLaRecette.dao.DaoSession;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table QUANTITE.
 */
public class Quantite {

	private Long id;
	private Float quantite;
	private String suffixe;
	private long recetteId;
	private Long ingredientId;
	private Long mesureId;

	/** Used to resolve relations */
	private transient DaoSession daoSession;

	/** Used for active entity operations. */
	private transient QuantiteDao myDao;

	private Recette recette;
	private Long recette__resolvedKey;

	private Ingredient ingredient;
	private Long ingredient__resolvedKey;

	private Mesure mesure;
	private Long mesure__resolvedKey;

	public Quantite() {
	}

	public Quantite(Long id) {
		this.id = id;
	}

	public Quantite(Long id, Float quantite, String suffixe, long recetteId,
			Long ingredientId, Long mesureId) {
		this.id = id;
		this.quantite = quantite;
		this.suffixe = suffixe;
		this.recetteId = recetteId;
		this.ingredientId = ingredientId;
		this.mesureId = mesureId;
	}

	/** called by internal mechanisms, do not call yourself. */
	public void __setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
		myDao = daoSession != null ? daoSession.getQuantiteDao() : null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getQuantite() {
		return quantite;
	}

	public void setQuantite(Float quantite) {
		this.quantite = quantite;
	}

	public String getSuffixe() {
		return suffixe;
	}

	public void setSuffixe(String suffixe) {
		this.suffixe = suffixe;
	}

	public long getRecetteId() {
		return recetteId;
	}

	public void setRecetteId(long recetteId) {
		this.recetteId = recetteId;
	}

	public Long getIngredientId() {
		return ingredientId;
	}

	public void setIngredientId(Long ingredientId) {
		this.ingredientId = ingredientId;
	}

	public Long getMesureId() {
		return mesureId;
	}

	public void setMesureId(Long mesureId) {
		this.mesureId = mesureId;
	}

	/** To-one relationship, resolved on first access. */
	public Recette getRecette() {
		if (recette__resolvedKey == null
				|| !recette__resolvedKey.equals(recetteId)) {
			if (daoSession == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			RecetteDao targetDao = daoSession.getRecetteDao();
			recette = targetDao.load(recetteId);
			recette__resolvedKey = recetteId;
		}
		return recette;
	}

	public void setRecette(Recette recette) {
		if (recette == null) {
			throw new DaoException(
					"To-one property 'recetteId' has not-null constraint; cannot set to-one to null");
		}
		this.recette = recette;
		recetteId = recette.getId();
		recette__resolvedKey = recetteId;
	}

	/** To-one relationship, resolved on first access. */
	public Ingredient getIngredient() {
		if (ingredient__resolvedKey == null
				|| !ingredient__resolvedKey.equals(ingredientId)) {
			if (daoSession == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			IngredientDao targetDao = daoSession.getIngredientDao();
			ingredient = targetDao.load(ingredientId);
			ingredient__resolvedKey = ingredientId;
		}
		return ingredient;
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
		ingredientId = ingredient == null ? null : ingredient.getId();
		ingredient__resolvedKey = ingredientId;
	}

	/** To-one relationship, resolved on first access. */
	public Mesure getMesure() {
		if (mesure__resolvedKey == null
				|| !mesure__resolvedKey.equals(mesureId)) {
			if (daoSession == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			MesureDao targetDao = daoSession.getMesureDao();
			mesure = targetDao.load(mesureId);
			mesure__resolvedKey = mesureId;
		}
		return mesure;
	}

	public void setMesure(Mesure mesure) {
		this.mesure = mesure;
		mesureId = mesure == null ? null : mesure.getId();
		mesure__resolvedKey = mesureId;
	}

	/**
	 * Convenient call for {@link AbstractDao#delete(Object)}. Entity must
	 * attached to an entity context.
	 */
	public void delete() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.delete(this);
	}

	/**
	 * Convenient call for {@link AbstractDao#update(Object)}. Entity must
	 * attached to an entity context.
	 */
	public void update() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.update(this);
	}

	/**
	 * Convenient call for {@link AbstractDao#refresh(Object)}. Entity must
	 * attached to an entity context.
	 */
	public void refresh() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.refresh(this);
	}

}

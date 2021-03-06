package com.clerton.leal.tracker.manager.impl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import roboguice.util.Ln;

import com.clerton.leal.tracker.database.DatabaseHelper;
import com.clerton.leal.tracker.manager.AbstractManager;
import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

/**
 * @author clertonleal@gmail.com
 * @since 23/03/2013
 * 
 * This class should be extended by all others concrete managers.
 * Thus leaving only the specific methods therefor.
 */
public abstract class AbstractManagerImpl<managerType> implements AbstractManager<managerType>{

	@Inject
	private DatabaseHelper databaseHelper;
	
	private Class<managerType> clazz;
	
	private Dao<managerType, Integer> dao;
	
	private QueryBuilder<managerType, Integer> queryBuilder;
	
	private DeleteBuilder<managerType, Integer> deleteBuilder;
	
	
	public AbstractManagerImpl(Class<managerType> clazz){
		this.clazz = clazz;
	}
	
	protected Dao<managerType, Integer> getDao() throws SQLException {
		if(dao == null){
			dao = databaseHelper.getDao(clazz);
		}
		
		return dao;
	}
	
	protected QueryBuilder<managerType, Integer> getQueryBuilder() throws SQLException {
		if(queryBuilder == null){
			queryBuilder = getDao().queryBuilder();
		}
		
		queryBuilder.clear();
		
		return queryBuilder;
	}
	
	protected DeleteBuilder<managerType, Integer> getDeleteBuilder() throws SQLException {
		if(deleteBuilder == null){
			deleteBuilder = getDao().deleteBuilder();
		}
		
		deleteBuilder.clear();
		
		return deleteBuilder;
	}
	
	@Override
	public void create(final managerType managerType) {
		try {
			getDao().create(managerType);
		} catch (SQLException e) {
			Ln.e(e, "Error to create " + clazz.getName() + " in database");
		}
	}
	
	@Override
	public void update(final managerType managerType) {
		try {
			getDao().update(managerType);
		} catch (SQLException e) {
			Ln.e(e, "Error to update " + clazz.getName() + " in database");
		}
	}
	
	@Override
	public void createOrUpdate(final managerType managerType) {
		try {
			getDao().createOrUpdate(managerType);
		} catch (SQLException e) {
			Ln.e(e, "Error to createOrUpdate " + clazz.getName() + " in database");
		}
	}
	
	@Override
	public void delete(final Integer id) {
		try {
			getDao().deleteById(id);
		} catch (SQLException e) {
			Ln.e(e, "Error to delete " + clazz.getName() + " in database");
		}
	}
	
	@Override
	public managerType getById(final Integer id) {
		try {
			return getDao().queryForId(id);
		} catch (SQLException e) {
			Ln.e(e, "Error to getById " + clazz.getName() + " in database");
		}
		
		return null;
	}
	
	@Override
	public List<managerType> getAll() {
		try {
			return getDao().queryForAll();
		} catch (SQLException e) {
			Ln.e(e, "Error to getAll " + clazz.getName() + " in database");
		}
		
		return Collections.emptyList();
	}
	
	@Override
	public void deleteAll() {
		try {
			getDeleteBuilder().delete();
		} catch (SQLException e) {
			Ln.e(e, "Error to delete " + clazz.getName() + " in database");
		}
	}
	
	@Override
	public int countOf() {
		try {
			return (int) dao.countOf();
		} catch (SQLException e) {
			Ln.e(e, "Error to count " + clazz.getName() + " in database");
		}
		
		return 0;
	}
}

package com.liwei.greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.liwei.greendao.Customer;

import com.liwei.greendao.CustomerDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig customerDaoConfig;

    private final CustomerDao customerDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        customerDaoConfig = daoConfigMap.get(CustomerDao.class).clone();
        customerDaoConfig.initIdentityScope(type);

        customerDao = new CustomerDao(customerDaoConfig, this);

        registerDao(Customer.class, customerDao);
    }
    
    public void clear() {
        customerDaoConfig.getIdentityScope().clear();
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

}
package com.liwei.greendao;

import com.liwei.greendao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "USER".
 */
public class User {

    private Long id;
    private Long pictureId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient UserDao myDao;

    private Picture picture;
    private Long picture__resolvedKey;


    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, Long pictureId) {
        this.id = id;
        this.pictureId = pictureId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }

    /** To-one relationship, resolved on first access. */
    public Picture getPicture() {
        Long __key = this.pictureId;
        if (picture__resolvedKey == null || !picture__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PictureDao targetDao = daoSession.getPictureDao();
            Picture pictureNew = targetDao.load(__key);
            synchronized (this) {
                picture = pictureNew;
            	picture__resolvedKey = __key;
            }
        }
        return picture;
    }

    public void setPicture(Picture picture) {
        synchronized (this) {
            this.picture = picture;
            pictureId = picture == null ? null : picture.getId();
            picture__resolvedKey = pictureId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}

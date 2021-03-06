package com.study.yang.lifehelper.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.study.yang.lifehelper.db.Voice;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "VOICE".
*/
public class VoiceDao extends AbstractDao<Voice, Long> {

    public static final String TABLENAME = "VOICE";

    /**
     * Properties of entity Voice.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Filename = new Property(1, String.class, "filename", false, "FILENAME");
        public final static Property Duration = new Property(2, Long.class, "duration", false, "DURATION");
        public final static Property Filepath = new Property(3, String.class, "filepath", false, "FILEPATH");
    };


    public VoiceDao(DaoConfig config) {
        super(config);
    }
    
    public VoiceDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"VOICE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"FILENAME\" TEXT," + // 1: filename
                "\"DURATION\" INTEGER," + // 2: duration
                "\"FILEPATH\" TEXT);"); // 3: filepath
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"VOICE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Voice entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String filename = entity.getFilename();
        if (filename != null) {
            stmt.bindString(2, filename);
        }
 
        Long duration = entity.getDuration();
        if (duration != null) {
            stmt.bindLong(3, duration);
        }
 
        String filepath = entity.getFilepath();
        if (filepath != null) {
            stmt.bindString(4, filepath);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Voice readEntity(Cursor cursor, int offset) {
        Voice entity = new Voice( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // filename
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // duration
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // filepath
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Voice entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFilename(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDuration(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setFilepath(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Voice entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Voice entity) {
        if(entity != null) {
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

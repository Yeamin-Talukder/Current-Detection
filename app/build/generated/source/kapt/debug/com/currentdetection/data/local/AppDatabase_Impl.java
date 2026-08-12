package com.currentdetection.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile NetworkDao _networkDao;

  private volatile PowerEventDao _powerEventDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `networks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `displayName` TEXT NOT NULL, `ssid` TEXT NOT NULL, `bssid` TEXT NOT NULL, `enabled` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `power_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `startTime` INTEGER NOT NULL, `endTime` INTEGER, `duration` INTEGER, `detectedCheckerCount` INTEGER NOT NULL, `totalCheckerCount` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'cfc274f8ea491945c521a2e2fe9153ce')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `networks`");
        db.execSQL("DROP TABLE IF EXISTS `power_events`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsNetworks = new HashMap<String, TableInfo.Column>(6);
        _columnsNetworks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNetworks.put("displayName", new TableInfo.Column("displayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNetworks.put("ssid", new TableInfo.Column("ssid", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNetworks.put("bssid", new TableInfo.Column("bssid", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNetworks.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNetworks.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNetworks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNetworks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNetworks = new TableInfo("networks", _columnsNetworks, _foreignKeysNetworks, _indicesNetworks);
        final TableInfo _existingNetworks = TableInfo.read(db, "networks");
        if (!_infoNetworks.equals(_existingNetworks)) {
          return new RoomOpenHelper.ValidationResult(false, "networks(com.currentdetection.data.local.entities.NetworkEntity).\n"
                  + " Expected:\n" + _infoNetworks + "\n"
                  + " Found:\n" + _existingNetworks);
        }
        final HashMap<String, TableInfo.Column> _columnsPowerEvents = new HashMap<String, TableInfo.Column>(6);
        _columnsPowerEvents.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPowerEvents.put("startTime", new TableInfo.Column("startTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPowerEvents.put("endTime", new TableInfo.Column("endTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPowerEvents.put("duration", new TableInfo.Column("duration", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPowerEvents.put("detectedCheckerCount", new TableInfo.Column("detectedCheckerCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPowerEvents.put("totalCheckerCount", new TableInfo.Column("totalCheckerCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPowerEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPowerEvents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPowerEvents = new TableInfo("power_events", _columnsPowerEvents, _foreignKeysPowerEvents, _indicesPowerEvents);
        final TableInfo _existingPowerEvents = TableInfo.read(db, "power_events");
        if (!_infoPowerEvents.equals(_existingPowerEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "power_events(com.currentdetection.data.local.entities.PowerEventEntity).\n"
                  + " Expected:\n" + _infoPowerEvents + "\n"
                  + " Found:\n" + _existingPowerEvents);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "cfc274f8ea491945c521a2e2fe9153ce", "1dc9cf04fca104d71b29e08c4ba1d33d");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "networks","power_events");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `networks`");
      _db.execSQL("DELETE FROM `power_events`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(NetworkDao.class, NetworkDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PowerEventDao.class, PowerEventDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public NetworkDao networkDao() {
    if (_networkDao != null) {
      return _networkDao;
    } else {
      synchronized(this) {
        if(_networkDao == null) {
          _networkDao = new NetworkDao_Impl(this);
        }
        return _networkDao;
      }
    }
  }

  @Override
  public PowerEventDao powerEventDao() {
    if (_powerEventDao != null) {
      return _powerEventDao;
    } else {
      synchronized(this) {
        if(_powerEventDao == null) {
          _powerEventDao = new PowerEventDao_Impl(this);
        }
        return _powerEventDao;
      }
    }
  }
}

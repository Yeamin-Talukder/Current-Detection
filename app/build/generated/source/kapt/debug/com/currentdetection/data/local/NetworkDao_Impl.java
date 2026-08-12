package com.currentdetection.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.currentdetection.data.local.entities.NetworkEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NetworkDao_Impl implements NetworkDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<NetworkEntity> __insertionAdapterOfNetworkEntity;

  private final EntityDeletionOrUpdateAdapter<NetworkEntity> __deletionAdapterOfNetworkEntity;

  private final EntityDeletionOrUpdateAdapter<NetworkEntity> __updateAdapterOfNetworkEntity;

  public NetworkDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNetworkEntity = new EntityInsertionAdapter<NetworkEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `networks` (`id`,`displayName`,`ssid`,`bssid`,`enabled`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NetworkEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getDisplayName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDisplayName());
        }
        if (entity.getSsid() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSsid());
        }
        if (entity.getBssid() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getBssid());
        }
        final int _tmp = entity.getEnabled() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfNetworkEntity = new EntityDeletionOrUpdateAdapter<NetworkEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `networks` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NetworkEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfNetworkEntity = new EntityDeletionOrUpdateAdapter<NetworkEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `networks` SET `id` = ?,`displayName` = ?,`ssid` = ?,`bssid` = ?,`enabled` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NetworkEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getDisplayName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDisplayName());
        }
        if (entity.getSsid() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSsid());
        }
        if (entity.getBssid() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getBssid());
        }
        final int _tmp = entity.getEnabled() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getId());
      }
    };
  }

  @Override
  public Object insertNetwork(final NetworkEntity network,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfNetworkEntity.insertAndReturnId(network);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteNetwork(final NetworkEntity network,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfNetworkEntity.handle(network);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateNetwork(final NetworkEntity network,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfNetworkEntity.handle(network);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<NetworkEntity>> getAllNetworks() {
    final String _sql = "SELECT * FROM networks ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"networks"}, new Callable<List<NetworkEntity>>() {
      @Override
      @NonNull
      public List<NetworkEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfSsid = CursorUtil.getColumnIndexOrThrow(_cursor, "ssid");
          final int _cursorIndexOfBssid = CursorUtil.getColumnIndexOrThrow(_cursor, "bssid");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<NetworkEntity> _result = new ArrayList<NetworkEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NetworkEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDisplayName;
            if (_cursor.isNull(_cursorIndexOfDisplayName)) {
              _tmpDisplayName = null;
            } else {
              _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            }
            final String _tmpSsid;
            if (_cursor.isNull(_cursorIndexOfSsid)) {
              _tmpSsid = null;
            } else {
              _tmpSsid = _cursor.getString(_cursorIndexOfSsid);
            }
            final String _tmpBssid;
            if (_cursor.isNull(_cursorIndexOfBssid)) {
              _tmpBssid = null;
            } else {
              _tmpBssid = _cursor.getString(_cursorIndexOfBssid);
            }
            final boolean _tmpEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new NetworkEntity(_tmpId,_tmpDisplayName,_tmpSsid,_tmpBssid,_tmpEnabled,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<NetworkEntity>> getEnabledNetworks() {
    final String _sql = "SELECT * FROM networks WHERE enabled = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"networks"}, new Callable<List<NetworkEntity>>() {
      @Override
      @NonNull
      public List<NetworkEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfSsid = CursorUtil.getColumnIndexOrThrow(_cursor, "ssid");
          final int _cursorIndexOfBssid = CursorUtil.getColumnIndexOrThrow(_cursor, "bssid");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<NetworkEntity> _result = new ArrayList<NetworkEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NetworkEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDisplayName;
            if (_cursor.isNull(_cursorIndexOfDisplayName)) {
              _tmpDisplayName = null;
            } else {
              _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            }
            final String _tmpSsid;
            if (_cursor.isNull(_cursorIndexOfSsid)) {
              _tmpSsid = null;
            } else {
              _tmpSsid = _cursor.getString(_cursorIndexOfSsid);
            }
            final String _tmpBssid;
            if (_cursor.isNull(_cursorIndexOfBssid)) {
              _tmpBssid = null;
            } else {
              _tmpBssid = _cursor.getString(_cursorIndexOfBssid);
            }
            final boolean _tmpEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new NetworkEntity(_tmpId,_tmpDisplayName,_tmpSsid,_tmpBssid,_tmpEnabled,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

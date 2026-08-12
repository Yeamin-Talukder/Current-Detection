package com.currentdetection.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.currentdetection.data.local.entities.PowerEventEntity;
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
public final class PowerEventDao_Impl implements PowerEventDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PowerEventEntity> __insertionAdapterOfPowerEventEntity;

  private final EntityDeletionOrUpdateAdapter<PowerEventEntity> __updateAdapterOfPowerEventEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearAllEvents;

  public PowerEventDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPowerEventEntity = new EntityInsertionAdapter<PowerEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `power_events` (`id`,`startTime`,`endTime`,`duration`,`detectedCheckerCount`,`totalCheckerCount`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PowerEventEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStartTime());
        if (entity.getEndTime() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getEndTime());
        }
        if (entity.getDuration() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getDuration());
        }
        statement.bindLong(5, entity.getDetectedCheckerCount());
        statement.bindLong(6, entity.getTotalCheckerCount());
      }
    };
    this.__updateAdapterOfPowerEventEntity = new EntityDeletionOrUpdateAdapter<PowerEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `power_events` SET `id` = ?,`startTime` = ?,`endTime` = ?,`duration` = ?,`detectedCheckerCount` = ?,`totalCheckerCount` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PowerEventEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStartTime());
        if (entity.getEndTime() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getEndTime());
        }
        if (entity.getDuration() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getDuration());
        }
        statement.bindLong(5, entity.getDetectedCheckerCount());
        statement.bindLong(6, entity.getTotalCheckerCount());
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfClearAllEvents = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM power_events";
        return _query;
      }
    };
  }

  @Override
  public Object insertEvent(final PowerEventEntity event,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPowerEventEntity.insertAndReturnId(event);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateEvent(final PowerEventEntity event,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPowerEventEntity.handle(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllEvents(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllEvents.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAllEvents.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PowerEventEntity>> getAllEvents() {
    final String _sql = "SELECT * FROM power_events ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"power_events"}, new Callable<List<PowerEventEntity>>() {
      @Override
      @NonNull
      public List<PowerEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfDetectedCheckerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "detectedCheckerCount");
          final int _cursorIndexOfTotalCheckerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCheckerCount");
          final List<PowerEventEntity> _result = new ArrayList<PowerEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PowerEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            final int _tmpDetectedCheckerCount;
            _tmpDetectedCheckerCount = _cursor.getInt(_cursorIndexOfDetectedCheckerCount);
            final int _tmpTotalCheckerCount;
            _tmpTotalCheckerCount = _cursor.getInt(_cursorIndexOfTotalCheckerCount);
            _item = new PowerEventEntity(_tmpId,_tmpStartTime,_tmpEndTime,_tmpDuration,_tmpDetectedCheckerCount,_tmpTotalCheckerCount);
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
  public Object getActiveOutageEvent(final Continuation<? super PowerEventEntity> $completion) {
    final String _sql = "SELECT * FROM power_events WHERE endTime IS NULL ORDER BY startTime DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PowerEventEntity>() {
      @Override
      @Nullable
      public PowerEventEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfDetectedCheckerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "detectedCheckerCount");
          final int _cursorIndexOfTotalCheckerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCheckerCount");
          final PowerEventEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            final int _tmpDetectedCheckerCount;
            _tmpDetectedCheckerCount = _cursor.getInt(_cursorIndexOfDetectedCheckerCount);
            final int _tmpTotalCheckerCount;
            _tmpTotalCheckerCount = _cursor.getInt(_cursorIndexOfTotalCheckerCount);
            _result = new PowerEventEntity(_tmpId,_tmpStartTime,_tmpEndTime,_tmpDuration,_tmpDetectedCheckerCount,_tmpTotalCheckerCount);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<PowerEventEntity> getActiveOutageEventFlow() {
    final String _sql = "SELECT * FROM power_events WHERE endTime IS NULL ORDER BY startTime DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"power_events"}, new Callable<PowerEventEntity>() {
      @Override
      @Nullable
      public PowerEventEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfDetectedCheckerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "detectedCheckerCount");
          final int _cursorIndexOfTotalCheckerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCheckerCount");
          final PowerEventEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            final int _tmpDetectedCheckerCount;
            _tmpDetectedCheckerCount = _cursor.getInt(_cursorIndexOfDetectedCheckerCount);
            final int _tmpTotalCheckerCount;
            _tmpTotalCheckerCount = _cursor.getInt(_cursorIndexOfTotalCheckerCount);
            _result = new PowerEventEntity(_tmpId,_tmpStartTime,_tmpEndTime,_tmpDuration,_tmpDetectedCheckerCount,_tmpTotalCheckerCount);
          } else {
            _result = null;
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
  public Flow<List<PowerEventEntity>> getEventsInRange(final long startTime, final long endTime) {
    final String _sql = "SELECT * FROM power_events WHERE startTime >= ? AND startTime < ? ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endTime);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"power_events"}, new Callable<List<PowerEventEntity>>() {
      @Override
      @NonNull
      public List<PowerEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfDetectedCheckerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "detectedCheckerCount");
          final int _cursorIndexOfTotalCheckerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCheckerCount");
          final List<PowerEventEntity> _result = new ArrayList<PowerEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PowerEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            final int _tmpDetectedCheckerCount;
            _tmpDetectedCheckerCount = _cursor.getInt(_cursorIndexOfDetectedCheckerCount);
            final int _tmpTotalCheckerCount;
            _tmpTotalCheckerCount = _cursor.getInt(_cursorIndexOfTotalCheckerCount);
            _item = new PowerEventEntity(_tmpId,_tmpStartTime,_tmpEndTime,_tmpDuration,_tmpDetectedCheckerCount,_tmpTotalCheckerCount);
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
  public Object getAllEventsList(final Continuation<? super List<PowerEventEntity>> $completion) {
    final String _sql = "SELECT * FROM power_events ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PowerEventEntity>>() {
      @Override
      @NonNull
      public List<PowerEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfDetectedCheckerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "detectedCheckerCount");
          final int _cursorIndexOfTotalCheckerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCheckerCount");
          final List<PowerEventEntity> _result = new ArrayList<PowerEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PowerEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            final int _tmpDetectedCheckerCount;
            _tmpDetectedCheckerCount = _cursor.getInt(_cursorIndexOfDetectedCheckerCount);
            final int _tmpTotalCheckerCount;
            _tmpTotalCheckerCount = _cursor.getInt(_cursorIndexOfTotalCheckerCount);
            _item = new PowerEventEntity(_tmpId,_tmpStartTime,_tmpEndTime,_tmpDuration,_tmpDetectedCheckerCount,_tmpTotalCheckerCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

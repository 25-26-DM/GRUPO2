package ec.edu.uce.final_kdledesma.data;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<User> __insertAdapterOfUser;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfUser = new EntityInsertAdapter<User>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `users` (`username`,`password`,`syncStatus`,`lastModified`,`isDeleted`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, @NonNull final User entity) {
        if (entity.getUsername() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getUsername());
        }
        if (entity.getPassword() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getPassword());
        }
        if (entity.getSyncStatus() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getSyncStatus());
        }
        statement.bindLong(4, entity.getLastModified());
        final int _tmp = entity.isDeleted() ? 1 : 0;
        statement.bindLong(5, _tmp);
      }
    };
  }

  @Override
  public Object insertUser(final User user, final Continuation<? super Unit> $completion) {
    if (user == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfUser.insert(_connection, user);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object validateUser(final String username, final String password,
      final Continuation<? super User> $completion) {
    final String _sql = "SELECT * FROM users WHERE username = ? AND password = ? AND isDeleted = 0 LIMIT 1";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (username == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, username);
        }
        _argIndex = 2;
        if (password == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, password);
        }
        final int _columnIndexOfUsername = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "username");
        final int _columnIndexOfPassword = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "password");
        final int _columnIndexOfSyncStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "syncStatus");
        final int _columnIndexOfLastModified = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastModified");
        final int _columnIndexOfIsDeleted = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isDeleted");
        final User _result;
        if (_stmt.step()) {
          final String _tmpUsername;
          if (_stmt.isNull(_columnIndexOfUsername)) {
            _tmpUsername = null;
          } else {
            _tmpUsername = _stmt.getText(_columnIndexOfUsername);
          }
          final String _tmpPassword;
          if (_stmt.isNull(_columnIndexOfPassword)) {
            _tmpPassword = null;
          } else {
            _tmpPassword = _stmt.getText(_columnIndexOfPassword);
          }
          final String _tmpSyncStatus;
          if (_stmt.isNull(_columnIndexOfSyncStatus)) {
            _tmpSyncStatus = null;
          } else {
            _tmpSyncStatus = _stmt.getText(_columnIndexOfSyncStatus);
          }
          final long _tmpLastModified;
          _tmpLastModified = _stmt.getLong(_columnIndexOfLastModified);
          final boolean _tmpIsDeleted;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIsDeleted));
          _tmpIsDeleted = _tmp != 0;
          _result = new User(_tmpUsername,_tmpPassword,_tmpSyncStatus,_tmpLastModified,_tmpIsDeleted);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getUserByUsername(final String username,
      final Continuation<? super User> $completion) {
    final String _sql = "SELECT * FROM users WHERE username = ? LIMIT 1";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (username == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, username);
        }
        final int _columnIndexOfUsername = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "username");
        final int _columnIndexOfPassword = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "password");
        final int _columnIndexOfSyncStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "syncStatus");
        final int _columnIndexOfLastModified = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastModified");
        final int _columnIndexOfIsDeleted = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isDeleted");
        final User _result;
        if (_stmt.step()) {
          final String _tmpUsername;
          if (_stmt.isNull(_columnIndexOfUsername)) {
            _tmpUsername = null;
          } else {
            _tmpUsername = _stmt.getText(_columnIndexOfUsername);
          }
          final String _tmpPassword;
          if (_stmt.isNull(_columnIndexOfPassword)) {
            _tmpPassword = null;
          } else {
            _tmpPassword = _stmt.getText(_columnIndexOfPassword);
          }
          final String _tmpSyncStatus;
          if (_stmt.isNull(_columnIndexOfSyncStatus)) {
            _tmpSyncStatus = null;
          } else {
            _tmpSyncStatus = _stmt.getText(_columnIndexOfSyncStatus);
          }
          final long _tmpLastModified;
          _tmpLastModified = _stmt.getLong(_columnIndexOfLastModified);
          final boolean _tmpIsDeleted;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIsDeleted));
          _tmpIsDeleted = _tmp != 0;
          _result = new User(_tmpUsername,_tmpPassword,_tmpSyncStatus,_tmpLastModified,_tmpIsDeleted);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getPendingSync(final Continuation<? super List<User>> $completion) {
    final String _sql = "SELECT * FROM users WHERE syncStatus = 'pending' AND isDeleted = 0";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfUsername = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "username");
        final int _columnIndexOfPassword = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "password");
        final int _columnIndexOfSyncStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "syncStatus");
        final int _columnIndexOfLastModified = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastModified");
        final int _columnIndexOfIsDeleted = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isDeleted");
        final List<User> _result = new ArrayList<User>();
        while (_stmt.step()) {
          final User _item;
          final String _tmpUsername;
          if (_stmt.isNull(_columnIndexOfUsername)) {
            _tmpUsername = null;
          } else {
            _tmpUsername = _stmt.getText(_columnIndexOfUsername);
          }
          final String _tmpPassword;
          if (_stmt.isNull(_columnIndexOfPassword)) {
            _tmpPassword = null;
          } else {
            _tmpPassword = _stmt.getText(_columnIndexOfPassword);
          }
          final String _tmpSyncStatus;
          if (_stmt.isNull(_columnIndexOfSyncStatus)) {
            _tmpSyncStatus = null;
          } else {
            _tmpSyncStatus = _stmt.getText(_columnIndexOfSyncStatus);
          }
          final long _tmpLastModified;
          _tmpLastModified = _stmt.getLong(_columnIndexOfLastModified);
          final boolean _tmpIsDeleted;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIsDeleted));
          _tmpIsDeleted = _tmp != 0;
          _item = new User(_tmpUsername,_tmpPassword,_tmpSyncStatus,_tmpLastModified,_tmpIsDeleted);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object updateSyncStatus(final String username, final String status,
      final Continuation<? super Unit> $completion) {
    final String _sql = "UPDATE users SET syncStatus = ? WHERE username = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (status == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, status);
        }
        _argIndex = 2;
        if (username == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, username);
        }
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

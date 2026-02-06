package ec.edu.uce.final_kdledesma.data;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
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
public final class ProductDao_Impl implements ProductDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Product> __insertAdapterOfProduct;

  private final EntityDeleteOrUpdateAdapter<Product> __deleteAdapterOfProduct;

  private final EntityDeleteOrUpdateAdapter<Product> __updateAdapterOfProduct;

  public ProductDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfProduct = new EntityInsertAdapter<Product>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `products` (`code`,`description`,`manufactureDate`,`cost`,`available`,`photo`,`syncStatus`,`lastModified`,`isDeleted`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, @NonNull final Product entity) {
        if (entity.getCode() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getCode());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getDescription());
        }
        statement.bindLong(3, entity.getManufactureDate());
        statement.bindDouble(4, entity.getCost());
        final int _tmp = entity.getAvailable() ? 1 : 0;
        statement.bindLong(5, _tmp);
        if (entity.getPhoto() == null) {
          statement.bindNull(6);
        } else {
          statement.bindBlob(6, entity.getPhoto());
        }
        if (entity.getSyncStatus() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getSyncStatus());
        }
        statement.bindLong(8, entity.getLastModified());
        final int _tmp_1 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
      }
    };
    this.__deleteAdapterOfProduct = new EntityDeleteOrUpdateAdapter<Product>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `products` WHERE `code` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, @NonNull final Product entity) {
        if (entity.getCode() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getCode());
        }
      }
    };
    this.__updateAdapterOfProduct = new EntityDeleteOrUpdateAdapter<Product>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `products` SET `code` = ?,`description` = ?,`manufactureDate` = ?,`cost` = ?,`available` = ?,`photo` = ?,`syncStatus` = ?,`lastModified` = ?,`isDeleted` = ? WHERE `code` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, @NonNull final Product entity) {
        if (entity.getCode() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getCode());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getDescription());
        }
        statement.bindLong(3, entity.getManufactureDate());
        statement.bindDouble(4, entity.getCost());
        final int _tmp = entity.getAvailable() ? 1 : 0;
        statement.bindLong(5, _tmp);
        if (entity.getPhoto() == null) {
          statement.bindNull(6);
        } else {
          statement.bindBlob(6, entity.getPhoto());
        }
        if (entity.getSyncStatus() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getSyncStatus());
        }
        statement.bindLong(8, entity.getLastModified());
        final int _tmp_1 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        if (entity.getCode() == null) {
          statement.bindNull(10);
        } else {
          statement.bindText(10, entity.getCode());
        }
      }
    };
  }

  @Override
  public Object insert(final Product product, final Continuation<? super Unit> $completion) {
    if (product == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfProduct.insert(_connection, product);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object delete(final Product product, final Continuation<? super Unit> $completion) {
    if (product == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __deleteAdapterOfProduct.handle(_connection, product);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object update(final Product product, final Continuation<? super Unit> $completion) {
    if (product == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __updateAdapterOfProduct.handle(_connection, product);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object getAll(final Continuation<? super List<Product>> $completion) {
    final String _sql = "SELECT * FROM products WHERE isDeleted = 0 ORDER BY description";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "code");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfManufactureDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "manufactureDate");
        final int _columnIndexOfCost = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cost");
        final int _columnIndexOfAvailable = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "available");
        final int _columnIndexOfPhoto = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "photo");
        final int _columnIndexOfSyncStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "syncStatus");
        final int _columnIndexOfLastModified = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastModified");
        final int _columnIndexOfIsDeleted = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isDeleted");
        final List<Product> _result = new ArrayList<Product>();
        while (_stmt.step()) {
          final Product _item;
          final String _tmpCode;
          if (_stmt.isNull(_columnIndexOfCode)) {
            _tmpCode = null;
          } else {
            _tmpCode = _stmt.getText(_columnIndexOfCode);
          }
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          final long _tmpManufactureDate;
          _tmpManufactureDate = _stmt.getLong(_columnIndexOfManufactureDate);
          final double _tmpCost;
          _tmpCost = _stmt.getDouble(_columnIndexOfCost);
          final boolean _tmpAvailable;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfAvailable));
          _tmpAvailable = _tmp != 0;
          final byte[] _tmpPhoto;
          if (_stmt.isNull(_columnIndexOfPhoto)) {
            _tmpPhoto = null;
          } else {
            _tmpPhoto = _stmt.getBlob(_columnIndexOfPhoto);
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
          final int _tmp_1;
          _tmp_1 = (int) (_stmt.getLong(_columnIndexOfIsDeleted));
          _tmpIsDeleted = _tmp_1 != 0;
          _item = new Product(_tmpCode,_tmpDescription,_tmpManufactureDate,_tmpCost,_tmpAvailable,_tmpPhoto,_tmpSyncStatus,_tmpLastModified,_tmpIsDeleted);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getByCode(final String code, final Continuation<? super Product> $completion) {
    final String _sql = "SELECT * FROM products WHERE code = ? LIMIT 1";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (code == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, code);
        }
        final int _columnIndexOfCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "code");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfManufactureDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "manufactureDate");
        final int _columnIndexOfCost = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cost");
        final int _columnIndexOfAvailable = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "available");
        final int _columnIndexOfPhoto = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "photo");
        final int _columnIndexOfSyncStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "syncStatus");
        final int _columnIndexOfLastModified = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastModified");
        final int _columnIndexOfIsDeleted = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isDeleted");
        final Product _result;
        if (_stmt.step()) {
          final String _tmpCode;
          if (_stmt.isNull(_columnIndexOfCode)) {
            _tmpCode = null;
          } else {
            _tmpCode = _stmt.getText(_columnIndexOfCode);
          }
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          final long _tmpManufactureDate;
          _tmpManufactureDate = _stmt.getLong(_columnIndexOfManufactureDate);
          final double _tmpCost;
          _tmpCost = _stmt.getDouble(_columnIndexOfCost);
          final boolean _tmpAvailable;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfAvailable));
          _tmpAvailable = _tmp != 0;
          final byte[] _tmpPhoto;
          if (_stmt.isNull(_columnIndexOfPhoto)) {
            _tmpPhoto = null;
          } else {
            _tmpPhoto = _stmt.getBlob(_columnIndexOfPhoto);
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
          final int _tmp_1;
          _tmp_1 = (int) (_stmt.getLong(_columnIndexOfIsDeleted));
          _tmpIsDeleted = _tmp_1 != 0;
          _result = new Product(_tmpCode,_tmpDescription,_tmpManufactureDate,_tmpCost,_tmpAvailable,_tmpPhoto,_tmpSyncStatus,_tmpLastModified,_tmpIsDeleted);
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
  public Object getPendingSync(final Continuation<? super List<Product>> $completion) {
    final String _sql = "SELECT * FROM products WHERE syncStatus = 'pending' AND isDeleted = 0";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "code");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfManufactureDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "manufactureDate");
        final int _columnIndexOfCost = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cost");
        final int _columnIndexOfAvailable = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "available");
        final int _columnIndexOfPhoto = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "photo");
        final int _columnIndexOfSyncStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "syncStatus");
        final int _columnIndexOfLastModified = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastModified");
        final int _columnIndexOfIsDeleted = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isDeleted");
        final List<Product> _result = new ArrayList<Product>();
        while (_stmt.step()) {
          final Product _item;
          final String _tmpCode;
          if (_stmt.isNull(_columnIndexOfCode)) {
            _tmpCode = null;
          } else {
            _tmpCode = _stmt.getText(_columnIndexOfCode);
          }
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          final long _tmpManufactureDate;
          _tmpManufactureDate = _stmt.getLong(_columnIndexOfManufactureDate);
          final double _tmpCost;
          _tmpCost = _stmt.getDouble(_columnIndexOfCost);
          final boolean _tmpAvailable;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfAvailable));
          _tmpAvailable = _tmp != 0;
          final byte[] _tmpPhoto;
          if (_stmt.isNull(_columnIndexOfPhoto)) {
            _tmpPhoto = null;
          } else {
            _tmpPhoto = _stmt.getBlob(_columnIndexOfPhoto);
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
          final int _tmp_1;
          _tmp_1 = (int) (_stmt.getLong(_columnIndexOfIsDeleted));
          _tmpIsDeleted = _tmp_1 != 0;
          _item = new Product(_tmpCode,_tmpDescription,_tmpManufactureDate,_tmpCost,_tmpAvailable,_tmpPhoto,_tmpSyncStatus,_tmpLastModified,_tmpIsDeleted);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getPendingDeletion(final Continuation<? super List<Product>> $completion) {
    final String _sql = "SELECT * FROM products WHERE syncStatus = 'pending' AND isDeleted = 1";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "code");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfManufactureDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "manufactureDate");
        final int _columnIndexOfCost = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cost");
        final int _columnIndexOfAvailable = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "available");
        final int _columnIndexOfPhoto = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "photo");
        final int _columnIndexOfSyncStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "syncStatus");
        final int _columnIndexOfLastModified = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastModified");
        final int _columnIndexOfIsDeleted = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isDeleted");
        final List<Product> _result = new ArrayList<Product>();
        while (_stmt.step()) {
          final Product _item;
          final String _tmpCode;
          if (_stmt.isNull(_columnIndexOfCode)) {
            _tmpCode = null;
          } else {
            _tmpCode = _stmt.getText(_columnIndexOfCode);
          }
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          final long _tmpManufactureDate;
          _tmpManufactureDate = _stmt.getLong(_columnIndexOfManufactureDate);
          final double _tmpCost;
          _tmpCost = _stmt.getDouble(_columnIndexOfCost);
          final boolean _tmpAvailable;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfAvailable));
          _tmpAvailable = _tmp != 0;
          final byte[] _tmpPhoto;
          if (_stmt.isNull(_columnIndexOfPhoto)) {
            _tmpPhoto = null;
          } else {
            _tmpPhoto = _stmt.getBlob(_columnIndexOfPhoto);
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
          final int _tmp_1;
          _tmp_1 = (int) (_stmt.getLong(_columnIndexOfIsDeleted));
          _tmpIsDeleted = _tmp_1 != 0;
          _item = new Product(_tmpCode,_tmpDescription,_tmpManufactureDate,_tmpCost,_tmpAvailable,_tmpPhoto,_tmpSyncStatus,_tmpLastModified,_tmpIsDeleted);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object updateSyncStatus(final String code, final String status,
      final Continuation<? super Unit> $completion) {
    final String _sql = "UPDATE products SET syncStatus = ? WHERE code = ?";
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
        if (code == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, code);
        }
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object markAsDeleted(final String code, final long timestamp,
      final Continuation<? super Unit> $completion) {
    final String _sql = "UPDATE products SET isDeleted = 1, syncStatus = 'pending', lastModified = ? WHERE code = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
        if (code == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, code);
        }
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object cleanupSyncedDeletions(final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM products WHERE isDeleted = 1 AND syncStatus = 'synced'";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
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

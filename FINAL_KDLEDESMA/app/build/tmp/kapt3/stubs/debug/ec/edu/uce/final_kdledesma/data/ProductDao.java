package ec.edu.uce.final_kdledesma.data;

@kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u0018\u0010\u0006\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u001e\u0010\f\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\u000fJ\u0016\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0004H\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u0016\u0010\u0013\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0004H\u00a7@\u00a2\u0006\u0002\u0010\u0012J \u0010\u0014\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u00a7@\u00a2\u0006\u0002\u0010\u0017J\u000e\u0010\u0018\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u0016\u0010\u0019\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0004H\u00a7@\u00a2\u0006\u0002\u0010\u0012\u00a8\u0006\u001a\u00c0\u0006\u0003"}, d2 = {"Lec/edu/uce/final_kdledesma/data/ProductDao;", "", "getAll", "", "Lec/edu/uce/final_kdledesma/data/Product;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getByCode", "code", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPendingSync", "getPendingDeletion", "updateSyncStatus", "", "status", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "product", "(Lec/edu/uce/final_kdledesma/data/Product;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "markAsDeleted", "timestamp", "", "(Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "cleanupSyncedDeletions", "delete", "app_debug"})
@androidx.room.Dao()
public abstract interface ProductDao {
    
    @androidx.room.Query(value = "SELECT * FROM products WHERE isDeleted = 0 ORDER BY description")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<ec.edu.uce.final_kdledesma.data.Product>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM products WHERE code = :code LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getByCode(@org.jetbrains.annotations.NotNull()
    java.lang.String code, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super ec.edu.uce.final_kdledesma.data.Product> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM products WHERE syncStatus = \'pending\' AND isDeleted = 0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPendingSync(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<ec.edu.uce.final_kdledesma.data.Product>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM products WHERE syncStatus = \'pending\' AND isDeleted = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPendingDeletion(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<ec.edu.uce.final_kdledesma.data.Product>> $completion);
    
    @androidx.room.Query(value = "UPDATE products SET syncStatus = :status WHERE code = :code")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSyncStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String code, @org.jetbrains.annotations.NotNull()
    java.lang.String status, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    ec.edu.uce.final_kdledesma.data.Product product, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    ec.edu.uce.final_kdledesma.data.Product product, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE products SET isDeleted = 1, syncStatus = \'pending\', lastModified = :timestamp WHERE code = :code")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markAsDeleted(@org.jetbrains.annotations.NotNull()
    java.lang.String code, long timestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM products WHERE isDeleted = 1 AND syncStatus = \'synced\'")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object cleanupSyncedDeletions(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    ec.edu.uce.final_kdledesma.data.Product product, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @kotlin.Metadata(mv = {2, 3, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}
package ec.edu.uce.final_kdledesma.data;

@kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\bg\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010\b\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00030\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u001e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0007J\u0016\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0012\u00a8\u0006\u0013\u00c0\u0006\u0003"}, d2 = {"Lec/edu/uce/final_kdledesma/data/UserDao;", "", "validateUser", "Lec/edu/uce/final_kdledesma/data/User;", "username", "", "password", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserByUsername", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPendingSync", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateSyncStatus", "", "status", "insertUser", "user", "(Lec/edu/uce/final_kdledesma/data/User;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface UserDao {
    
    @androidx.room.Query(value = "SELECT * FROM users WHERE username = :username AND password = :password AND isDeleted = 0 LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object validateUser(@org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super ec.edu.uce.final_kdledesma.data.User> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM users WHERE username = :username LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUserByUsername(@org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super ec.edu.uce.final_kdledesma.data.User> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM users WHERE syncStatus = \'pending\' AND isDeleted = 0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPendingSync(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<ec.edu.uce.final_kdledesma.data.User>> $completion);
    
    @androidx.room.Query(value = "UPDATE users SET syncStatus = :status WHERE username = :username")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSyncStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    java.lang.String status, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertUser(@org.jetbrains.annotations.NotNull()
    ec.edu.uce.final_kdledesma.data.User user, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}
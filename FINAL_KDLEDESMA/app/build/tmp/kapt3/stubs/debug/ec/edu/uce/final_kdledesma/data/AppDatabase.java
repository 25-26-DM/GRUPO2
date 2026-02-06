package ec.edu.uce.final_kdledesma.data;

@kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \b2\u00020\u0001:\u0001\bB\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0007H&\u00a8\u0006\t"}, d2 = {"Lec/edu/uce/final_kdledesma/data/AppDatabase;", "Landroidx/room/RoomDatabase;", "<init>", "()V", "userDao", "Lec/edu/uce/final_kdledesma/data/UserDao;", "productDao", "Lec/edu/uce/final_kdledesma/data/ProductDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {ec.edu.uce.final_kdledesma.data.User.class, ec.edu.uce.final_kdledesma.data.Product.class}, version = 2)
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile ec.edu.uce.final_kdledesma.data.AppDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_1_2 = null;
    @org.jetbrains.annotations.NotNull()
    public static final ec.edu.uce.final_kdledesma.data.AppDatabase.Companion Companion = null;
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract ec.edu.uce.final_kdledesma.data.UserDao userDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract ec.edu.uce.final_kdledesma.data.ProductDao productDao();
    
    @kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000bR\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\b\u00a8\u0006\u000e"}, d2 = {"Lec/edu/uce/final_kdledesma/data/AppDatabase$Companion;", "", "<init>", "()V", "INSTANCE", "Lec/edu/uce/final_kdledesma/data/AppDatabase;", "MIGRATION_1_2", "Landroidx/room/migration/Migration;", "Landroidx/room/migration/Migration;", "getDatabase", "context", "Landroid/content/Context;", "prepopulateIfEmpty", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final ec.edu.uce.final_kdledesma.data.AppDatabase getDatabase(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        public final void prepopulateIfEmpty(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
    }
}
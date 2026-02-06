package ec.edu.uce.final_kdledesma.sync;

@kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u0016\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0015J\u0018\u0010\u0016\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0017\u001a\u00020\u0005H\u0086@\u00a2\u0006\u0002\u0010\u0018J\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00140\u001aH\u0086@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u0005H\u0086@\u00a2\u0006\u0002\u0010\u0018J\u0016\u0010\u001d\u001a\u00020\u00122\u0006\u0010\u001e\u001a\u00020\u001fH\u0086@\u00a2\u0006\u0002\u0010 J\u0018\u0010!\u001a\u0004\u0018\u00010\u001f2\u0006\u0010\"\u001a\u00020\u0005H\u0086@\u00a2\u0006\u0002\u0010\u0018J\u000e\u0010#\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u001bJ\u000e\u0010$\u001a\u00020\u000eH\u0082@\u00a2\u0006\u0002\u0010\u001bJ\u000e\u0010%\u001a\u00020\u000eH\u0082@\u00a2\u0006\u0002\u0010\u001bJ\u000e\u0010&\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u001bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lec/edu/uce/final_kdledesma/sync/DynamoDBHelper;", "", "<init>", "()V", "TAG", "", "REGION", "PRODUCTS_TABLE", "USERS_TABLE", "DUMMY_ACCESS_KEY", "DUMMY_SECRET_KEY", "client", "Laws/sdk/kotlin/services/dynamodb/DynamoDbClient;", "initialize", "", "context", "Landroid/content/Context;", "putProduct", "", "product", "Lec/edu/uce/final_kdledesma/data/Product;", "(Lec/edu/uce/final_kdledesma/data/Product;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getProduct", "code", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllProducts", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteProduct", "putUser", "user", "Lec/edu/uce/final_kdledesma/data/User;", "(Lec/edu/uce/final_kdledesma/data/User;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUser", "username", "createTablesIfNotExist", "createProductsTable", "createUsersTable", "close", "app_debug"})
public final class DynamoDBHelper {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "DynamoDBHelper";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String REGION = "us-east-1";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PRODUCTS_TABLE = "Products";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String USERS_TABLE = "Users";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String DUMMY_ACCESS_KEY = "ACCESS_KEY";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String DUMMY_SECRET_KEY = "SECRET_KEY";
    private static aws.sdk.kotlin.services.dynamodb.DynamoDbClient client;
    @org.jetbrains.annotations.NotNull()
    public static final ec.edu.uce.final_kdledesma.sync.DynamoDBHelper INSTANCE = null;
    
    private DynamoDBHelper() {
        super();
    }
    
    public final void initialize(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object putProduct(@org.jetbrains.annotations.NotNull()
    ec.edu.uce.final_kdledesma.data.Product product, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getProduct(@org.jetbrains.annotations.NotNull()
    java.lang.String code, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super ec.edu.uce.final_kdledesma.data.Product> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAllProducts(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<ec.edu.uce.final_kdledesma.data.Product>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteProduct(@org.jetbrains.annotations.NotNull()
    java.lang.String code, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object putUser(@org.jetbrains.annotations.NotNull()
    ec.edu.uce.final_kdledesma.data.User user, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getUser(@org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super ec.edu.uce.final_kdledesma.data.User> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createTablesIfNotExist(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object createProductsTable(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object createUsersTable(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object close(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}
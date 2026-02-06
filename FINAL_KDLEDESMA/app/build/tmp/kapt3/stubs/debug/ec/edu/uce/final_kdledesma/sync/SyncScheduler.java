package ec.edu.uce.final_kdledesma.sync;

/**
 * Scheduler para configurar la sincronización automática periódica
 */
@kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lec/edu/uce/final_kdledesma/sync/SyncScheduler;", "", "<init>", "()V", "TAG", "", "SYNC_WORK_NAME", "schedulePeriodic", "", "context", "Landroid/content/Context;", "scheduleImmediate", "cancelAll", "app_debug"})
public final class SyncScheduler {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "SyncScheduler";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String SYNC_WORK_NAME = "periodic_sync_work";
    @org.jetbrains.annotations.NotNull()
    public static final ec.edu.uce.final_kdledesma.sync.SyncScheduler INSTANCE = null;
    
    private SyncScheduler() {
        super();
    }
    
    /**
     * Programa una sincronización periódica cada 15 minutos
     * Solo se ejecuta cuando hay conexión a Internet
     */
    public final void schedulePeriodic(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    /**
     * Programa una sincronización inmediata en cuanto haya Internet
     */
    public final void scheduleImmediate(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    /**
     * Cancela todas las sincronizaciones programadas
     */
    public final void cancelAll(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
}
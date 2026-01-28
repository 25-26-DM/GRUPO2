package ec.edu.uce.final_svacurio.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Product::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Agregar campos de sincronización a products
                db.execSQL("ALTER TABLE products ADD COLUMN syncStatus TEXT NOT NULL DEFAULT 'pending'")
                db.execSQL("ALTER TABLE products ADD COLUMN lastModified INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
                db.execSQL("ALTER TABLE products ADD COLUMN isDeleted INTEGER NOT NULL DEFAULT 0")

                // Agregar campos de sincronización a users
                db.execSQL("ALTER TABLE users ADD COLUMN syncStatus TEXT NOT NULL DEFAULT 'pending'")
                db.execSQL("ALTER TABLE users ADD COLUMN lastModified INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
                db.execSQL("ALTER TABLE users ADD COLUMN isDeleted INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                .addMigrations(MIGRATION_1_2)
                .build()
                INSTANCE = instance
                instance
            }
        }

        fun prepopulateIfEmpty(context: Context) {
            val db = getDatabase(context)
            CoroutineScope(Dispatchers.IO).launch {
                val dao = db.productDao()
                if (dao.getAll().isEmpty()) {
                    dao.insert(Product("P001", "Producto A", System.currentTimeMillis(), 10.0, true, null))
                    dao.insert(Product("P002", "Producto B", System.currentTimeMillis(), 20.5, true, null))
                    dao.insert(Product("P003", "Producto C", System.currentTimeMillis(), 15.75, false, null))
                }
            }
        }
    }
}

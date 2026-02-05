package ec.edu.uce.final_aetroyab.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val password: String,
    val syncStatus: String = "pending", // pending, synced, error
    val lastModified: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
)
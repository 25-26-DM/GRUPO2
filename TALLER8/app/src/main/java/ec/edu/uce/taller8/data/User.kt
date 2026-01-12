package ec.edu.uce.taller8.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    // WARNING: In a real app, passwords should be hashed and salted, not stored in plain text.
    val password: String
)
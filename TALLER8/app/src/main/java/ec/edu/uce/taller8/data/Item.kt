package ec.edu.uce.taller8.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "items",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int, // Foreign key referencing User
    val name: String,
    val price: Double,
    val quantity: Int
)
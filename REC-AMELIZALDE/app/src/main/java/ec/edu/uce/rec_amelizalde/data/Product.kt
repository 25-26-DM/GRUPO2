package ec.edu.uce.rec_amelizalde.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "products")
data class Product(
    @PrimaryKey val code: String,
    val description: String,
    val manufactureDate: Long,
    val cost: Double,
    val available: Boolean,
    val photo: ByteArray? = null,
    val syncStatus: String = "pending", // pending, synced, error
    val lastModified: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
) : Parcelable
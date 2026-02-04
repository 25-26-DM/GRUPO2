package ec.edu.uce.final_erenriquezp.sync

import android.content.Context
import android.util.Log
import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import ec.edu.uce.final_erenriquezp.data.Product
import ec.edu.uce.final_erenriquezp.data.User
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.*
import aws.smithy.kotlin.runtime.net.url.Url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DynamoDBHelper {
    private const val TAG = "DynamoDBHelper"
    private const val REGION = "us-east-1"
    private const val PRODUCTS_TABLE = "Products"
    private const val USERS_TABLE = "Users"

    // ¡IMPORTANTE! Usa credenciales falsas para el desarrollo local.
    // NUNCA subas credenciales reales a tu control de versiones.
    private const val DUMMY_ACCESS_KEY = "ACCESS_KEY"   
    private const val DUMMY_SECRET_KEY = "SECRET_KEY"

    // Apunta a tu instancia local de DynamoDB (por ejemplo, en Docker).
    // 10.0.2.2 es la IP del host desde el emulador de Android.

    private lateinit var client: DynamoDbClient

    fun initialize(context: Context) {
        if (::client.isInitialized) return

        client = DynamoDbClient {
            region = REGION
            credentialsProvider = StaticCredentialsProvider {
                accessKeyId = DUMMY_ACCESS_KEY
                secretAccessKey = DUMMY_SECRET_KEY
            }
        }
    }

    // ========== PRODUCT OPERATIONS ========== 

    suspend fun putProduct(product: Product): Boolean = withContext(Dispatchers.IO) {
        try {
            val item = mutableMapOf(
                "code" to AttributeValue.S(product.code),
                "description" to AttributeValue.S(product.description),
                "manufactureDate" to AttributeValue.N(product.manufactureDate.toString()),
                "cost" to AttributeValue.N(product.cost.toString()),
                "available" to AttributeValue.Bool(product.available),
                "lastModified" to AttributeValue.N(product.lastModified.toString()),
                "isDeleted" to AttributeValue.Bool(product.isDeleted)
            )

            product.photo?.let {
                item["photo"] = AttributeValue.B(it)
            }

            val request = PutItemRequest {
                tableName = PRODUCTS_TABLE
                this.item = item
            }

            client.putItem(request)
            Log.d(TAG, "Product ${product.code} synced to DynamoDB")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing product ${product.code}", e)
            false
        }
    }

    suspend fun getProduct(code: String): Product? = withContext(Dispatchers.IO) {
        try {
            val key = mapOf("code" to AttributeValue.S(code))
            val request = GetItemRequest { tableName = PRODUCTS_TABLE; this.key = key }
            val response = client.getItem(request)

            response.item?.let { item ->
                Product(
                    code = item["code"]?.asS() ?: "",
                    description = item["description"]?.asS() ?: "",
                    manufactureDate = item["manufactureDate"]?.asN()?.toLongOrNull() ?: 0L,
                    cost = item["cost"]?.asN()?.toDoubleOrNull() ?: 0.0,
                    available = item["available"]?.asBool() ?: false,
                    photo = item["photo"]?.asB(),
                    lastModified = item["lastModified"]?.asN()?.toLongOrNull() ?: 0L,
                    isDeleted = item["isDeleted"]?.asBool() ?: false,
                    syncStatus = "synced"
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting product $code", e)
            null
        }
    }

    suspend fun getAllProducts(): List<Product> = withContext(Dispatchers.IO) {
        try {
            val request = ScanRequest { tableName = PRODUCTS_TABLE }
            val response = client.scan(request)
            response.items?.mapNotNull { item ->
                try {
                    Product(
                        code = item["code"]?.asS() ?: "",
                        description = item["description"]?.asS() ?: "",
                        manufactureDate = item["manufactureDate"]?.asN()?.toLongOrNull() ?: 0L,
                        cost = item["cost"]?.asN()?.toDoubleOrNull() ?: 0.0,
                        available = item["available"]?.asBool() ?: false,
                        photo = item["photo"]?.asB(),
                        lastModified = item["lastModified"]?.asN()?.toLongOrNull() ?: 0L,
                        isDeleted = item["isDeleted"]?.asBool() ?: false,
                        syncStatus = "synced"
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing product", e)
                    null
                }
            } ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting all products", e)
            emptyList()
        }
    }

    suspend fun deleteProduct(code: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val key = mapOf("code" to AttributeValue.S(code))
            val request = DeleteItemRequest { tableName = PRODUCTS_TABLE; this.key = key }
            client.deleteItem(request)
            Log.d(TAG, "Product $code deleted from DynamoDB")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting product $code", e)
            false
        }
    }

    // ========== USER OPERATIONS ========== 

    suspend fun putUser(user: User): Boolean = withContext(Dispatchers.IO) {
        try {
            val item = mapOf(
                "username" to AttributeValue.S(user.username),
                "password" to AttributeValue.S(user.password),
                "lastModified" to AttributeValue.N(System.currentTimeMillis().toString()),
                "isDeleted" to AttributeValue.Bool(false)
            )
            val request = PutItemRequest { tableName = USERS_TABLE; this.item = item }
            client.putItem(request)
            Log.d(TAG, "User ${user.username} synced to DynamoDB")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing user ${user.username}", e)
            false
        }
    }

    suspend fun getUser(username: String): User? = withContext(Dispatchers.IO) {
        try {
            val key = mapOf("username" to AttributeValue.S(username))
            val request = GetItemRequest { tableName = USERS_TABLE; this.key = key }
            val response = client.getItem(request)
            response.item?.let { item ->
                User(
                    username = item["username"]?.asS() ?: "",
                    password = item["password"]?.asS() ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user $username", e)
            null
        }
    }

    // ========== TABLE MANAGEMENT ========== 

    suspend fun createTablesIfNotExist() = withContext(Dispatchers.IO) {
        createProductsTable()
        createUsersTable()
    }

    private suspend fun createProductsTable() {
        try {
            val request = CreateTableRequest {
                tableName = PRODUCTS_TABLE
                keySchema = listOf(KeySchemaElement { attributeName = "code"; keyType = KeyType.Hash })
                attributeDefinitions = listOf(AttributeDefinition { attributeName = "code"; attributeType = ScalarAttributeType.S })
                billingMode = BillingMode.PayPerRequest
            }
            client.createTable(request)
            Log.d(TAG, "Products table created")
        } catch (e: ResourceInUseException) {
            Log.d(TAG, "Products table already exists")
        } catch (e: Exception) {
            Log.e(TAG, "Error creating products table", e)
        }
    }

    private suspend fun createUsersTable() {
        try {
            val request = CreateTableRequest {
                tableName = USERS_TABLE
                keySchema = listOf(KeySchemaElement { attributeName = "username"; keyType = KeyType.Hash })
                attributeDefinitions = listOf(AttributeDefinition { attributeName = "username"; attributeType = ScalarAttributeType.S })
                billingMode = BillingMode.PayPerRequest
            }
            client.createTable(request)
            Log.d(TAG, "Users table created")
        } catch (e: ResourceInUseException) {
            Log.d(TAG, "Users table already exists")
        } catch (e: Exception) {
            Log.e(TAG, "Error creating users table", e)
        }
    }

    suspend fun close() = withContext(Dispatchers.IO) {
        if (::client.isInitialized) {
            client.close()
        }
    }
}

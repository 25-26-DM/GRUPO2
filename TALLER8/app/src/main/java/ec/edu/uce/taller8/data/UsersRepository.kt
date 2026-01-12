package ec.edu.uce.taller8.data

interface UsersRepository {
    suspend fun insertUser(user: User)
    suspend fun getUser(username: String): User?
}
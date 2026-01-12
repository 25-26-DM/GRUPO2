package ec.edu.uce.taller8.data

class OfflineUsersRepository(private val userDao: UserDao) : UsersRepository {
    override suspend fun insertUser(user: User) = userDao.insert(user)
    override suspend fun getUser(username: String): User? = userDao.getUser(username)
}
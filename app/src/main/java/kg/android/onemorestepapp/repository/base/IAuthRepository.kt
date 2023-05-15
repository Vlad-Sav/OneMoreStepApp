package kg.android.onemorestepapp.repository.base

import kg.android.onemorestepapp.models.AuthResult

interface IAuthRepository {
    suspend fun register(username: String,
                         email: String,
                         password: String,
                         confirmPassword: String
    ): AuthResult<Unit>
    suspend fun login(email: String, password: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}
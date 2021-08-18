package com.cornershop.counterstest.domain.repository.user

import com.cornershop.counterstest.domain.models.UserModel

interface UserRepository {
    suspend fun getUserInformation(): UserModel
    suspend fun setUserInformation(userInformation: UserModel): UserModel
}
package com.cornershop.counterstest.domain.usecases.user

import com.cornershop.counterstest.data.repository.user.UserRepositoryImpl
import com.cornershop.counterstest.domain.models.UserModel
import javax.inject.Inject

class FirstTimeUseCase @Inject constructor(
    private val userRepository: UserRepositoryImpl
) {
    suspend fun invoke(): UserModel {
        return userRepository.setUserInformation(UserModel(isFirstTime = false))
    }
}
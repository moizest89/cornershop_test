package com.cornershop.counterstest.domain.usecases.user

import com.cornershop.counterstest.domain.models.UserModel
import com.cornershop.counterstest.domain.repository.user.UserRepository
import javax.inject.Inject

class FirstTimeUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun invoke(): UserModel {
        return userRepository.setUserInformation(UserModel(isFirstTime = false))
    }
}
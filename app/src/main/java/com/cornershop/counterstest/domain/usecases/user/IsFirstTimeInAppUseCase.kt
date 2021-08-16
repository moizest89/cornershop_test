package com.cornershop.counterstest.domain.usecases.user

import com.cornershop.counterstest.data.repository.user.UserRepositoryImpl
import javax.inject.Inject

class IsFirstTimeInAppUseCase @Inject constructor(
    private val userRepository: UserRepositoryImpl
) {
    suspend fun invoke() : Boolean{
        return userRepository.getUserInformation().isFirstTime
    }
}
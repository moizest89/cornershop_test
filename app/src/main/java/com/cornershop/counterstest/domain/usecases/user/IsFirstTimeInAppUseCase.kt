package com.cornershop.counterstest.domain.usecases.user

import com.cornershop.counterstest.domain.repository.user.UserRepository
import javax.inject.Inject

class IsFirstTimeInAppUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun invoke() : Boolean{
        return userRepository.getUserInformation().isFirstTime
    }
}
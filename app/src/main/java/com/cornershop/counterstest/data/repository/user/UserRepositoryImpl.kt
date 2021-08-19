package com.cornershop.counterstest.data.repository.user

import com.cornershop.counterstest.data.datasources.sharepreference.SharePreference
import com.cornershop.counterstest.data.mappers.toUser
import com.cornershop.counterstest.data.mappers.toUserModel
import com.cornershop.counterstest.data.models.db.User
import com.cornershop.counterstest.domain.models.UserModel
import com.cornershop.counterstest.domain.repository.user.UserRepository
import javax.inject.Inject

class UserRepositoryImpl constructor(
    private val sharePreference: SharePreference,
) : UserRepository {

    override suspend fun getUserInformation() = sharePreference.getUserInformation().toUserModel()
    override suspend fun setUserInformation(userInformation: UserModel) =
        sharePreference.setUserInformation(userInformation.toUser()).toUserModel()
}
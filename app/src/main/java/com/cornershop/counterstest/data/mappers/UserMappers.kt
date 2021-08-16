package com.cornershop.counterstest.data.mappers

import com.cornershop.counterstest.data.models.db.User
import com.cornershop.counterstest.domain.models.UserModel

fun User.toUserModel() : UserModel =
    UserModel(isFirstTime = this.isFirstTime)

fun UserModel.toUser() : User =
    User(isFirstTime = this.isFirstTime)
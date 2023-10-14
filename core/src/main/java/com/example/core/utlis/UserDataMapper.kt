package com.example.core.utlis

import com.example.core.data.local.UserEntity
import com.example.core.data.network.response.DetailResponse
import com.example.core.data.network.response.RepoResponseItem
import com.example.core.data.network.response.UsersResponseItem
import com.example.core.domain.entities.Repo
import com.example.core.domain.entities.User

object UserDataMapper {

    fun userResponseToDomain(response: UsersResponseItem): User =
        User(
            id = response.id,
            login = response.login,
            avatarUrl = response.avatarUrl
        )

    fun userEntityToDomain(userEntity: UserEntity): User =
        User(
            id = userEntity.id,
            login = userEntity.login,
            avatarUrl = userEntity.avatarUrl
        )

    fun userDomainToEntity(user: User): UserEntity =
        UserEntity(
            id = user.id,
            login = user.login,
            avatarUrl = user.avatarUrl
        )

    fun repoResponseToDomain(repoResponseItem: RepoResponseItem): Repo =
        Repo(
            name = repoResponseItem.name,
            visibility = repoResponseItem.visibility
        )

    fun detailResponseToDomain(detailResponse: DetailResponse): User =
        User(
            id = detailResponse.id,
            name = detailResponse.name,
            login = detailResponse.login,
            following = detailResponse.following,
            followers = detailResponse.followers,
            avatarUrl = detailResponse.avatarUrl,
            type = detailResponse.type
        )
}
package com.develop.local.preferences.filter

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.develop.local.proto.UserFilterPreferences
import java.io.InputStream
import java.io.OutputStream

object UserFilterSerializer : Serializer<UserFilterPreferences> {
    override val defaultValue: UserFilterPreferences = UserFilterPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserFilterPreferences {
        try {
            return UserFilterPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: UserFilterPreferences,
        output: OutputStream
    ) = t.writeTo(output)
}

private const val DATA_STORE_FILE_NAME = "user_filter_preferences.pb"

val Context.userFilterPreferencesStore: DataStore<UserFilterPreferences> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = UserFilterSerializer
)
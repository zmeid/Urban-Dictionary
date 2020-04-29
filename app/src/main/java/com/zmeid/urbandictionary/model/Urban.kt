package com.zmeid.urbandictionary.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.zmeid.urbandictionary.util.*

/**
 * [Urban] data class is used by Retrofit and Room. It holds variables needed to map API responses and Room operations.
 *
 * Every row has a [tag]. We need to insert words to database with a [tag]. [tag] is needed in order to query definitions and simulate api search algorithm. Word variable is not enough. For Ex: try searching "e-", "E&E" will also be in results. Searching definitions is done by searching [tag]. Since we are using [tag] to search the definitions, it is indexed. Indexes improves database read performance.
 *
 * For sound urls we have two variables. [soundUrls] is used to map API response. But we insert only one sound url to database. [soundUrl] is used to hold and insert first element of [soundUrls].
 *
 */
@Entity(indices = [Index(ROOM_WORD_TAG)])
data class Urban(
    @PrimaryKey @SerializedName(SN_DEFINITION_ID) val id: Int,
    @ColumnInfo(name = ROOM_WORD_TAG) var tag: String,
    @SerializedName(SN_WORD) val word: String,
    @SerializedName(SN_DEFINITION) val definition: String,
    @SerializedName(SN_EXAMPLE) val example: String,
    @SerializedName(SN_AUTHOR) val author: String,
    @SerializedName(SN_THUMBS_UP) val thumbsUp: Int,
    @SerializedName(SN_THUMBS_DOWN) val thumbsDown: Int,
    var soundUrl: String?
) {
    @Ignore // @Ignore is not accepted by room when used in constructor. It looks like a bug.
    @SerializedName(SN_SOUND_URLS)
    var soundUrls: List<String>? = null
}
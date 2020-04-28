package com.zmeid.urbandictionary.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.zmeid.urbandictionary.util.*

@Entity(indices = [Index("tag")])
data class Urban(
    @PrimaryKey @SerializedName(SN_DEFINITION_ID) val id: Int,
    var tag: String, // We need to insert words to database with a tag. Tag is needed in order to query definitions and simulate api search algorithm. Word variable is not enough. For Ex: try searching "e-", "E&E" will also be in results.
    @SerializedName(SN_WORD) val word: String,
    @SerializedName(SN_DEFINITION) val definition: String,
    @SerializedName(SN_EXAMPLE) val example: String,
    @SerializedName(SN_AUTHOR) val author: String,
    @SerializedName(SN_THUMBS_UP) val thumbsUp: Int,
    @SerializedName(SN_THUMBS_DOWN) val thumbsDown: Int,
    var soundUrl: String?
) {
    @Ignore // @Ignore is not accepted by room when used in constructor. It looks like a bug
    @SerializedName(SN_SOUND_URLS)
    var soundUrls: List<String>? = null
}
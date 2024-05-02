package com.caiosilva.tagsearcher.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Items(
    @SerializedName("title") var title: String? = null,
    @SerializedName("link") var link: String? = null,
    @SerializedName("media") var media: Media? = Media(),
    @SerializedName("date_taken") var dateTaken: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("published") var published: String? = null,
    @SerializedName("author") var author: String? = null,
    @SerializedName("author_id") var authorId: String? = null,
    @SerializedName("tags") var tags: String? = null
) : Parcelable

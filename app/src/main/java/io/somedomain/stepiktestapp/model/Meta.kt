package io.somedomain.stepiktestapp.model

import com.google.gson.annotations.SerializedName

class Meta(
        @SerializedName("page")
        var page: Int,
        @SerializedName("has_next")
        var hasNext: Boolean,
        @SerializedName("has_previous")
        var hasPrevious: Boolean
)
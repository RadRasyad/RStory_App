package com.danrsy.rstoryapp.data.model.upload

import com.google.gson.annotations.SerializedName

class UploadResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

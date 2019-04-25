package com.importre.example

import com.google.gson.Gson

internal val Any?.json
    get() = Gson().toJson(this)

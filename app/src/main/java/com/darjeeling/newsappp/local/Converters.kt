package com.darjeeling.newsappp.local

import androidx.room.TypeConverter
import com.darjeeling.newsappp.model.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}
package com.android.coroutines_playground.data.db.entity.mapper

interface Mapper<T, R> {
    fun map(t: T): R
}
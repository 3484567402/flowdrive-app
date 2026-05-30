package com.sakura.flowdrive.core.navigation

interface NavigationResultKey<T> {
    val key: String
        get() = this::class.java.name

    fun serialize(value: T): Any = value as Any

    @Suppress("UNCHECKED_CAST")
    fun deserialize(raw: Any): T = raw as T
}

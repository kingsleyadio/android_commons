package com.kingsleyadio.appcommons.preference

import java.lang.reflect.Type
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

@OptIn(ExperimentalStdlibApi::class)
@PublishedApi
internal inline fun <reified T : Any> javaTypeOf(): Type = typeOf<T>().javaType

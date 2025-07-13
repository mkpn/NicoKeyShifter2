package com.neesan.core

fun isRunningOnTest(): Boolean = System.getProperty("isTestEnvironment")?.toBoolean() ?: false
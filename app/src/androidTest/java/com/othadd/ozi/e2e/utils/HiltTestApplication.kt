package com.othadd.ozi.e2e.utils

import com.othadd.ozi.BaseOziApplication
import dagger.hilt.android.testing.CustomTestApplication

@CustomTestApplication(BaseOziApplication::class)
abstract class HiltTestApplication
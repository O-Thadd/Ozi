package com.iyke.ozix.e2e.utils

import com.iyke.ozix.BaseOziApplication
import dagger.hilt.android.testing.CustomTestApplication

@CustomTestApplication(BaseOziApplication::class)
abstract class HiltTestApplication
package com.samriddha.weatherforecastapp.utils

import com.google.android.gms.common.api.ApiException
import java.io.IOException

class NoInternetException:IOException()
class LocationNotGrantedException:Exception()
class EpochDateNotFoundException:Exception()
class ApiException:IOException()
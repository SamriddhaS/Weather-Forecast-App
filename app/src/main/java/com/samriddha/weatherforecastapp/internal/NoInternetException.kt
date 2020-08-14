package com.samriddha.weatherforecastapp.internal

import java.io.IOException

class NoInternetException:IOException()
class LocationNotGrantedException:Exception()
class EpochDateNotFoundException:Exception()
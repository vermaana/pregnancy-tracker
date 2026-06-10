package com.anni.pregnancytracker.domain.model

import java.time.LocalDate

data class UserProfile(val name: String, val age: Int, val lmpDate: LocalDate)

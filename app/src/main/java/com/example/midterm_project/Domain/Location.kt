package com.example.midterm_project.Domain

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Location(val latitude: Double? = null, val longitude: Double? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
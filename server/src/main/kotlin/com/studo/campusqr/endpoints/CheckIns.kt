package com.studo.campusqr.endpoints

import com.moshbit.katerbase.*
import com.studo.campusqr.common.*
import com.studo.campusqr.common.extensions.emailRegex
import com.studo.campusqr.common.extensions.emptyToNull
import com.studo.campusqr.database.*
import com.studo.campusqr.database.MainDatabase.getConfig
import com.studo.campusqr.extensions.*
import com.studo.campusqr.utils.AuthenticatedApplicationCall
import com.studo.campusqr.utils.getAuthenticatedCall
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import java.util.*
import java.text.SimpleDateFormat

/**
 * This file contains every endpoint which is used in the location management.
 */

suspend fun AuthenticatedApplicationCall.listCheckIns() {
  if (!user.canViewCheckIns) {
    respondForbidden()
    return
  }

  val checkIns = runOnDb {
    getCollection<CheckIn>()
      .find()
      .sortByDescending(CheckIn::date)
      .toList()
  }

  val locationMap = checkIns.getLocationMap()

  val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")

  respondObject(
    checkIns.map { checkIn ->
      CheckInObj(
        id = checkIn._id,
        locationId = checkIn.locationId,
        locationName = locationMap.getValue(checkIn.locationId).name,
        seat = checkIn.seat,
        checkInDate = dateFormat.format(checkIn.date.time.toDouble()),
        checkOutDate = dateFormat.format(checkIn.checkOutDate?.time?.toDouble()),
        email = checkIn.email
      )
    }
  )
}
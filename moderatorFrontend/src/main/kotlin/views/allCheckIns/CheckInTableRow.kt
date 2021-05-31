package views.allCheckIns

import app.baseUrl
import app.routeContext
import com.studo.campusqr.common.*
import kotlinx.browser.window
import react.*
import util.*
import views.locations.AddLocationProps
import views.locations.renderAddLocation
import webcore.MenuItem
import webcore.NetworkManager
import webcore.extensions.launch
import webcore.materialMenu
import webcore.materialUI.*
import webcore.mbMaterialDialog

interface CheckInTableRowProps : RProps {
  class Config(
    val checkIn: CheckInObj,
    val userData: UserData,
  ) {
    val clientUser: ClientUser get() = userData.clientUser!!
  }

  var config: Config
  var classes: CheckInTableRowClasses
}

interface CheckInTableRowState : RState {
  var showProgress: Boolean
}

class CheckInTableRow : RComponent<CheckInTableRowProps, CheckInTableRowState>() {

  override fun CheckInTableRowState.init() {
    showProgress = false
  }

  override fun RBuilder.render() {
    mTableRow {
      mTableCell {
        +props.config.checkIn.locationName
      }
      mTableCell {
        +props.config.checkIn.checkInDate.toString()
      }
      mTableCell {
        +props.config.checkIn.checkOutDate.toString()
      }
      mTableCell {
        +props.config.checkIn.email
      }
      mTableCell {
        +props.config.checkIn.seat.toString()
      }
    }
  }
}

interface CheckInTableRowClasses

private val style = { _: dynamic ->
}

private val styled = withStyles<CheckInTableRowProps, CheckInTableRow>(style)

fun RBuilder.renderCheckInTableRow(config: CheckInTableRowProps.Config) = styled {
  attrs.config = config
}
  
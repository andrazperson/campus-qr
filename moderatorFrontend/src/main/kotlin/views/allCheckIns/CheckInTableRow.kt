package views.allCheckIns

import com.studo.campusqr.common.*
import react.*
import webcore.materialUI.*

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
        +props.config.checkIn.seat.toString()
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
  
package views.allCheckIns

import com.studo.campusqr.common.*
import kotlinext.js.js
import kotlinx.browser.window
import views.locations.AddLocationProps.Config
import views.locations.renderAddLocation
import react.*
import util.Strings
import util.apiBase
import util.get
import views.common.*
import views.locations.locationsOverview.LocationTableRowProps
import views.locations.locationsOverview.renderLocationTableRow
import webcore.*
import webcore.extensions.launch
import webcore.materialUI.*

interface ListLocationsProps : RProps {
  var classes: ListLocationsClasses
  var userData: UserData
}

interface ListLocationsState : RState {
  var locationList: List<CheckInObj>?
  var showAddLocationDialog: Boolean
  var showImportLocationDialog: Boolean
  var loadingLocationList: Boolean
  var snackbarText: String
}

class ListLocations : RComponent<ListLocationsProps, ListLocationsState>() {

  override fun ListLocationsState.init() {
    locationList = null
    showAddLocationDialog = false
    showImportLocationDialog = false
    loadingLocationList = false
    snackbarText = ""
  }

  private fun fetchCheckInList() = launch {
    setState { loadingLocationList = true }
    val response = NetworkManager.get<Array<CheckInObj>>("$apiBase/checkIns/listCheckIns")
    setState {
      locationList = response?.toList()
      loadingLocationList = false
    }
  }

  override fun componentDidMount() {
    fetchCheckInList()
  }

  override fun RBuilder.render() {
    renderLinearProgress(state.loadingLocationList)

    if (state.locationList?.isNotEmpty() == true) {
      mTable {
        mTableHead {
          mTableRow {
            mTableCell { +Strings.all_check_ins_location.get() }
            mTableCell { +Strings.all_check_ins_in_date.get() }
            mTableCell { +Strings.all_check_ins_out_date.get() }
            mTableCell { +Strings.all_check_ins_email.get() }
            mTableCell { +Strings.all_check_ins_seat.get() }
          }
        }
        mTableBody {
          state.locationList!!.forEach { checkIn ->
            renderCheckInTableRow(
              CheckInTableRowProps.Config(
                checkIn,
                userData = props.userData,
              )
            )
          }
        }
      }
    } else if (state.locationList == null && !state.loadingLocationList) {
      networkErrorView()
    } else if (!state.loadingLocationList) {
      genericErrorView(
        Strings.location_no_locations_title.get(),
        Strings.location_no_locations_subtitle.get()
      )
    }
  }
}

interface ListLocationsClasses

private val style = { _: dynamic ->
  js {
  }
}

private val styled = withStyles<ListLocationsProps, ListLocations>(style)

fun RBuilder.renderAllCheckIns(userData: UserData) = styled {
  // Set component attrs here
  attrs.userData = userData
}
  
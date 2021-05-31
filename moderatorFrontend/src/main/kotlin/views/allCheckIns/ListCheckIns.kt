package views.allCheckIns

import com.studo.campusqr.common.*
import kotlinext.js.js
import kotlinx.browser.window
import react.*
import util.Strings
import util.apiBase
import util.get
import views.common.*
import webcore.*
import webcore.extensions.launch
import webcore.materialUI.*

interface ListCheckInsProps : RProps {
  var classes: ListCheckInsClasses
  var userData: UserData
}

interface ListCheckInsState : RState {
  var checkInsList: List<CheckInObj>?
  var loadingCheckInsList: Boolean
}

class ListCheckIns : RComponent<ListCheckInsProps, ListCheckInsState>() {

  override fun ListCheckInsState.init() {
    checkInsList = null
    loadingCheckInsList = false
  }

  private fun fetchCheckInList() = launch {
    setState { loadingCheckInsList = true }
    val response = NetworkManager.get<Array<CheckInObj>>("$apiBase/checkIns/listCheckIns")
    setState {
      checkInsList = response?.toList()
      loadingCheckInsList = false
    }
  }

  override fun componentDidMount() {
    fetchCheckInList()
  }

  override fun RBuilder.render() {
    renderToolbarView(
      ToolbarViewProps.Config(
        title = Strings.all_check_ins.get(),
      )
    )
    renderLinearProgress(state.loadingCheckInsList)

    if (state.checkInsList?.isNotEmpty() == true) {
      mTable {
        mTableHead {
          mTableRow {
            mTableCell { +Strings.all_check_ins_location.get() }
            mTableCell { +Strings.all_check_ins_seat.get() }
            mTableCell { +Strings.all_check_ins_in_date.get() }
            mTableCell { +Strings.all_check_ins_out_date.get() }
            mTableCell { +Strings.all_check_ins_email.get() }
          }
        }
        mTableBody {
          state.checkInsList!!.forEach { checkIn ->
            renderCheckInTableRow(
              CheckInTableRowProps.Config(
                checkIn,
                userData = props.userData,
              )
            )
          }
        }
      }
    } else if (state.checkInsList == null && !state.loadingCheckInsList) {
      networkErrorView()
    } else if (!state.loadingCheckInsList) {
      genericErrorView(
        Strings.all_check_ins_error_title.get(),
        ""
      )
    }
  }
}

interface ListCheckInsClasses

private val style = { _: dynamic ->
  js {
  }
}

private val styled = withStyles<ListCheckInsProps, ListCheckIns>(style)

fun RBuilder.renderAllCheckIns(userData: UserData) = styled {
  // Set component attrs here
  attrs.userData = userData
}
  
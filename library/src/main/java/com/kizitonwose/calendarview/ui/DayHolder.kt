package com.kizitonwose.calendarview.ui

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.Px
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.gridlayout.widget.GridLayout
import androidx.gridlayout.widget.GridLayout.UNDEFINED
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.utils.inflate

internal data class DayConfig(
    @Px val width: Int,
    @Px val height: Int,
    @LayoutRes val dayViewRes: Int,
    val viewBinder: DayBinder<ViewContainer>
)

internal class DayHolder(private val config: DayConfig) {

    private lateinit var dateView: View
    private lateinit var viewContainer: ViewContainer
    private var day: CalendarDay? = null

    fun inflateDayView(parent: ViewGroup): View {
        dateView = parent.inflate(config.dayViewRes).apply {
            updateLayoutParams<GridLayout.LayoutParams> {
                // if this isn't set when the gridlayout width is set to match parent the views will not fill
                columnSpec = GridLayout.spec(UNDEFINED, 1f)
            }
        }
        return dateView
    }

    fun bindDayView(currentDay: CalendarDay?) {
        this.day = currentDay
        if (!::viewContainer.isInitialized) {
            viewContainer = config.viewBinder.create(dateView)
        }

        val dayHash = currentDay?.date.hashCode()
        if (viewContainer.view.tag != dayHash) {
            viewContainer.view.tag = dayHash
        }

        if (currentDay != null) {
            if (!viewContainer.view.isVisible) {
                viewContainer.view.isVisible = true
            }
            config.viewBinder.bind(viewContainer, currentDay)
        } else if (!viewContainer.view.isGone) {
            viewContainer.view.isGone = true
        }
    }

    fun reloadViewIfNecessary(day: CalendarDay): Boolean {
        return if (day == this.day) {
            bindDayView(this.day)
            true
        } else {
            false
        }
    }
}

package com.kizitonwose.calendarview.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth

internal class MonthViewHolder(
    adapter: CalendarAdapter,
    rootLayout: ViewGroup,
    private val dayHolders: List<DayHolder>,
    private var monthHeaderBinder: MonthHeaderFooterBinder<ViewContainer>?,
    private var monthFooterBinder: MonthHeaderFooterBinder<ViewContainer>?
) : RecyclerView.ViewHolder(rootLayout) {

    val headerView: View? = rootLayout.findViewById(adapter.headerViewId)
    val footerView: View? = rootLayout.findViewById(adapter.footerViewId)

    private var headerContainer: ViewContainer? = null
    private var footerContainer: ViewContainer? = null

    lateinit var month: CalendarMonth

    fun bindMonth(month: CalendarMonth) {
        this.month = month
        headerView?.let { view ->
            val headerContainer = headerContainer ?: monthHeaderBinder!!.create(view).also {
                headerContainer = it
            }
            monthHeaderBinder?.bind(headerContainer, month)
        }
        footerView?.let { view ->
            val footerContainer = footerContainer ?: monthFooterBinder!!.create(view).also {
                footerContainer = it
            }
            monthFooterBinder?.bind(footerContainer, month)
        }

        dayHolders.forEachIndexed { dayIndex, holder ->
            // Indices can be null if OutDateStyle is NONE. We set the
            // visibility for the views at these indices to INVISIBLE.
            holder.bindDayView(month.weekDays.flatten().getOrNull(dayIndex))
        }
    }

    fun reloadDay(day: CalendarDay) {
        dayHolders.find { it.reloadViewIfNecessary(day) }
    }
}

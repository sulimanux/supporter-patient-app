package com.app.sanad.users.patient.calender.presentaion

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.app.sanad.util.log

/**
 * A decorator for the MaterialCalendarView that highlights specific dates.
 *
 * Responsibilities:
 *  - Highlights the current day and any days contained in the provided `dates` set.
 *  - Changes the text color of decorated days (current day in green, others in red).
 *  - Can be extended to include custom background drawables for decorated days.
 *
 * Usage:
 *  - Pass a HashSet of [CalendarDay] representing days with tasks or events.
 *  - Attach the decorator to a MaterialCalendarView to visually distinguish these days.
 *
 */
class TaskDecorator(private val dates: HashSet<CalendarDay>) : DayViewDecorator {

    private val currentDay = CalendarDay.today()
    private var _day: CalendarDay? = null

    /**
     * Determines whether a given day should be decorated.
     */
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        _day = day
        return day == currentDay || dates.contains(day)
    }

    /**
     * Applies the decoration to the day view.
     */
    override fun decorate(view: DayViewFacade?) {
        view?.let {
            it.addSpan(ForegroundColorSpan(Color.RED))
            _day?.let { day ->
                if (day == currentDay) {
                    view.addSpan(ForegroundColorSpan(Color.GREEN))
                }
            }
        }
    }


}

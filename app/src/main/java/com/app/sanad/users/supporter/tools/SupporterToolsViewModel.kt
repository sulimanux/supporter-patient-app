package com.app.sanad.users.supporter.tools

import androidx.lifecycle.ViewModel
import com.app.sanad.model.Step

/**
 * ViewModel to manage the state of supporter tools,
 * specifically for step-by-step processes or plans.
 * Holds the current list of steps and the current index within that list.
 */
class SupporterToolsViewModel : ViewModel() {

    // Tracks the current position in the list of steps
    private var currentIndex = 0

    // Stores the list of steps that supporter is viewing or managing
    private var currentList = emptyList<Step>()

    /**
     * Updates the current index.
     * @param index The new position in the list of steps.
     */
    fun setCurrentIndex(index: Int) {
        currentIndex = index
    }

    /**
     * Returns the current index.
     * @return Int current position in the list
     */
    fun getCurrentIndex() = currentIndex

    /**
     * Returns the current list of steps.
     * @return List<Step> current steps list
     */
    fun getCurrentList() = currentList

    /**
     * Updates the current list of steps.
     * @param list The new list of steps to be stored.
     */
    fun setCurrentList(list: List<Step>) {
        currentList = list
    }
}

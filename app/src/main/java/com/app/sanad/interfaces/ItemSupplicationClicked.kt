package com.app.sanad.interfaces

import android.view.View
import com.app.sanad.model.Supplication

interface ItemSupplicationClicked {

    fun onItemClicked(view: View, supplication: Supplication)
    fun onEditClicked(supplication: Supplication)
    fun onDeleteClicked(supplication: Supplication)
}
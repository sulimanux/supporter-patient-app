package com.app.sanad.users.supporter.tools.cofe.presintaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.sanad.R
import com.app.sanad.util.log


class ExitCofeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_exit_cofe2, container, false)
        view.findViewById<TextView>(R.id.exit)?.setOnClickListener {
            log("dfsd ")
            activity?.finish()
        }
        return view

    }

}
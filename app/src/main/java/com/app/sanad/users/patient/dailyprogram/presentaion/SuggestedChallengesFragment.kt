/**
 * Bottom sheet that displays suggested daily program challenges.
 * Allows user to choose an alternative task suggestion and returns selection.
 */
package com.app.sanad.users.patient.dailyprogram.presentaion

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import com.app.sanad.R
import com.app.sanad.base.BaseBottomSheetDialogFragment
import com.app.sanad.databinding.FragmentSuggestedChallengesBinding
import com.app.sanad.model.TestD
import com.app.sanad.users.patient.dailyprogram.data.entity.Task
import com.app.sanad.util.ENGLISH_KEY
import com.app.sanad.util.LANGUAGE
import kotlin.properties.Delegates

class SuggestedChallengesFragment : BaseBottomSheetDialogFragment() {

    private lateinit var adapter: SuggestedChallengesAdapter
    val viewModel: DailyProgramViewModel by viewModels()

    interface OnTaskItemClickListener {
        fun onTaskItemClicked(position: Int)
    }

    private lateinit var binding: FragmentSuggestedChallengesBinding

    private var tasks: List<Task> = emptyList()
    private var currentIndex by Delegates.notNull<Int>()
    private var lang = ""
    private var _itemClickListener: OnTaskItemClickListener? = null

    /**
     *  Inflates layout and initializes view actions.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSuggestedChallengesBinding.inflate(inflater, container, false)
        setupClickListener()
        initializeViews()
        return binding.root
    }

    /**
     * Loads data and adjusts UI based on app language.
     */
    private fun initializeViews() {
        loadTasksFromArguments()
        changeButtonBackColorBasedLang()
    }

    /**
     * Retrieves task list and state from arguments bundle.
     */
    private fun loadTasksFromArguments() {
        arguments?.let {
            tasks = it.getParcelableArrayList(ARG_TASKS) ?: emptyList()
            currentIndex = it.getInt(ARG_CURRENT_INDEX)
            lang = it.getString(ARG_LANG)!!
            adapter = SuggestedChallengesAdapter(tasks, currentIndex, lang)
            binding.recycler.adapter = adapter

            val previewTasks = mutableListOf<TestD>()
            for (index in tasks.indices) {
                if (index != currentIndex) previewTasks.add(TestD(index, tasks[index].image!!))
            }
        }
    }

    /**
     * Sets button click actions for confirm and close.
     */
    private fun setupClickListener() {
        binding.buttonConfirm.setOnClickListener {
            onItemClick(adapter.getSelectedPosition())
            dismiss()
        }
        binding.close.setOnClickListener { dismiss() }
    }

    /**
     * Emits selected task index to listener.
     */
    private fun onItemClick(position: Int) {
        _itemClickListener?.onTaskItemClicked(position)
        dismiss()
    }

    /**
     * Updates UI layout for RTL languages.
     */
    private fun changeButtonBackColorBasedLang() {
        if (viewModel.sharedPreferences.getString(LANGUAGE) != ENGLISH_KEY) {
            binding.close.setBackgroundDrawable(resources.getDrawable(R.drawable.background_back_right))
            binding.root.setBackgroundDrawable(resources.getDrawable(R.drawable.corner_top_lift))
        }
    }

    companion object {
        private const val ARG_TASKS = "tasks"
        private const val ARG_CURRENT_INDEX = "currentIndex"
        private const val ARG_LANG = "lang"

        fun newInstance(
            itemClickListener: OnTaskItemClickListener,
            currentIndex: Int,
            tasks: List<Task>,
            lang: String,
        ) = SuggestedChallengesFragment().apply {
            _itemClickListener = itemClickListener
            arguments = Bundle().apply {
                putInt(ARG_CURRENT_INDEX, currentIndex)
                putString(ARG_LANG, lang)
                putParcelableArrayList(ARG_TASKS, ArrayList(tasks))
            }
        }
    }
}

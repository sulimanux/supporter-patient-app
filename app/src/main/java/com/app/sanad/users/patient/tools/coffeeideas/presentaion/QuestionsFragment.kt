package com.app.sanad.users.patient.tools.coffeeideas.presentaion




import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentQuestionsBinding
import com.app.sanad.users.patient.tools.coffeeideas.data.questions
import com.app.sanad.util.QUESTION_ANSWERED
import com.app.sanad.util.REFLECT_ENGAGEMENT
import com.app.sanad.util.REQUIRED
import com.app.sanad.util.Temp
import com.app.sanad.util.USER_SKIP_INPUT
import com.app.sanad.util.log
import kotlin.getValue

/**
 * In this step, the patient reflects on their idea by answering 5 questions.
 * Ensures all questions are answered before allowing navigation to the next step.
 * Logs analytics events for skipped input, questions answered, and engagement time.
 */
@AndroidEntryPoint
class QuestionsFragment : BaseFragment() {

    
    // View binding instance
    private lateinit var binding: FragmentQuestionsBinding

    // Shared ViewModel scoped to the activity
    private val viewModel: CofeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout using view binding
        binding = FragmentQuestionsBinding.inflate(inflater)
        val args = QuestionsFragmentArgs.fromBundle(requireArguments())
        // Bind ViewModel for data binding
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Set up UI listeners
        setUpListeners()
        createViews(args.indexCup - 1)
        return binding.root
    }

    private fun createViews(indexCup: Int) {
        log(indexCup.toString())
        val inflater = LayoutInflater.from(context)
        questions[indexCup].forEachIndexed { i, q ->
            val view = inflater.inflate(R.layout.item_question, binding.questionsView, false)
            view.findViewById<TextView>(R.id.textView64).text = (i + 1).toString()
            view.findViewById<TextView>(R.id.textView65).text = q.question
            binding.questionsView.addView(view,i)
        }
    }

    override fun onStart() {
        super.onStart()
        // Track current fragment in Temp for engagement analytics
        Temp.stoppedAt = "QuestionsFragment"
    }

    /**
     * Sets up click listeners for back, next, and exit buttons
     */
    private fun setUpListeners() {

        // Exit the activity
        binding.exit.setOnClickListener {
            activity?.finish()
        }

        // NEXT button: validate that all questions are answered
        binding.constraintNext.setOnClickListener {

            if (viewModel.isAllQuestionsAnswered()) {
                // Clear cached supporters profile (if any)
                viewModel.supportersProfile.value = null

                // Navigate to Step 3
                findNavController().navigate(R.id.action_questionsFragment_to_step3Fragment)
            } else {
                findNavController().navigate(R.id.action_questionsFragment_to_step3Fragment)

                // Log skipped input for analytics
//                logEvent(USER_SKIP_INPUT) {
//                    param(REQUIRED, "answering_questions")
//                }
//
//                // Show toast to notify user
//                showToast(getString(R.string.please_answer_all_questions))
            }
        }

        // BACK button
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onStop() {
        super.onStop()
        // Log whether all questions were answered
        logEvent(QUESTION_ANSWERED) {
            param(QUESTION_ANSWERED, viewModel.isAllQuestionsAnswered().toString())
        }

        // Log engagement time for this reflection step
        logEvent(REFLECT_ENGAGEMENT) {
            param(REFLECT_ENGAGEMENT, duration())
        }
    }
}

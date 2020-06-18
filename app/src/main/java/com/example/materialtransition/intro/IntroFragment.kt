package com.example.materialtransition.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.materialtransition.R
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.fragment_intro.*

abstract class IntroFragment : Fragment() {

    abstract val color: Int

    abstract val description: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val transition = MaterialContainerTransform().apply {
            isDrawDebugEnabled = true
            duration = 3000L
        }

        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition

        exitTransition = Hold()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_intro, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(NEXT_BUTTON_TRANSITION_NAME_ARGS_KEY)?.let {
            ViewCompat.setTransitionName(nextButton, it)
        }

        initViews()
    }

    private fun initViews() {
        descriptionText.text = description

        val colorValue = ContextCompat.getColor(requireContext(), color)
        nextButton.setBackgroundColor(colorValue)

        nextButton.setOnClickListener { advancePage() }
    }

    private fun advancePage() {
        val navController = findNavController()

        val destination = if (navController.currentDestination?.id == R.id.secondStepIntroFragment)
            R.id.firstStepIntroFragment else R.id.secondStepIntroFragment // This is just to make a "dynamic" destination as in the actual app

        val nextButtonTransitionName = "transitionButton(${destination})"
        ViewCompat.setTransitionName(nextButton, nextButtonTransitionName)

        val args = Bundle()
        args.putString(NEXT_BUTTON_TRANSITION_NAME_ARGS_KEY, nextButtonTransitionName)

        val extras = FragmentNavigatorExtras(
            nextButton to nextButtonTransitionName
        )

        navController.navigate(destination, args, null, extras)
    }

    companion object {

        protected const val NEXT_BUTTON_TRANSITION_NAME_ARGS_KEY = "next_button_transition_name"
    }
}
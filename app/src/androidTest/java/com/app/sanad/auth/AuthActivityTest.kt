package com.app.sanad.auth

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Before
import org.junit.Test
import com.app.sanad.R
import com.app.sanad.auth.presentation.AuthActivity

class AuthActivityTest{


    private lateinit var scenario: ActivityScenario<AuthActivity>


    @Before
    fun setup(){
        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.STARTED)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun openSignUp(){
        onView(withId(R.id.edit_email)).perform(typeText("muhammednashat35@gmail.com"))
        onView(withId(R.id.passwordEditText)).perform(typeText("123456"))
     Espresso.closeSoftKeyboard()
        onView(withId(R.id.log_in)).perform(click())


    }

}
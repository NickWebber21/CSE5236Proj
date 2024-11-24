package com.example.schedulemaster

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.Intents.intended
import com.example.schedulemaster.ui.activity.WelcomeActivity
import com.example.schedulemaster.ui.activity.LoginActivity
import com.example.schedulemaster.ui.activity.CreateAccountActivity
import org.junit.After
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class WelcomePageUITest {
    //pre-testing env setup
    @Before
    fun setUp() {
        Intents.init()
    }
    //post-testing env teardown
    @After
    fun tearDown() {
        Intents.release()
    }
    //test login button
    @Test
    fun testLoginButton() {
        ActivityScenario.launch(WelcomeActivity::class.java)
        onView(withId(R.id.loginButton)).perform(click())
        intended(hasComponent(LoginActivity::class.java.name))
    }
    //test create account
    @Test
    fun testLogoutButton() {
        ActivityScenario.launch(WelcomeActivity::class.java)
        onView(withId(R.id.createAccountButton)).perform(click())
        intended(hasComponent(CreateAccountActivity::class.java.name))
    }
}

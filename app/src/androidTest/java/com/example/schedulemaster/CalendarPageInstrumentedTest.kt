package com.example.schedulemaster

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.schedulemaster.ui.activity.CalendarActivity
import com.example.schedulemaster.ui.activity.AddTaskActivity
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.Intents.intended
import com.example.schedulemaster.ui.activity.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import junit.framework.TestCase.fail
import org.junit.After
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class CalendarPageUITest {
    //pre-testing env setup
    @Before
    fun setUp() {
        Intents.init()
        //setup dummy firebase login
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            auth.signInWithEmailAndPassword("sypagoji@clip.lat", "TestingPassword1!")
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        fail("Failed to sign into account.")
                    }
                }
        }
    }
    //post-testing env teardown
    @After
    fun tearDown() {
        Intents.release()
        FirebaseAuth.getInstance().signOut()
    }
    //test add task button
    @Test
    fun testAddTaskButton() {
        ActivityScenario.launch(CalendarActivity::class.java)
        onView(withId(R.id.AddTaskButton)).perform(click())
        intended(hasComponent(AddTaskActivity::class.java.name))
    }
    //test logout button
    @Test
    fun testLogoutButton() {
        ActivityScenario.launch(CalendarActivity::class.java)
        onView(withId(R.id.logoutButton)).perform(click())
        FirebaseAuth.getInstance().currentUser?.let {
            fail("User still logged into account.")
        }
        intended(hasComponent(WelcomeActivity::class.java.name))
    }
}

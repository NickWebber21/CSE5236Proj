import org.junit.Test
import org.junit.Assert.*

//This is our current workaround solution for unit testing.
//We could not get mocks working, so this is a test double.
//Our test double is just the function we are testing rewritten in here.
class TestApplication {
    fun validateInputs(
        title: String, date: String, time: String, description: String, locationText: String): Result<Map<String, String>> {
        val inputs = mapOf(
            "title" to title,
            "date" to date,
            "time" to time,
            "description" to description,
            "locationText" to locationText
        )

        for ((key, value) in inputs) {
            if (value.isEmpty()) {
                return Result.failure(Exception("$key is required"))
            }
        }

        return Result.success(inputs)
    }
}

class InputValidationTest {
    @Test
    fun validateInputsSuccess() {
        val testApp = TestApplication()
        val result = testApp.validateInputs(
            "Title", "30/11/2024", "14:00", "Description", "Location"
        )
        assertTrue(result.isSuccess)
    }

    @Test
    fun validateInputsFailure_EmptyTitle() {
        val testApp = TestApplication()
        val result = testApp.validateInputs(
            "", "30/11/2024", "14:00", "Description", "Location"
        )
        assertTrue(result.isFailure)
    }

    @Test
    fun validateInputsFailure_EmptyDate() {
        val testApp = TestApplication()
        val result = testApp.validateInputs(
            "Title", "", "14:00", "Description", "Location"
        )
        assertTrue(result.isFailure)
    }

    @Test
    fun validateInputsFailure_EmptyTime() {
        val testApp = TestApplication()
        val result = testApp.validateInputs(
            "Title", "30/11/2024", "", "Description", "Location"
        )
        assertTrue(result.isFailure)
    }

    @Test
    fun validateInputsFailure_EmptyDescription() {
        val testApp = TestApplication()
        val result = testApp.validateInputs(
            "Title", "30/11/2024", "14:00", "", "Location"
        )
        assertTrue(result.isFailure)
    }

    @Test
    fun validateInputsFailure_EmptyLocation() {
        val testApp = TestApplication()
        val result = testApp.validateInputs(
            "Title", "30/11/2024", "14:00", "Description", ""
        )
        assertTrue(result.isFailure)
    }
}
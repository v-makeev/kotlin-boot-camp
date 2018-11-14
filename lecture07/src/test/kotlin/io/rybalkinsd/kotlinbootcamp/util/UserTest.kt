package io.rybalkinsd.kotlinbootcamp.util

import org.junit.Assert.*
import org.junit.Test

class UserTest {

    @Test
    fun `check name`() {
        assertEquals("Alice", User().name)
    }

}
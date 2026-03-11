package com.vam.coroutinesflowshomework.homework1

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith


class PrimeNumberTest {

    private lateinit var testDispatchers: TestDispatchers

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        testDispatchers = TestDispatchers()
        Dispatchers.setMain(testDispatchers.testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun reset(){
        Dispatchers.resetMain()
    }

    @Test
    fun testPrimeNumber() = runTest(testDispatchers.testDispatcher) {
        assertThat( findPrimeNumberAtIndex(1, testDispatchers.testDispatcher)).isEqualTo(2)
        assertThat( findPrimeNumberAtIndex(2, testDispatchers.testDispatcher)).isEqualTo(3)
        assertThat( findPrimeNumberAtIndex(3, testDispatchers.testDispatcher)).isEqualTo(5)
        assertThat( findPrimeNumberAtIndex(4, testDispatchers.testDispatcher)).isEqualTo(7)
        assertThat( findPrimeNumberAtIndex(5, testDispatchers.testDispatcher)).isEqualTo(11)
        assertThat( findPrimeNumberAtIndex(6, testDispatchers.testDispatcher)).isEqualTo(13)
    }

    @Test
    fun testPrimeNumberZero() = runTest(testDispatchers.testDispatcher) {
        assertFailsWith<IllegalArgumentException> {
            findPrimeNumberAtIndex(0, testDispatchers.testDispatcher)
        }
    }



}
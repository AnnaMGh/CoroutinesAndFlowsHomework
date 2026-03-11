@file:OptIn(ExperimentalCoroutinesApi::class)

package com.vam.coroutinesflowshomework.homework2

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.vam.coroutinesflowshomework.homework1.TestDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private lateinit var testDispatchers: TestDispatchers

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        testDispatchers = TestDispatchers()
        Dispatchers.setMain(testDispatchers.testDispatcher)
        viewModel = SearchViewModel(testDispatchers)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun reset() {
        Dispatchers.resetMain()
    }

    @Test
    fun `empty query shows validation error`() = runTest {
        viewModel.state.test {
            viewModel.onSearchQueryChange("")
            advanceTimeBy(301) // debounce
            var result = awaitItem()
            assertThat(result.query).isEqualTo("")
            result = awaitItem()
            assertThat(result.searchResults.isEmpty()).isTrue()
            assertThat(result.isLoading).isFalse()
            assertThat(result.validationMessages).isEqualTo("Input cannot be empty.")

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `digit in query shows validation error`() = runTest {
        viewModel.state.test {
            var result = awaitItem()
            assertThat(result.searchResults).isEmpty()

            viewModel.onSearchQueryChange("1")
            advanceTimeBy(301) // debounce
            result = awaitItem()
            assertThat(result.query).isEqualTo("1")
            result = awaitItem()
            assertThat(result.isLoading).isTrue()
            assertThat(result.validationMessages).isEqualTo("Input cannot contain a digit")


            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `valid query returns matching results`() = runTest { viewModel.state.test {
        var result = awaitItem()
        assertThat(result.searchResults).isEmpty()

        viewModel.onSearchQueryChange("Max")
        advanceTimeBy(301) // debounce
        runCurrent() // enter async
        advanceTimeBy(601) // async
        result = awaitItem()
        assertThat(result.query).isEqualTo("Max")
        result = awaitItem()
        assertThat(result.isLoading).isTrue()
        result = awaitItem()
        assertThat(result.searchResults[0]).isEqualTo("Max")
        assertThat(result.isLoading).isFalse()

        cancelAndConsumeRemainingEvents()
    }}

    @Test
    fun `valid query no duplicate`() = runTest { viewModel.state.test {
        var result = awaitItem()
        assertThat(result.searchResults).isEmpty()

        viewModel.onSearchQueryChange("Max")
        advanceTimeBy(301) // debounce
        runCurrent() // enter async
        advanceTimeBy(601) // async
        result = awaitItem()
        assertThat(result.query).isEqualTo("Max")
        result = awaitItem()
        assertThat(result.isLoading).isTrue()
        result = awaitItem()
        assertThat(result.searchResults.size).isEqualTo(1)
        assertThat(result.isLoading).isFalse()

        cancelAndConsumeRemainingEvents()
    }}
}
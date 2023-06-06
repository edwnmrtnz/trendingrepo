package com.edwnmrtnz.trendingrepo.ui

import com.edwnmrtnz.trendingrepo.GithubRepoTestData
import com.edwnmrtnz.trendingrepo.StatefulPresenter
import com.edwnmrtnz.trendingrepo.TestView
import com.edwnmrtnz.trendingrepo.core.domain.FetchTrendingGithubReposUseCase
import com.edwnmrtnz.trendingrepo.core.domain.exceptions.TrendyHttpErrorException
import com.github.amaterasu.localtest.CoroutineTestRule
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainPresenterTest {

    internal class FakeView : MainScreenView, TestView<MainUiState>()
    private lateinit var presenter: MainPresenter
    private lateinit var view: FakeView

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var fetchTrendingGithubReposUseCase: FetchTrendingGithubReposUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        StatefulPresenter.setScope(coroutineTestRule.scope)
        presenter = MainPresenter(fetchTrendingGithubReposUseCase)
        view = FakeView()
    }

    @Test
    fun `initial state must be loading`() = runBlocking {
        presenter.bind(view)

        Truth.assertThat(view.first().isLoading).isTrue()
    }

    @Test
    fun `when fetching repos done, flip loading state`() = runBlocking {
        Mockito.`when`(fetchTrendingGithubReposUseCase.execute(Unit))
            .thenReturn(listOf(SAMPLE_REPO))

        presenter.bind(view)

        Truth.assertThat(view.state().isLoading).isFalse()
    }

    @Test
    fun `when fetching failed, flip loading state and reflect error`() = runBlocking {
        Mockito.`when`(fetchTrendingGithubReposUseCase.execute(Unit))
            .then {
                throw TrendyHttpErrorException(404, "Not Found", Exception("Not Found"))
            }

        presenter.bind(view)

        Truth.assertThat(view.state().isLoading).isFalse()
        Truth.assertThat(view.state().loadError).isNotNull()
    }

    companion object {
        private val SAMPLE_REPO = GithubRepoTestData.build()
    }
}
package uz.coderqwerty.taskwithgooglemap.assembly.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigationDispatcher @Inject constructor() : AppNavigator, NavigationHandler {
    override val screenState = MutableSharedFlow<NavigationArgs>()

    private suspend fun navigation(args: NavigationArgs) {
        screenState.emit(args)
    }

    override suspend fun back() = navigation {
        pop()
    }

    override suspend fun push(screen: AppScreen) = navigation { push(screen) }

    override suspend fun popUntil(screen: AppScreen) = navigation { popUntil { it == screen } }

    override suspend fun replace(screen: AppScreen) = navigation { replace(screen) }

    override suspend fun replaceAll(screen: AppScreen) = navigation {
        val result = replaceAll(screen)
    }

    override suspend fun backToRoot() = navigation {
        popUntilRoot()
    }

    override suspend fun lastItemOrNull(): AppScreen? {
        var lastScreen: AppScreen? = null
        navigation {
            lastScreen = this.lastItemOrNull
        }

        return lastScreen
    }

}
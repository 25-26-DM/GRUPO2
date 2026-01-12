package ec.edu.uce.taller8.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ec.edu.uce.taller8.ui.AppViewModelProvider
import ec.edu.uce.taller8.ui.home.HomeDestination
import ec.edu.uce.taller8.ui.home.HomeScreen
import ec.edu.uce.taller8.ui.item.ItemDetailsDestination
import ec.edu.uce.taller8.ui.item.ItemDetailsScreen
import ec.edu.uce.taller8.ui.item.ItemEditDestination
import ec.edu.uce.taller8.ui.item.ItemEntryDestination
import ec.edu.uce.taller8.ui.item.ItemEntryScreen
import ec.edu.uce.taller8.ui.login.LoginDestination
import ec.edu.uce.taller8.ui.login.LoginScreen
import ec.edu.uce.taller8.ui.login.LoginViewModel

@Composable
fun InventoryNavHost(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val navController: NavHostController = rememberNavController()
    val activeUser by loginViewModel.activeUser.collectAsState()
    val loginResult by loginViewModel.loginResult.collectAsState()

    LaunchedEffect(loginResult) {
        if (loginResult?.success == true) {
            navController.navigate(HomeDestination.route) { 
                popUpTo(LoginDestination.route) { inclusive = true } 
            }
            loginViewModel.onLoginResultConsumed()
        }
    }

    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {
        composable(route = LoginDestination.route) {
            LoginScreen(loginViewModel)
        }
        composable(route = HomeDestination.route) {
            activeUser?.let { user ->
                HomeScreen(
                    userId = user.id,
                    navigateToItemEntry = { navController.navigate(ItemEntryDestination.route) },
                    navigateToItemUpdate = { navController.navigate("${ItemDetailsDestination.route}/$it") }
                )
            }
        }
        composable(route = ItemEntryDestination.route) {
            activeUser?.let { user ->
                ItemEntryScreen(
                    userId = user.id,
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = ItemDetailsDestination.arguments
        ) {
             activeUser?.let { user ->
                ItemDetailsScreen(
                    userId = user.id,
                    navigateToEditItem = { navController.navigate("${ItemEditDestination.route}/$it") },
                    navigateBack = { navController.navigateUp() }
                )
            }
        }
        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = ItemEditDestination.arguments
        ) {
            activeUser?.let { user ->
                ItemEntryScreen(
                    userId = user.id,
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}
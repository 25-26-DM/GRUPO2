package ec.edu.uce.taller8.ui.home

import ec.edu.uce.taller8.R
import ec.edu.uce.taller8.ui.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name // We'll use the app name for the title
}
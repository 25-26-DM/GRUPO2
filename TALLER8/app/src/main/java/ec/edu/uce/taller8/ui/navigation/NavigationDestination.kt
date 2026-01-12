package ec.edu.uce.taller8.ui.navigation

/**
 * Interface to describe the navigation destinations for the app
 */
interface NavigationDestination {
    /**
     * Defines a specific path for each destination
     */
    val route: String

    /**
     * Defines a string resource ID for each destination
     */
    val titleRes: Int
}
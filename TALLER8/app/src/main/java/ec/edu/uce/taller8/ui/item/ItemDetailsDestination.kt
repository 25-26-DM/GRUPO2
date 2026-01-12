package ec.edu.uce.taller8.ui.item

import androidx.navigation.NavType
import androidx.navigation.navArgument
import ec.edu.uce.taller8.R
import ec.edu.uce.taller8.ui.navigation.NavigationDestination

object ItemDetailsDestination : NavigationDestination {
    override val route = "item_details"
    override val titleRes = R.string.item_detail_title
    const val ITEM_ID_ARG = "itemId"
    val routeWithArgs = "$route/{$ITEM_ID_ARG}"
    val arguments = listOf(navArgument(ITEM_ID_ARG) { type = NavType.IntType })
}
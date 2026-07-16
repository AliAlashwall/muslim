package com.example.muslim.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.muslim.presentation.screens.home.HomeScreen
import com.example.muslim.presentation.screens.prayerTimes.PrayerTimesScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    ) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier
    ) {

        composable<HomeRoute> {
            HomeScreen(onPrayersClicked = { navController.navigate(PrayerTimesRoute) })
        }

        composable<PrayerTimesRoute> {
            PrayerTimesScreen(onBackClicked = { navController.popBackStack() })
        }

    }
}
package com.yjotdev.accidentreporter.application.navigation

import androidx.annotation.StringRes
import com.yjotdev.accidentreporter.R

enum class ViewRoutes(@get:StringRes val idTitle: Int){
    Start(idTitle = 0),
    TokenConfig(idTitle = R.string.tokenconfigview_title),
    Map(idTitle = R.string.mapview_title),
    AddPosition(idTitle = R.string.addpositionview_title),
    EditPosition(idTitle = R.string.editpositionview_title)
}
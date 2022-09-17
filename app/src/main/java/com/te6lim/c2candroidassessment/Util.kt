package com.te6lim.c2candroidassessment

import com.te6lim.c2candroidassessment.database.DBExhibit
import com.te6lim.c2candroidassessment.model.Exhibit
import com.te6lim.c2candroidassessment.network.NetworkExhibit

fun List<DBExhibit>.toModelExhibitList(): List<Exhibit> {
    return this.map {
        Exhibit(title = it.title ?: "", images = it.images ?: listOf())
    }
}

@JvmName("toModelExhibitListNetworkExhibit")
fun List<NetworkExhibit>.toModelExhibitList(): List<Exhibit> {
    return this.map {
        Exhibit(title = it.title, images = it.images)
    }
}

fun List<Exhibit>.toDBExhibitList(): List<DBExhibit> {
    return this.map {
        DBExhibit(title = it.title, images = it.images)
    }
}

fun List<Exhibit>.toNetworkExhibitList(): List<NetworkExhibit> {
    return map {
        NetworkExhibit(title = it.title, images = it.images)
    }
}
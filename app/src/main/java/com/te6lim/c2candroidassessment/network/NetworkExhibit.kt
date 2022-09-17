package com.te6lim.c2candroidassessment.network

import com.te6lim.c2candroidassessment.model.Exhibit

data class NetworkExhibit(
    override val title: String? = null, override val images: List<String>? = null
) : Exhibit(title, images)
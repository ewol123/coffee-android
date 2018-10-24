package com.example.x.coffeetime.application.api.BindingModel

data class PaginationModel(
        var Page: Int,
        val ItemsPerPage: Int,
        val Query: String
) {}


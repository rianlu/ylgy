package com.bedroom412.ylgy.parser

data class ImportSourceInfo(

    var url: String?,
    var importSourceParts: List<ImportSourcePartInfo> = listOf()
) {


}

data class ImportSourcePartInfo(
    var url: String
) {

}

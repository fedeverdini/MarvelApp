package com.example.marvelapp.model.character

interface IDetailObject {
    val available : Int
    val returned : Int
    val collectionURI : String
    val items : List<Items>
}
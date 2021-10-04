package com.example.marvelapp.datasource.remote

import com.example.marvelapp.model.Resource
import com.example.marvelapp.model.character.CharacterListResponse
import com.example.marvelapp.utils.constants.NetworkUrl.GET_CHARACTER_DETAILS
import com.example.marvelapp.utils.constants.NetworkUrl.GET_CHARACTER_LIST
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApi {
    @GET(GET_CHARACTER_LIST)
    suspend fun getCharacterList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Resource<CharacterListResponse>

    @GET(GET_CHARACTER_DETAILS)
    suspend fun getCharacterDetails(
        @Path("characterId") characterId: Int
    ): Resource<CharacterListResponse>
}
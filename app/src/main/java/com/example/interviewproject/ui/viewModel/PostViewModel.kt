package com.example.interviewproject.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interviewproject.api.ApiClient
import com.example.interviewproject.api.response.Post
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    val postsLiveData = MutableLiveData<ArrayList<Post>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    fun fetchPosts(pageNo: Int, items: Int) {
        viewModelScope.launch {
            try {
                loadingLiveData.postValue(true)
                val response = ApiClient.service.getPosts(pageNo, items)

                if (response.isSuccessful) {
                    postsLiveData.postValue(response.body())
                } else {
                    message.postValue("Server Error")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                message.postValue(e.message)
            }

        }
    }

}
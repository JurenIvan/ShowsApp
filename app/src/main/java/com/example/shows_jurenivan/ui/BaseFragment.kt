package com.example.shows_jurenivan.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View

open class BaseFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Fragment", "${javaClass.simpleName} onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("Fragment", "${javaClass.simpleName} onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Log.d("Fragment", "${javaClass.simpleName} onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Fragment", "${javaClass.simpleName} onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Fragment", "${javaClass.simpleName} onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Fragment", "${javaClass.simpleName} onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("Fragment", "${javaClass.simpleName} onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Fragment", "${javaClass.simpleName} onDestroy")
    }
}
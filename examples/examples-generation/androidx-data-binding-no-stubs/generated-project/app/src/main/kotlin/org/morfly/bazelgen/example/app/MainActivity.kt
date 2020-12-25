package org.morfly.bazelgen.example.app

import android.app.Activity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import org.morfly.bazelgen.example.app.R
import org.morfly.bazelgen.example.app.databinding.LayoutAppBinding


class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: LayoutAppBinding = DataBindingUtil.setContentView(this, R.layout.layout_app)
        val viewModel = AppViewModel()
        binding.viewModel = viewModel
    }
}
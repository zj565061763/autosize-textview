package com.sd.demo.autosize_textview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.autosize_textview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }

    override fun onClick(v: View?) {
        when (v) {
            _binding.btnBig -> {
                _binding.tvContent.apply {
                    this.text = this.text.toString() + "hello"
                }
            }
            _binding.btnReset -> {
                _binding.tvContent.apply {
                    this.text = "hello"
                }
            }
        }
    }
}
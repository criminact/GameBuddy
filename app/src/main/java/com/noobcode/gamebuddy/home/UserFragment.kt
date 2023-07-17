package com.noobcode.gamebuddy.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.noobcode.gamebuddy.CustomApplication
import com.noobcode.gamebuddy.R
import com.noobcode.gamebuddy.databinding.FragmentUserBinding
import com.noobcode.gamebuddy.utils.FirebaseObject

class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inviteDesc.text = FirebaseObject.currentUser.uid.substring(0,5)

        binding.inviteCodeBtn.setOnClickListener {
            val clipboardManager = CustomApplication.ApplicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.setPrimaryClip(ClipData.newPlainText   ("", "You have been invited to test GameBuddy, an app that is unlike any other. To join the program, enter this invitation code: ${binding.inviteDesc.text}"))
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
                Toast.makeText(CustomApplication.ApplicationContext, "Copied", Toast.LENGTH_SHORT).show()
        }
    }

}
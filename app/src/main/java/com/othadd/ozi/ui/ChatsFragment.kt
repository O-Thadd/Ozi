package com.othadd.ozi.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.othadd.ozi.*
import com.othadd.ozi.databinding.FragmentChatsBinding

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ChatsFragment : Fragment() {
    private val sharedViewModel: ChatViewModel by activityViewModels {
        ChatViewModelFactory(
            SettingsRepo(requireContext()),
            MessagingRepo((activity?.application as OziApplication)),
            activity?.application as OziApplication
        )
    }

    private lateinit var binding: FragmentChatsBinding
    private lateinit var chatsRecyclerAdapter: ChatsRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        chatsRecyclerAdapter = ChatsRecyclerAdapter{
            sharedViewModel.setChat(it)
            findNavController().navigate(ChatsFragmentDirections.actionChatsFragmentToChatFragment())
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            chatsFragment = this@ChatsFragment
            chatsRecyclerView.adapter = chatsRecyclerAdapter
        }

        createChannel(MY_NOTIFICATION_CHANNEL_ID, "Ozi")

        return binding.root
    }


    fun findUsers() {
        findNavController().navigate(ChatsFragmentDirections.actionChatsFragmentToFindUsersFragment())
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
            }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "New Messages"

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
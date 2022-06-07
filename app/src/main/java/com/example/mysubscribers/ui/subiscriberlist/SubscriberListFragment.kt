package com.example.mysubscribers.ui.subiscriberlist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mysubscribers.R
import com.example.mysubscribers.data.db.AppDatabase
import com.example.mysubscribers.extension.navigateWithAnimations
import com.example.mysubscribers.repository.DatabaseDataSource
import com.example.mysubscribers.repository.SubscriberRepository
import kotlinx.android.synthetic.main.subscriber_list_fragment.*

class SubscriberListFragment : Fragment(R.layout.subscriber_list_fragment) {

    private val viewModel: SubscriberListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val subscriberDao = AppDatabase.getInstance(requireContext()).subscriberDAO
                val repository: SubscriberRepository = DatabaseDataSource(subscriberDao)
                return SubscriberListViewModel(repository) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelEvents()
        configureViewListeners()
    }

    private fun observeViewModelEvents() {
        viewModel.allSubscribersEvent.observe(viewLifecycleOwner) { allSubscribers ->
            setHasOptionsMenu(allSubscribers.size > 1)

            val subscriberListAdapter = SubscriberListAdapter(allSubscribers) { subscriber ->
                val directions = SubscriberListFragmentDirections
                    .actionSubscriberListFragmentToSubscriberFragment(subscriber)

                findNavController().navigateWithAnimations(directions)
            }

            with(recycler_subscribers) {
                setHasFixedSize(true)
                adapter = subscriberListAdapter
            }
        }

        viewModel.deleteAllSubscribersEvent.observe(viewLifecycleOwner) {
            viewModel.getSubscribers()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSubscribers()
    }

    private fun configureViewListeners() {
        fabAddSubscriber.setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_subscriberListFragment_to_subscriberFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.subscriber_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.delete_subscribers) {
            viewModel.deleteAllSubscribers()
            true
        } else super.onOptionsItemSelected(item)
    }
}
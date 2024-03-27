package ru.shumikhin.dailycurrencyapp.presentation.allCurrencyScreen

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.shumikhin.dailycurrencyapp.R
import ru.shumikhin.dailycurrencyapp.databinding.FragmentAllCurrencyBinding

@AndroidEntryPoint
class AllCurrencyFragment : Fragment() {

    private var _binding: FragmentAllCurrencyBinding? = null
    private val binding get() = _binding!!

    private lateinit var progressBar: ProgressBar
    private lateinit var errorContainer: LinearLayout
    private lateinit var btnRetryLoadValute: MaterialButton

    private lateinit var contentContainer: LinearLayout

    private lateinit var valuteRecyclerView: RecyclerView
    private lateinit var valuteAdapter: ValuteAdapter

    private lateinit var tvLastUpdateTime: TextView
    private lateinit var tvLastUpdateTimeTitle: TextView

    private var textDefaultColor: Int = 0

    private val allCurrencyViewModel: AllCurrencyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllCurrencyBinding.inflate(inflater, container, false)
        initSwipeRefresh()
        initContainers()
        initComponents()
        initButtons()
        initRecyclerView()
        initViewModelObservers()
        val view = binding.root
        return view
    }

    private fun initSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            allCurrencyViewModel.reloadValute()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onStart() {
        super.onStart()
        allCurrencyViewModel.startUpdating()
    }


    override fun onPause() {
        super.onPause()
        allCurrencyViewModel.stopUpdating()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewModelObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    allCurrencyViewModel.autoUpdateCurrency()
                }
                allCurrencyViewModel.state.collectLatest { state ->
                    when (state) {
                        State.Default -> {
                            if (valuteAdapter.currentList().isEmpty()) {
                                showLoading()
                            }
                        }

                        is State.Error -> {
                            if (valuteAdapter.currentList().isEmpty()) {
                                showFullScreenError()
                            }else{
                                showTitleError()
                            }
                            allCurrencyViewModel.stopUpdating()
                        }

                        State.Loading -> {
                            if (valuteAdapter.currentList().isEmpty()) {
                                showLoading()
                            }
                        }

                        is State.Success -> {
                            showContent()
                            valuteAdapter.submitList(state.currencyRates.valute)
                            tvLastUpdateTime.text = state.currencyRates.updateTime
                            allCurrencyViewModel.startUpdating()
                        }
                    }
                }
            }
        }
    }


    private fun showFullScreenError() {
        errorContainer.visibility = View.VISIBLE
        contentContainer.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    private fun showTitleError(){
        tvLastUpdateTimeTitle.text = resources.getText(R.string.update_error)
        tvLastUpdateTimeTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.error_container_bg))
        tvLastUpdateTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.error_container_bg))
    }

    private fun showLoading() {
        errorContainer.visibility = View.GONE
        contentContainer.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showContent() {
        tvLastUpdateTimeTitle.text = resources.getText(R.string.last_update_time)
        tvLastUpdateTimeTitle.setTextColor(textDefaultColor)
        tvLastUpdateTime.setTextColor(textDefaultColor)

        errorContainer.visibility = View.GONE
        contentContainer.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun initButtons() {
        btnRetryLoadValute = binding.btnRetryLoadValutes
        btnRetryLoadValute.setOnClickListener {
            allCurrencyViewModel.reloadValute()
        }
    }

    private fun initComponents() {
        tvLastUpdateTime = binding.tvLastUpdateTime
        tvLastUpdateTimeTitle = binding.tvLastUpdateTimeTitle

        textDefaultColor = tvLastUpdateTime.textColors.defaultColor

        progressBar = binding.progressBar
    }

    private fun initContainers() {
        errorContainer = binding.containerError
        contentContainer = binding.layoutCurrencyRates
    }

    private fun initRecyclerView() {
        valuteRecyclerView = binding.rwValuteList
        valuteAdapter = ValuteAdapter()
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        valuteRecyclerView.adapter = valuteAdapter
        valuteRecyclerView.layoutManager = layoutManager
    }

}


package ru.shumikhin.dailycurrencyapp.presentation.allCurrencyScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.shumikhin.dailycurrencyapp.data.models.Valute
import ru.shumikhin.dailycurrencyapp.databinding.ItemCurrencyBinding

class ValuteAdapter: RecyclerView.Adapter<ValuteAdapter.ValuteViewHolder>() {
    private val differ: AsyncListDiffer<Valute> = AsyncListDiffer(this, DiffCallback())

    fun submitList(list: List<Valute>) = differ.submitList(list)

    fun currentList(): List<Valute> = differ.currentList

    fun getItemAtPosition(position: Int): Valute {
        return currentList()[position]
    }


    class ValuteViewHolder(val binding: ItemCurrencyBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValuteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCurrencyBinding.inflate(inflater, parent, false)
        return ValuteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return currentList().size
    }

    override fun onBindViewHolder(holder: ValuteViewHolder, position: Int) {
        val valuteItem = getItemAtPosition(position)
        with(holder.binding){
            tvValuteCode.text = valuteItem.charCode
            tvValuteName.text = valuteItem.name
            tvValuteNominal.text = valuteItem.nominal.toString()
            tvValuteValue.text = valuteItem.value.toString()
        }
    }
    private class DiffCallback : DiffUtil.ItemCallback<Valute>() {
        override fun areItemsTheSame(oldItem: Valute, newItem: Valute) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Valute, newItem: Valute) =
            oldItem == newItem
    }
}
package com.hend.searchproductschallenge.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hend.searchproductschallenge.R
import com.hend.searchproductschallenge.network.response.Product
import com.hend.searchproductschallenge.utils.loadImageUrl
import kotlinx.android.synthetic.main.item_product.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Products paging list adapter to display paging feature
 */
class ProductsAdapter : PagedListAdapter<Product, ProductsAdapter.MyViewHolder>(
    DiffUtilCallBack
) {

    private object DiffUtilCallBack : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productName == newItem.productName
                    && oldItem.availabilityState == newItem.availabilityState
                    && oldItem.salesPriceIncVat == newItem.salesPriceIncVat
        }
    }

    @ExperimentalCoroutinesApi
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { holder.bindProduct(it) }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgProduct = itemView.img_product
        private val txtProductName = itemView.txt_product_name
        private val txtAvailableInStock= itemView.txt_available_in_stock
        private val txtPrice = itemView.txt_price
        private val txtNextDayDelivery = itemView.txt_next_day_delivery

        fun bindProduct(product: Product) {
            product.let {
                imgProduct.loadImageUrl(it.productImage)
                txtProductName.text = it.productName
                txtAvailableInStock.text = itemView.context.getString(R.string.available_in_stock, it.availabilityState)
                txtPrice.text = itemView.context.getString(R.string.price, it.salesPriceIncVat.toString())

                if(it.nextDayDelivery) txtNextDayDelivery.visibility = View.VISIBLE else txtNextDayDelivery.visibility = View.GONE
            }
        }

        companion object {
            fun create(parent: ViewGroup): MyViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product, parent, false)
                return MyViewHolder(
                    view
                )
            }
        }
    }
}
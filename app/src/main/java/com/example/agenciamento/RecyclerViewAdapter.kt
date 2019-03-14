package com.cfsuman.kotlinexamples

import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.recyclerview_item.view.*
//import android.R
import android.util.Log
import com.example.agenciamento.*


class RecyclerViewAdapter(val desafios: ArrayList<Desafio>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    /*

        VH onCreateViewHolder (ViewGroup parent, int viewType)
            Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to
            represent an item.

            This new ViewHolder should be constructed with a new View that can
            represent the items of the given type. You can either create a new View manually
            or inflate it from an XML layout file.

            The new ViewHolder will be used to display items of the adapter using
            onBindViewHolder(ViewHolder, int, List). Since it will be re-used to display different
            items in the data set, it is a good idea to cache references to sub views of the View
            to avoid unnecessary findViewById(int) calls.

        Parameters
            parent ViewGroup: The ViewGroup into which the new View will be added after it
                              is bound to an adapter position.
            viewType int: The view type of the new View.

        Returns
            VH : A new ViewHolder that holds a View of the given view type.
    */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val mListener : RecyclerViewClickListener;

        // Inflate the custom view from xml layout file
        val v: View = LayoutInflater.from(parent.context)
            .inflate(com.example.agenciamento.R.layout.recyclerview_item,parent,false)

        // Add a view listener to each element
        v.setOnClickListener{
            val intentInfo = Intent(v.context, DesafioInfoActivity::class.java)
            intentInfo.putExtra("nome", v.recyclerview_nome.text.toString())
            startActivity(v.context, intentInfo, null)

            Log.d("teste,", "numero " + v.recyclerview_nome.text.toString())
        }

        // Return the view holder
        return ViewHolder(v)
    }


    /*

        void onBindViewHolder (VH holder, int position, List<Object> payloads)
            Called by RecyclerView to display the data at the specified position. This method should
            update the contents of the itemView to reflect the item at the given position.

        Parameters
            holder VH: The ViewHolder which should be updated to represent the contents of the
                       item at the given position in the data set.
            position int: The position of the item within the adapter's data set.
            payloads List: A non-null list of merged payloads. Can be empty list if requires
                           full update.
    */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nome?.text = desafios.get(position).nome
        holder.prazo?.text = desafios.get(position).prazo
    }


    override fun getItemCount(): Int {
        // Return the size of users list
        // Returns the total number of items in the data set held by the adapter.
        return desafios.size
    }

    /*
        RecyclerView.ViewHolder
            A ViewHolder describes an item view and metadata about its place within the RecyclerView.
    */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //Referência ao view de nome e resumo de recyclerview_item.xml
        val nome = itemView.recyclerview_nome
        val prazo = itemView.recyclerview_prazo
    }


    // This two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
package com.example.agenciamento

import android.app.ActionBar
import android.app.LauncherActivity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.cfsuman.kotlinexamples.RecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_desafio_info.*
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.firestore.FirebaseFirestore
//import jdk.nashorn.internal.runtime.ECMAException.getException
//import android.support.test.orchestrator.junit.BundleJUnitUtils.getResult
import com.google.firebase.firestore.QueryDocumentSnapshot
//import org.junit.experimental.results.ResultMatchers.isSuccessful
import com.google.firebase.firestore.QuerySnapshot
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import android.support.multidex.MultiDex
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.Window.FEATURE_NO_TITLE
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot


//Lista de elementos que armazenarão os desafios encontrados
var desafioLista = ArrayList<Desafio>()

//Lista de areas disponíveis de Desafios
var areaLista : MutableList<String> = ArrayList()


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var desafio : Desafio // Objeto de Data class que aramzena os documentos(desafios) para aramazena-los em desafioLista
        var desafioArea : Desafio // Objeto de Data class que aramzena os documentos(areaoption) para aramazena-los em areaLista

        //Removo a ActionBar
        val aBar = supportActionBar
        aBar?.hide()

        //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------//

        //CRIAR A RECYCLER VIEW

        //inicializa uma nova linear layout manager
        val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(
            this, // Context
            LinearLayout.VERTICAL, // Orientation
            false // Reverse layout
        )

        //Especifica o layout manager para o recycler view
        recycler_view.layoutManager = linearLayoutManager

        //Adiciona os desafios encontrados ao adapter
        recycler_view.adapter = RecyclerViewAdapter(desafioLista)


        // Crio uma conexão com o Banco de Dados através da val db
        // e uma refência a coleção de desafios em val desafiosCollection
        val db = FirebaseFirestore.getInstance()
        val desafiosCollection = db.collection("desafios")


        //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------//

        //BUSCAR OS DESAFIOS NO FIRESTORE

        //Busco todos os documentos(desafios) da minha coleção
        desafiosCollection
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("SuccessDesafio", document.id + " => " + document.data)
                        //Crio um novo elemento desafio para cada Document para adicionar na minha Data Class
                        desafio  = document.toObject(Desafio::class.java)
                        desafioLista.add(desafio)

                        //Update no recycler view
                        recycler_view.adapter?.notifyDataSetChanged()
                    }
                }
                else {
                    Log.d("ErrorDesafio", "Error getting documents: ", task.exception)
                }
            }

        //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------//

        //PESQUISAR DESAFIOS

        searchView.setIconifiedByDefault(true)
        searchView.setFocusable(true)
        searchView.setIconified(false)
        //Adiciono um listener na barra de pesquisa que mostra os desafios onde o input é igual ao nome do desafios
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //Função que mostra os resultados da pesquisa apos confirmação
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            //Função que mostra o resultado da pesquisa live
            override fun onQueryTextChange(newText: String): Boolean {
                val UserInput : String  = newText.toLowerCase()
                val searchDesafioLista = ArrayList<Desafio>()

                //itero pela lista de desafios buscando os resultados e adicionando eles em uma nova lista que será mostrada
                for(element in desafioLista){
                    if(element.nome.toLowerCase() == UserInput){
                        searchDesafioLista.add(element)
                    }

                    //Caso nada digitado, mostro todos os desafios
                    else if(newText.isEmpty()){
                        recycler_view.adapter = RecyclerViewAdapter(desafioLista)
                    }
                }

                //Atualizo os desafios com a nova lista de desafios encontrados
                recycler_view.adapter = RecyclerViewAdapter(searchDesafioLista)
                return false
            }
        })

        //Função para resetar a lista quando o "x" da barra de busca for pressionada
        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                recycler_view.adapter = RecyclerViewAdapter(desafioLista)
                return true
            }
        })

        //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------//

        //DEFINIR SPINNER

        //Busco as areas de desafio disponíveis para o spinner
        val areaCollection = db.collection("areaoptions")
        areaCollection
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("SuccessArea", document.id + " => " + document.data)
                        //Crio um novo elemento desafio para cada Document para adicionar na minha Data Class
                        desafioArea = document.toObject(Desafio::class.java)
                        Log.d("DesafioArea", desafioArea.nome)
                        areaLista.add(desafioArea.nome)
                    }
                }
                else {
                    Log.d("ErrorArea", "Error getting documents: ", task.exception)
                }
            }

        //Verificar que todas as áreas estão em areaLista
        for(element in areaLista){
            Log.d("areaLista", element)
        }

        //Crio uma lista com os desafios filtrados
        val spinnerDesafioLista = ArrayList<Desafio>()

        if (spinner != null) {
            //Crio um array adapter que armazena todas as opções de areas armazenadas em areaLista
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, areaLista)
            spinner.adapter = arrayAdapter


            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                //Função para quando item do spinner é clicado(Adiciono desafios filtrados em spinnerDesafioLista e exibo os mesmos
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    Log.d("spinner", parent.getItemAtPosition(position).toString())
                    for(element in desafioLista){

                        if(element.area == parent.getItemAtPosition(position).toString() ){
                            spinnerDesafioLista.add(element)
                        }
                    }
                    //Atualizo os desafios com a nova lista de desafios filtrado
                    if(spinnerDesafioLista.isNotEmpty())
                        recycler_view.adapter = RecyclerViewAdapter(spinnerDesafioLista)
                }

                //Função para quando nenhum item do spinner é clicado(Reseto a lista de desafios original)
                override fun onNothingSelected(parent: AdapterView<*>) {
                    recycler_view.adapter = RecyclerViewAdapter(desafioLista)
                }
            }
        }

        //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------//

        /*
        //CRIAR NOVOS DESAFIOS

        addButton.setOnClickListener{
            //Adiciona o intent pro botão de ADD ir para a tela de criação de desafios
            val intentAdd = Intent(this, AdicionarActivity::class.java)
            startActivityForResult(intentAdd, 1)
            //Quando activity encerrar, chamo onActivityResult para lidar com os dados obtidos
        }
        */

    }

    //Encerro a Activity caso o usuário pressione o Back Button
    override fun onBackPressed() {
        finishAffinity()
    }
}







    //FUNÇÃO RESPONSÁVEL POR CAPTURAR OS RESULTADOS DAA INTENT DE CRIAÇÃO DE DESAFIOS
    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        //CASO INTENT CRIAR NOVO DESAFIO
        if(requestCode == 1){
            val novoNome = data?.getStringExtra("nome").toString()
            val novaDesc = data?.getStringExtra("desc").toString()
            //Log.d("teste", novoNome + " " + novaDesc)

            desafios.add(desafio(novoNome, novaDesc)) //Adiciona novo desafio a lista
            itemCount++ //Atualiza a contagem de desafios
            recycler_view.adapter?.notifyItemChanged(itemCount) //Notifica o adapter que existe um novo item
        }

    }
    */

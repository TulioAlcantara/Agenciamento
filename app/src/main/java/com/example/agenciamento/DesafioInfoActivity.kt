package com.example.agenciamento

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_desafio_info.*
import kotlinx.android.synthetic.main.activity_main.*

var resultados = ArrayList<Desafio>()

class DesafioInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desafio_info)

        //Remove a ActioBar
        val aBar = supportActionBar
        aBar?.hide()


        //Recebe o nome do desafio clicado
        val nome = intent.getStringExtra("nome")
        var db = FirebaseFirestore.getInstance()
        var desafiosCollection = db.collection("desafios")

        //Busco as informações do desafio baseado no nome
        desafiosCollection.whereEqualTo("nome", nome)
            .get()
            .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    Log.d("FoundDesafio", document.id + " => " + document.data)
                    //Crio um novo elemento desafio para cada Document para adicionar na minha Data Class
                    var resultado : Desafio = document.toObject(Desafio::class.java)
                    resultados.add(resultado)

                }
            }
            //Caso não encontrar, encerro a Activity
            else {
                Log.d("NotFoundDesafios", "Error getting documents: ", task.exception)
                finish()
            }
        }

        for(element in resultados){
            nomeInfo.text = nome
            resumoInfo.text = element.resumo
            demandanteInfo.text = element.demandante
            emailInfo.text = element.emaildemandante
            prazoInfo.text = element.prazo
            areaInfo.text = element.area
            if(element.remunerado == true){
                remuneradoInfo.setChecked(true)
            }
            else{
                remuneradoInfo.setChecked(false)
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}

package com.example.agenciamento


// Modelo para armazenar os resultados encontrados nas consultas ao Firestore
data class Desafio(
    var area:String = "",
    var demandante:String = "",
    var emaildemandante:String = "",
    var nome:String = "",
    var prazo:String = "",
    var remunerado:Boolean? = null,
    var resumo:String = "",
    var status:String = ""
)

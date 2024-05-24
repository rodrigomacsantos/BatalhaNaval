import java.io.File

const val MENU_PRINCIPAL = 100
const val MENU_DEFINIR_TABULEIRO = 101
const val MENU_DEFINIR_NAVIOS = 102
const val MENU_JOGAR = 103
const val MENU_LER_FICHEIRO = 104
const val MENU_GRAVAR_FICHEIROS = 105
const val SAIR = 106
const val POR_IMPLEMENTAR = "!!! POR IMPLEMENTAR, tente novamente"

var numLinhas = -1
var numColunas = -1

var tabuleiroHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroComputador: Array<Array<Char?>> = emptyArray()

var tabuleiroPalpitesDoHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoComputador: Array<Array<Char?>> = emptyArray()


fun menuPrincipal(): Int {
    println()
    println("> > Batalha Naval < <")
    println()
    println("1 - Definir Tabuleiro e Navios")
    println("2 - Jogar")
    println("3 - Gravar")
    println("4 - Ler")
    println("0 - Sair")
    println()
    var instrucao: Int? = null
    while (instrucao == null) {
        instrucao = readln().toIntOrNull()
        if (instrucao == 1) {
            return menuDefinirTabuleiro()
        }
        if (instrucao == 2) {
            return menuJogar()
        }
        if (instrucao == 3) {
            return menuGuardarFicheiro()
        }
        if (instrucao == 4) {
            return menuLerFicheiro()
        }
        if (instrucao == 0) {
            return SAIR
        }
        if (instrucao == null || instrucao > 4 || instrucao < 0) {
            instrucao = null
            println("!!! Opcao invalida, tente novamente")
        }
    }
    return menuPrincipal()
}

fun menuDefinirTabuleiro(): Int {
    println()
    println("> > Batalha Naval < <")
    println()
    println("Defina o tamanho do tabuleiro:")
    var numLinhas: Int? = 0
    while (numLinhas == 0) {
        println("Quantas linhas?")
        numLinhas = readln().toIntOrNull()
        if (numLinhas == null) {
            println("!!! Numero de linhas invalidas, tente novamente")
            numLinhas = 0
        } else if (numLinhas == -1) {
            return menuPrincipal()
        } else {
            var numColunas: Int? = 0
            while (numColunas == 0) {
                println("Quantas colunas?")
                numColunas = readln().toIntOrNull()
                if (numColunas == null) {
                    println("!!! Numero de colunas invalidas, tente novamente")
                    numColunas = 0
                } else if (numColunas == -1) {
                    return menuPrincipal()
                } else {
                    if (tamanhoTabuleiroValido(numLinhas, numColunas)) {
                        tabuleiroHumano = criaTabuleiroVazio(numLinhas, numColunas)
                        tabuleiroComputador = criaTabuleiroVazio(numLinhas, numColunas)
                        tabuleiroPalpitesDoHumano = criaTabuleiroVazio(numLinhas, numColunas)
                        tabuleiroPalpitesDoComputador = criaTabuleiroVazio(numLinhas, numColunas)
                        mostrarTabuleiro(tabuleiroHumano, true)
                        return menuDefinirNavios(numLinhas, numColunas)
                    } else {
                        println("!!! Tabuleiro invalido, tente novamente")
                        return menuDefinirTabuleiro()
                    }

                }
            }
        }

    }
    return menuPrincipal()
}

fun menuDefinirNavios(numLinhas: Int, numColunas: Int): Int {
    val pedirCoordenadas = "Coordenadas? (ex: 6,G)"
    val navios = calculaNumNavios(numLinhas, numColunas)
    var count = 0
    while (count < navios.size) {
        var count2 = 0
        while (count2 < navios[count]) {
            if (count == 0) {
                println("Insira as coordenadas de um submarino:")
                var coordenadas = ""
                while (coordenadas == "") {
                    println(pedirCoordenadas)
                    coordenadas = readln()
                    if (coordenadas == "-1") {
                        return menuPrincipal() }
                    val coordenadasProcessadas = processaCoordenadas(coordenadas, numLinhas, numColunas)
                    if (coordenadasProcessadas == null) {
                        coordenadas = ""
                        println("!!! Coordenadas invalidas, tente novamente")
                    } else {
                        if (insereNavioSimples(
                                tabuleiroHumano, coordenadasProcessadas.first, coordenadasProcessadas.second, 1)) {
                            var count = 0
                            while (count < obtemMapa(tabuleiroHumano, true).size) {
                                println(obtemMapa(tabuleiroHumano, true)[count])
                                count++ }
                            count2++ } else {
                            coordenadas = ""
                            println("!!! Posicionamento invalido, tente novamente") } } }
            } else {
                if (count == 1) {
                    println("Insira as coordenadas de um contra-torpedeiro:")
                } else if (count == 2) {
                    println("Insira as coordenadas de um navio-tanque:")
                } else if (count == 3) {
                    println("Insira as coordenadas de um porta-avioes:") }
                var coordenadas = ""
                while (coordenadas == "") {
                    println(pedirCoordenadas)
                    coordenadas = readln()
                    if (coordenadas == "-1") {
                        return menuPrincipal() }
                    val coordenadasProcessadas =
                        processaCoordenadas(coordenadas, numLinhas, numColunas) // println(coordenadasProcessadas)
                    if (coordenadasProcessadas == null) {
                        coordenadas = ""
                        println("!!! Coordenadas invalidas, tente novamente") } else {
                        println("Insira a orientacao do navio:")
                        var orientacao = ""
                        while (orientacao == "") {
                            println("Orientacao? (N, S, E, O)")
                            orientacao = readln()
                            if (orientacao == "-1") {
                                return menuPrincipal() }
                            if (orientacao == "N" || orientacao == "S" || orientacao == "E" || orientacao == "O") {
                                if (insereNavio(tabuleiroHumano, coordenadasProcessadas.first,
                                        coordenadasProcessadas.second, orientacao, count + 1)) {
                                    mostrarTabuleiro(tabuleiroHumano, true)
                                    count2++ } else {
                                    coordenadas = ""
                                    println("!!! Posicionamento invalido, tente novamente") }
                            } else {
                                orientacao = ""
                                println("!!! Orientacao invalida, tente novamente") } } } } } }
        count++ }
    preencheTabuleiroComputador(tabuleiroComputador, navios)
    mostrarTabuleiroComputador()
    return menuPrincipal()
}
fun mostrarTabuleiroComputador(){
    var respostaValida = false
    while (!respostaValida) {
        println("Pretende ver o mapa gerado para o Computador? (S/N)")
        val resposta = readln()
        if (resposta == "S") {
            mostrarTabuleiro(tabuleiroComputador, true)
            respostaValida = true
        } else if (resposta == "N" || resposta == "-1") {
            respostaValida = true
        }
    }
}

fun mostrarTabuleiro(tabuleiro: Array<Array<Char?>>, real: Boolean) {
    var count = 0
    while (count < obtemMapa(tabuleiro, real).size) {
        println(obtemMapa(tabuleiro, real)[count])
        count++
    }
}

fun menuJogar(): Int {
    if (tabuleiroHumano.isEmpty()) {
        println("!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente")
        return menuPrincipal() }
    while (!venceu(tabuleiroPalpitesDoHumano) && !venceu(tabuleiroPalpitesDoComputador)) {
        mostrarTabuleiro(tabuleiroPalpitesDoHumano, false)
        println("Indique a posição que pretende atingir")
        println("Coordenadas? (ex: 6,G)")
        val coordenadas = readln()
        if (coordenadas == "-1") {
            return menuPrincipal() }
        if (coordenadas != "") {
            val coordenadasProcessadas =
                processaCoordenadas(coordenadas, tabuleiroPalpitesDoHumano.size, tabuleiroPalpitesDoHumano.size)
            val coordenadasProcessadasComputador = geraTiroComputador(tabuleiroPalpitesDoComputador)
            if (lancarTiro(tabuleiroComputador, tabuleiroPalpitesDoHumano, coordenadasProcessadas) != "") {
                print(
                    ">>> HUMANO >>>" + lancarTiro(
                        tabuleiroComputador, tabuleiroPalpitesDoHumano, coordenadasProcessadas))
                if (navioCompleto(
                        tabuleiroPalpitesDoHumano,
                        coordenadasProcessadas?.first!!.toInt(),
                        coordenadasProcessadas?.second!!.toInt()
                    )) {
                    println(" Navio ao fundo!")
                } else {
                    println()
                }
                if (venceu(tabuleiroPalpitesDoHumano)) {
                    println("PARABENS! Venceu o jogo!")
                    println("Prima enter para voltar ao menu principal")
                    val voltar = readln()
                    if (voltar == "-1") {
                        return menuPrincipal()
                    }
                    return menuPrincipal()
                }
                println("Computador lancou tiro para a posicao " + coordenadasProcessadasComputador)
                print(
                    ">>> COMPUTADOR >>>" + lancarTiro(
                        tabuleiroHumano, tabuleiroPalpitesDoComputador, coordenadasProcessadasComputador
                    ))
                if (navioCompleto(
                        tabuleiroPalpitesDoComputador,
                        coordenadasProcessadasComputador?.first!!.toInt(),
                        coordenadasProcessadasComputador?.second!!.toInt()
                    )) {
                    println(" Navio ao fundo!") }
                else{
                    println() }
                if (venceu(tabuleiroPalpitesDoComputador)) {
                    println("OPS! O computador venceu o jogo!")
                    println("Prima enter para voltar para ao menu principal")
                    val voltar = readln()
                    if (voltar == "-1") {
                        return menuPrincipal()
                    }
                    return menuPrincipal()
                }
                println("Prima enter para continuar")
                val continuar = readln()
                if (continuar == "-1") {
                    return menuPrincipal()
                } } } }

    return menuPrincipal()
}

fun menuLerFicheiro(): Int {
    println("Introduza o nome do ficheiro (ex: jogo.txt)")
    val nomeDoFicheiro = readln()
    tabuleiroHumano = lerJogo(nomeDoFicheiro, 1)
    tabuleiroPalpitesDoHumano = lerJogo(nomeDoFicheiro, 2)
    tabuleiroComputador = lerJogo(nomeDoFicheiro, 3)
    tabuleiroPalpitesDoComputador = lerJogo(nomeDoFicheiro, 4)
    println("Tabuleiro " + tabuleiroHumano.size + "x" + tabuleiroHumano.size + " lido com sucesso")
    mostrarTabuleiro(tabuleiroHumano, true)
    return menuPrincipal()
}

fun menuGuardarFicheiro(): Int {
    println("Introduza o nome do ficheiro (ex: jogo.txt)")
    val nomeDoFicheiro = readln()
    gravarJogo(
        nomeDoFicheiro, tabuleiroHumano, tabuleiroPalpitesDoHumano, tabuleiroComputador, tabuleiroPalpitesDoComputador
    )
    if (nomeDoFicheiro == "-1") {
        return menuPrincipal()
    }
    println("Tabuleiro " + tabuleiroComputador.size + "x" + tabuleiroComputador.size + " gravado com sucesso")
    return menuPrincipal()
}

fun tamanhoTabuleiroValido(numLinhas: Int, numColunas: Int): Boolean {
    if (numLinhas == numColunas){
        if (numLinhas == 4 || numLinhas == 5 || numLinhas == 7 || numLinhas == 8 || numLinhas == 10) {
            return true
        }
        return false
    } else {
        return false
    }
}

fun criaLegendaHorizontal(numColunas: Int): String {
    var count = 0
    val letra: CharRange = 'A'..'Z'
    var legenda = ""
    while (numColunas > count) {
        if (count == 0) {
            legenda += "${letra.first}"
        } else {
            legenda += " | ${letra.first + count}"

        }
        count++
    }
    return legenda
}

fun criaTerreno(numLinhas: Int, numColunas: Int): String {
    val legenda = "\n| " + criaLegendaHorizontal(numColunas) + " |"
    var linha = "| ~ |"
    var countColunas = 1
    var countLinhas = 1
    var terreno = "$legenda\n"
    while (countColunas < numColunas) {
        linha += " ~ |"
        countColunas++
    }
    while (countLinhas <= numLinhas) {
        terreno += ("$linha $countLinhas\n")
        countLinhas++
    }
    return terreno
}


fun processaCoordenadas(coordenadas: String, numLinhas: Int, numColunas: Int): Pair<Int, Int>? {
    if (coordenadas.first().isDigit() && coordenadas.first().digitToInt() <= numLinhas) {
        var count = 0
        val letra: CharRange = 'A'..'Z'
        while (numColunas > count) {
            if ("${letra.first + count}" == coordenadas.last().toString()) {
                val coordenadasSplit = coordenadas.split(',')
                if (coordenadasSplit[0].toInt() >= 10) {
                    return Pair(coordenadasSplit[0].toInt(), count + 1)
                }
                return Pair(coordenadas.first().digitToInt(), count + 1)
            }
            count++
        }
    }
    return null
}

fun main() {

    var menuActual = MENU_PRINCIPAL
    while (true) {
        menuActual = when (menuActual) {
            MENU_PRINCIPAL -> menuPrincipal()
            MENU_DEFINIR_TABULEIRO -> menuDefinirTabuleiro()
            MENU_DEFINIR_NAVIOS -> menuDefinirNavios(0, 0)
            MENU_JOGAR -> menuJogar()
            MENU_LER_FICHEIRO -> menuLerFicheiro()
            MENU_GRAVAR_FICHEIROS -> menuGuardarFicheiro()
            SAIR -> return
            else -> return
        }
    }
}

fun calculaNumNavios(numLinhas: Int, numColunas: Int): Array<Int> {
    var navios = arrayOf(0, 0, 0, 0)
    if (numColunas == numLinhas){
        if(numLinhas == 4 || numLinhas == 5 || numLinhas == 7 || numLinhas == 8 || numLinhas == 10){
            if (numLinhas == 4) {
                navios = arrayOf(2, 0, 0, 0)
            }
            if (numLinhas == 5) {
                navios = arrayOf(1, 1, 1, 0)
            }
            if (numLinhas == 7) {
                navios = arrayOf(2, 1, 1, 1)
            }
            if (numLinhas == 8) {
                navios = arrayOf(2, 2, 1, 1)
            }
            if (numLinhas == 10) {
                navios = arrayOf(3, 2, 1, 1)
            }
            return navios
        }}
    return emptyArray()
}

fun criaTabuleiroVazio(numLinhas: Int, numColunas: Int): Array<Array<Char?>> {
    val tabuleiro: Array<Array<Char?>> = Array(numLinhas) { Array<Char?>(numColunas) { null } }
    return tabuleiro
}

fun coordenadaContida(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {
    val linhaProcessada = linha
    val colunaProcessada = coluna
    if (linhaProcessada <= tabuleiro.size && colunaProcessada <= tabuleiro.size && linhaProcessada > 0 && colunaProcessada > 0) {
        return true
    } else {
        return false
    }
}

fun limparCoordenadasVazias(coordenadas: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    return coordenadas.filter { it != Pair(0, 0) }.toTypedArray()
}

fun juntarCoordenadas(coordenadas1: Array<Pair<Int, Int>>, coordenadas2: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    return coordenadas1 + coordenadas2
}

fun gerarCoordenadasNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Array<Pair<Int, Int>> {
    if (dimensao == 1) {
        val coordenadas = arrayOf(Pair(linha, coluna))
        return verificarCoordenadasNavio(tabuleiro, coordenadas)
    } else {
        if (orientacao == "E") {
            if (dimensao == 2) {
                val coordenadas = arrayOf(
                    Pair(linha, coluna), Pair(linha, coluna + 1))
                return verificarCoordenadasNavio(tabuleiro, coordenadas) }
            if (dimensao == 3) {
                val coordenadas = arrayOf(
                    Pair(linha, coluna), Pair(linha, coluna + 1), Pair(linha, coluna + 2))
                return verificarCoordenadasNavio(tabuleiro, coordenadas) }
            if (dimensao == 4) {
                val coordenadas = arrayOf(
                    Pair(linha, coluna), Pair(linha, coluna + 1), Pair(linha, coluna + 2), Pair(linha, coluna + 3))
                return verificarCoordenadasNavio(tabuleiro, coordenadas)
            } }
        if (orientacao == "O") {
            if (dimensao == 2) {
                val coordenadas = arrayOf(
                    Pair(linha, coluna), Pair(linha, coluna - 1))
                return verificarCoordenadasNavio(tabuleiro, coordenadas) }
            if (dimensao == 3) {
                val coordenadas = arrayOf(
                    Pair(linha, coluna), Pair(linha, coluna - 1), Pair(linha, coluna - 2))
                return verificarCoordenadasNavio(tabuleiro, coordenadas) }
            if (dimensao == 4) {
                val coordenadas = arrayOf(
                    Pair(linha, coluna), Pair(linha, coluna - 1), Pair(linha, coluna - 2), Pair(linha, coluna - 3))
                return verificarCoordenadasNavio(tabuleiro, coordenadas) } }
        if (orientacao == "N") {
            if (dimensao == 2) {
                val coordenadas = arrayOf(
                    Pair(linha, coluna), Pair(linha - 1, coluna))
                return verificarCoordenadasNavio(tabuleiro, coordenadas) }
            if (dimensao == 3) {
                val coordenadas = arrayOf(
                    Pair(linha, coluna), Pair(linha - 1, coluna), Pair(linha - 2, coluna))
                return verificarCoordenadasNavio(tabuleiro, coordenadas) }
            if (dimensao == 4) {
                val coordenadas = arrayOf(
                    Pair(linha, coluna), Pair(linha - 1, coluna), Pair(linha - 2, coluna), Pair(linha - 3, coluna))
                return verificarCoordenadasNavio(tabuleiro, coordenadas)
            } }
        if (orientacao == "S") {
            if (dimensao == 2) {
                val coordenadas = arrayOf(
                    Pair(linha, coluna), Pair(linha + 1, coluna))
                return verificarCoordenadasNavio(tabuleiro, coordenadas) }
            if (dimensao == 3) {
                val coordenadas = arrayOf(
                    Pair(linha, coluna), Pair(linha + 1, coluna), Pair(linha + 2, coluna))
                return verificarCoordenadasNavio(tabuleiro, coordenadas) }
            if (dimensao == 4) {
                val coordenadas = arrayOf(
                    Pair(linha, coluna), Pair(linha + 1, coluna), Pair(linha + 2, coluna), Pair(linha + 3, coluna))
                return verificarCoordenadasNavio(tabuleiro, coordenadas) } } }
    return emptyArray() }

fun gerarCoordenadasFronteira(
    tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int
): Array<Pair<Int, Int>> {

    val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, linha, coluna, orientacao, dimensao)
    var coordenadasFronteira = emptyArray<Pair<Int, Int>>()
    var count = 0
    while (count < coordenadasNavio.size) {
        val (a, b) = coordenadasNavio[count]
        val coordenadaFronteiraLocal = arrayOf(
            Pair(a + 2 - 1, b + 1 - 1),
            Pair(a + 2 - 1, b + 2 - 1),
            Pair(a + 1 - 1, b + 2 - 1),
            Pair(a - 1, b - 1),
            Pair(a - 1, b + 1 - 1),
            Pair(a + 1 - 1, b - 1),
            Pair(a + 2 - 1, b - 1),
            Pair(a - 1, b + 2 - 1)
        )
        coordenadasFronteira = juntarCoordenadas(coordenadasFronteira, coordenadaFronteiraLocal)
        count++
    }
    coordenadasFronteira = limparCoordenadasVazias(coordenadasFronteira)
    coordenadasFronteira = limparCoordenadasInvalidas(tabuleiro, coordenadasFronteira)
    return removerCoordenadasRepetidas(filtrarCoordenadas(coordenadasNavio, coordenadasFronteira))


}

fun estaLivre(tabuleiro: Array<Array<Char?>>, coordenadas: Array<Pair<Int, Int>>): Boolean {
    var count = 0
    while (count < coordenadas.size) {
        var (a, b) = coordenadas[count]
        if (tabuleiro[a - 1][b - 1] != null) {
            return false
        }
        count++
    }
    return true

}

fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, dimensao: Int): Boolean {
    val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, linha, coluna, "E", dimensao)
    val coordenadasFronteira = gerarCoordenadasFronteira(tabuleiro, linha, coluna, "E", dimensao)
    if (coordenadasNavio.size == 0) {
        return false
    }

    var count2 = 0
    while (count2 < coordenadasFronteira.size) {
        count2++
    }


    var coordenadasTotal = juntarCoordenadas(coordenadasNavio, coordenadasFronteira)
    coordenadasTotal = limparCoordenadasInvalidas(tabuleiro, coordenadasTotal)
    if (estaLivre(tabuleiro, coordenadasTotal)) {
        var count = 0
        while (count < coordenadasNavio.size) {
            val (a, b) = coordenadasNavio[count]
            tabuleiro[a - 1][b - 1] = dimensao.digitToChar()
            count++
        }

        return true
    }
    return false
}

fun insereNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Boolean {
    if (orientacao == "E") {
        return insereNavioSimples(tabuleiro, linha, coluna, dimensao)
    }
    val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, linha, coluna, orientacao, dimensao)
    if (coordenadasNavio.isEmpty()) {
        return false
    }
    var count = 0
    while (count < coordenadasNavio.size) {
        var (a, b) = coordenadasNavio[count]

        if (!coordenadaContida(tabuleiro, a, b)) {
            return false
        }
        count++
    }
    val coordenadasFronteira = gerarCoordenadasFronteira(tabuleiro, linha, coluna, orientacao, dimensao)


    var count2 = 0
    while (count2 < coordenadasFronteira.size) {
        val (a, b) = coordenadasFronteira[count2]
        count2++
    }
    var coordenadasTotal = juntarCoordenadas(coordenadasNavio, coordenadasFronteira)
    coordenadasTotal = limparCoordenadasInvalidas(tabuleiro, coordenadasTotal)
    if (estaLivre(tabuleiro, coordenadasTotal)) {
        var count = 0
        while (count < coordenadasNavio.size) {
            val (a, b) = coordenadasNavio[count]
            tabuleiro[a - 1][b - 1] = dimensao.digitToChar()
            count++
        }
        return true
    }
    return false
}


fun preencheTabuleiroComputador(tabuleiro: Array<Array<Char?>>, navios: Array<Int>) {
    var count = 0
    if(tabuleiro.size==4){
        val linhacoluna1 = 1
        val linhacoluna4 = 4
        insereNavioSimples(tabuleiro, linhacoluna1, linhacoluna1, 1)
        insereNavioSimples(tabuleiro, linhacoluna4, linhacoluna4, 1)
    }
    else{
    while (count < navios.size) {
        var count2 = 0
        while (count2 < navios[count]) {
            val linha = (0..(tabuleiro.size - 1)).random()
            val coluna = (0..(tabuleiro.size - 1)).random()
            val orientacao = "NSEO".random().toString()
            if (insereNavio(tabuleiro, linha, coluna, orientacao, count+1)) {
                count2++
            }
        }
        count++
    }}
}

fun navioCompleto(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {
    val linhaProcessada = linha
    val colunaProcessada = coluna
    val posicao = tabuleiro[linhaProcessada - 1][colunaProcessada - 1]
    if (posicao == null) {
        return false
    } else if (posicao == '1') {
        return true
    } else if (posicao == '2') {
        val fronteiras = gerarCoordenadasFronteira(tabuleiro, linhaProcessada, colunaProcessada, "E", 1)
        var count = 0
        while (count < fronteiras.size) {
            var (a, b) = fronteiras[count]
            if (tabuleiro[a - 1][b - 1] == posicao) {
                return true
            }
            count++
        }
        return false
    } else if (posicao == '3') {
        var count = 0
        var countTotal = 0
        while (count < tabuleiro.size) {
            var count2 = 0
            while (count2 < tabuleiro.size) {
                if (tabuleiro[count][count2] == '3') {
                    countTotal++
                }
                count2++
            }
            count++
        }
        if (countTotal == 3) {
            return true
        }
        return false
    } else if (posicao == '4') {
        var count = 0
        var countTotal = 0
        while (count < tabuleiro.size) {
            var count2 = 0
            while (count2 < tabuleiro.size) {
                if (tabuleiro[count][count2] == '4') {
                    countTotal++
                }
                count2++
            }
            count++
        }
        if (countTotal == 4) {
            return true
        }
        return false
    }
    return false
}

fun obtemMapa(tabuleiro: Array<Array<Char?>>, tabuleiroReal: Boolean): Array<String> {
    val legenda = "| " + criaLegendaHorizontal(tabuleiro.size) + " |"
    var terreno = arrayOf(legenda)
    if (tabuleiroReal) {
        val linha = "|"

        var countLinhas = 0
        while (countLinhas < tabuleiro.size) {
            var linhaString = linha
            var countColunas = 0
            while (countColunas < tabuleiro.size) {
                if (tabuleiro[countLinhas][countColunas] == null) {
                    linhaString += " ~ |"
                } else {
                    linhaString += " " + tabuleiro[countLinhas][countColunas] + " |"
                }
                countColunas++
            }
            linhaString += " " + (countLinhas + 1)
            terreno += linhaString
            countLinhas++
        }
    } else {
        val linha = "|"
        var countLinhas = 0
        while (countLinhas < tabuleiro.size) {
            var linhaString = linha
            var countColunas = 0
            while (countColunas < tabuleiro.size) {
                if (tabuleiro[countLinhas][countColunas] == null) {
                    linhaString += " ? |"
                } else if (tabuleiro[countLinhas][countColunas] == 'X') {
                    linhaString += " X |"
                } else {
                    if (navioCompleto(tabuleiro, countLinhas + 1, countColunas + 1)) {
                        linhaString += " " + tabuleiro[countLinhas][countColunas] + " |"
                    } else {
                        if (tabuleiro[countLinhas][countColunas] == '1') {
                            linhaString += " \u2081 |"
                        } else if (tabuleiro[countLinhas][countColunas] == '2') {
                            linhaString += " \u2082 |"
                        } else if (tabuleiro[countLinhas][countColunas] == '3') {
                            linhaString += " \u2083 |"
                        } else if (tabuleiro[countLinhas][countColunas] == '4') {
                            linhaString += " \u2084 |"
                        }
                    }
                }
                countColunas++
            }
            linhaString += " " + (countLinhas + 1)
            terreno += linhaString
            countLinhas++
        }
    }
    return terreno
}

fun lancarTiro(
    tabuleiroReal: Array<Array<Char?>>, tabuleiroPalpites: Array<Array<Char?>>, coordenadas: Pair<Int, Int>?
): String {
    if (coordenadas == null) {
        return ""
    }
    var (a, b) = coordenadas
    if (tabuleiroReal[a - 1][b - 1] == null) {
        tabuleiroPalpites[a - 1][b - 1] = 'X'
        return "Agua."
    } else if (tabuleiroReal[a - 1][b - 1] == '1') {
        tabuleiroPalpites[a - 1][b - 1] = '1'
        return "Tiro num submarino."
    } else if (tabuleiroReal[a - 1][b - 1] == '2') {
        tabuleiroPalpites[a - 1][b - 1] = '2'
        return "Tiro num contra-torpedeiro."
    } else if (tabuleiroReal[a - 1][b - 1] == '3') {
        tabuleiroPalpites[a - 1][b - 1] = '3'
        return "Tiro num navio-tanque."
    } else if (tabuleiroReal[a - 1][b - 1] == '4') {
        tabuleiroPalpites[a - 1][b - 1] = '4'
        return "Tiro num porta-avioes."
    }
    return "Agua."
}

fun geraTiroComputador(tabuleiroPalpitesComputador: Array<Array<Char?>>): Pair<Int, Int> {
    var tiroValido = false
    while (!tiroValido) {
        val linha = (0..tabuleiroPalpitesComputador.size - 1).random()
        val coluna = (0..tabuleiroPalpitesComputador.size - 1).random()
        if (tabuleiroPalpitesComputador[linha][coluna] == null) {
            tiroValido = true
            return Pair(linha + 1, coluna + 1)
        }
    }
    return Pair(-1, -1)
}

fun contarNaviosDeDimensao(tabuleiro: Array<Array<Char?>>, dimensao: Int): Int {
    var numNavios = 0
    if (dimensao == 1) {
        var count = 0
        while (count < tabuleiro.size) {
            var count2 = 0
            while (count2 < tabuleiro.size) {
                if (tabuleiro[count][count2] == '1') {
                    numNavios++
                }
                count2++
            }
            count++
        }
    } else if (dimensao == 2) {
        val tabuleiroTemp = criaTabuleiroVazio(tabuleiro.size, tabuleiro.size)
        var count = 0
        val fronteiras = emptyArray<Pair<Int, Int>>()
        while (count < tabuleiro.size) {
            var count2 = 0
            while (count2 < tabuleiro.size) {
                if (!(fronteiras.contains(Pair(count, count2)))) {
                    if (tabuleiro[count][count2] == '2') {
                        tabuleiroTemp[count][count2] = '2'
                        numNavios = contarNaviosHelper(count, count2, tabuleiroTemp, tabuleiro, numNavios)
                    }
                }
                count2++
            }
            count++
        }
    } else if (dimensao == 3) {
        var count = 0
        while (count < tabuleiro.size) {
            var count2 = 0
            while (count2 < tabuleiro.size) {
                if (tabuleiro[count][count2] == '3') {
                    if (navioCompleto(tabuleiro, count + 1, count2 + 1)) {
                        numNavios = 1  // apenas pode existir 1 navio de dimensao 3
                        return numNavios
                    }
                }
                count2++
            }
            count++
        }
    } else if (dimensao == 4) {
        var count = 0
        while (count < tabuleiro.size) {
            var count2 = 0
            while (count2 < tabuleiro.size) {
                if (tabuleiro[count][count2] == '4') {
                    if (navioCompleto(tabuleiro, count + 1, count2 + 1)) {
                        numNavios = 1  // apenas pode existir 1 navio de dimensao 4
                        return numNavios
                    }
                }
                count2++
            }
            count++
        }
    }
    return numNavios
}

fun contarNaviosHelper (count: Int, count2: Int, tabuleiroTemp: Array<Array<Char?>>, tabuleiro: Array<Array<Char?>>, numNavios: Int): Int{
    if (count == 0) {
        if (count2 == 0) {
            if (tabuleiroTemp[count + 1][count2] == '2' || tabuleiroTemp[count][count2 + 1] == '2') {
                return numNavios + 1
            }
        } else if (count2 == tabuleiro.size - 1) {
            if (tabuleiroTemp[count + 1][count2] == '2' || tabuleiroTemp[count][count2 - 1] == '2') {
                return numNavios + 1
            }
        } else {
            if (tabuleiroTemp[count + 1][count2] == '2' || tabuleiroTemp[count][count2 + 1] == '2' || tabuleiroTemp[count][count2 - 1] == '2') {
                return numNavios + 1
            }
        }
    } else if (count == tabuleiro.size - 1) {
        if (count2 == 0) {
            if (tabuleiroTemp[count - 1][count2] == '2' || tabuleiroTemp[count][count2 + 1] == '2') {
                return numNavios + 1
            }
        } else if (count2 == tabuleiro.size - 1) {
            if (tabuleiroTemp[count - 1][count2] == '2' || tabuleiroTemp[count][count2 - 1] == '2') {
                return numNavios + 1
            }
        } else {
            if (tabuleiroTemp[count - 1][count2] == '2' || tabuleiroTemp[count][count2 + 1] == '2' || tabuleiroTemp[count][count2 - 1] == '2') {
                return numNavios + 1
            }
        }
    } else {
        if (count2 == 0) {
            if (tabuleiroTemp[count + 1][count2] == '2' || tabuleiroTemp[count - 1][count2] == '2' || tabuleiroTemp[count][count2 + 1] == '2') {
                return numNavios + 1
            }
        } else if (count2 == tabuleiro.size - 1) {
            if (tabuleiroTemp[count + 1][count2] == '2' || tabuleiroTemp[count - 1][count2] == '2' || tabuleiroTemp[count][count2 - 1] == '2') {
                return numNavios + 1
            }
        } else {
            if (tabuleiroTemp[count + 1][count2] == '2' || tabuleiroTemp[count - 1][count2] == '2' ||
                tabuleiroTemp[count][count2 + 1] == '2' || tabuleiroTemp[count][count2 - 1] == '2') {
                return numNavios + 1
            }
        }
    }
    return numNavios
}

fun venceu(tabuleiro: Array<Array<Char?>>): Boolean {
    var count = 0
    val numNaviosExistentes = calculaNumNavios(tabuleiro.size, tabuleiro.size)
    while (count < numNaviosExistentes.size) {
        if (contarNaviosDeDimensao(tabuleiro, count + 1) < numNaviosExistentes[count]) {
            return false
        }
        count++
    }
    return true
}

fun lerJogo(nomeDoFicheiro: String, tipoDeTabuleiro: Int): Array<Array<Char?>> {
    val linhas = File(nomeDoFicheiro).readLines()
    val tamanhoTabuleiro = linhas[0].split(",")[0].toInt()
    val tabuleiro = criaTabuleiroVazio(tamanhoTabuleiro, tamanhoTabuleiro)
    if (tipoDeTabuleiro == 1) {
        var count = 0
        while (count < linhas.size) {
            if (linhas[count] == "Jogador" && linhas[count + 1] == "Real") {
                count += 2
                var count2 = 0
                while (count2 < tamanhoTabuleiro) {
                    val conteudoDaLinha = processarLinhasDoSave(linhas[count + count2])
                    tabuleiro[count2] = conteudoDaLinha
                    count2++
                }
            }
            count++
        }
        return tabuleiro
    } else if (tipoDeTabuleiro == 2) {
        var count = 0
        while (count < linhas.size) {
            if (linhas[count] == "Jogador" && linhas[count + 1] == "Palpites") {
                count += 2
                var count2 = 0
                while (count2 < tamanhoTabuleiro) {
                    val conteudoDaLinha = processarLinhasDoSave(linhas[count + count2])
                    tabuleiro[count2] = conteudoDaLinha
                    count2++
                }
            }
            count++
        }
        return tabuleiro
    } else if (tipoDeTabuleiro == 3) {
        var count = 0
        while (count < linhas.size) {
            if (linhas[count] == "Computador" && linhas[count + 1] == "Real") {
                count += 2
                var count2 = 0
                while (count2 < tamanhoTabuleiro) {
                    val conteudoDaLinha = processarLinhasDoSave(linhas[count + count2])
                    tabuleiro[count2] = conteudoDaLinha
                    count2++
                }
            }
            count++
        }
        return tabuleiro
    } else if (tipoDeTabuleiro == 4) {
        var count = 0
        while (count < linhas.size) {
            if (linhas[count] == "Computador" && linhas[count + 1] == "Palpites") {
                count += 2
                var count2 = 0
                while (count2 < tamanhoTabuleiro) {
                    val conteudoDaLinha = processarLinhasDoSave(linhas[count + count2])
                    tabuleiro[count2] = conteudoDaLinha
                    count2++
                }
            }
            count++
        }
        return tabuleiro
    }
    return tabuleiro
}

fun processarLinhasDoSave(inputString: String): Array<Char?> {
    val stringTemp = ","+inputString
    var valores = emptyArray<Char?>()
    var count = 0
    while (count<stringTemp.length){
        if(stringTemp[count]==','){
            if(count+1<stringTemp.length) {

                if (stringTemp[count + 1] != ',') {
                    valores += stringTemp[count + 1]
                    count += 2
                } else {
                    valores += null
                    count++
                }
            } else{
                valores+= null
                count ++
            }
        }
        else {
            count++
        }
    }
    return valores
}

fun gravarJogo(
    nomeDoFicheiro: String,
    tabuleiroRealHumano: Array<Array<Char?>>,
    tabuleiroPalpitesHumano: Array<Array<Char?>>,
    tabuleiroRealComputador: Array<Array<Char?>>,
    tabuleiroPalpitesComputador: Array<Array<Char?>>
) {
    var save = tabuleiroRealHumano.size.toString() + "," + tabuleiroRealHumano.size.toString() + "\n" + "\n"

    save += "Jogador\n" + "Real\n"
    var linha = 0
    while (linha < tabuleiroRealHumano.size) {
        var coluna = 0
        while (coluna < tabuleiroRealHumano.size) {
            if (tabuleiroRealHumano[linha][coluna] != null) {
                save += tabuleiroRealHumano[linha][coluna].toString()
            }
            if (coluna + 1 < tabuleiroRealHumano.size) {
                save += ","
            }
            coluna++
        }
        save += "\n"
        linha++
    }

    save += "\n" + "Jogador\n" + "Palpites\n"
    linha = 0
    while (linha < tabuleiroPalpitesHumano.size) {
        var coluna = 0
        while (coluna < tabuleiroPalpitesHumano.size) {
            if (tabuleiroPalpitesHumano[linha][coluna] != null) {
                save += tabuleiroPalpitesHumano[linha][coluna].toString()
            }
            if (coluna + 1 < tabuleiroPalpitesHumano.size) {
                save += ","
            }
            coluna++
        }
        save += "\n"
        linha++
    }
    save += "\n" + "Computador\n" + "Real\n"
    linha = 0
    while (linha < tabuleiroRealComputador.size) {
        var coluna = 0
        while (coluna < tabuleiroRealComputador.size) {
            if (tabuleiroRealComputador[linha][coluna] != null) {
                save += tabuleiroRealComputador[linha][coluna].toString()
            }
            if (coluna + 1 < tabuleiroRealComputador.size) {
                save += ","
            }
            coluna++
        }
        save += "\n"
        linha++
    }
    save += "\n" + "Computador\n" + "Palpites\n"
    linha = 0
    while (linha < tabuleiroPalpitesComputador.size) {
        var coluna = 0
        while (coluna < tabuleiroPalpitesComputador.size) {
            if (tabuleiroPalpitesComputador[linha][coluna] != null) {
                save += tabuleiroPalpitesComputador[linha][coluna].toString()
            }
            if (coluna + 1 < tabuleiroPalpitesComputador.size) {
                save += ","
            }
            coluna++
        }
        save += "\n"
        linha++
    }

    File(nomeDoFicheiro).writeText(save)
}

fun verificarCoordenadasNavio(
    tabuleiro: Array<Array<Char?>>, coordenadas: Array<Pair<Int, Int>>
): Array<Pair<Int, Int>> {
    var count = 0
    while (count < coordenadas.size) {

        val (a, b) = coordenadas[count]
        if (!coordenadaContida(tabuleiro, a, b)) {
            return emptyArray()
        }
        count++
    }
    return coordenadas
}

fun filtrarCoordenadas(
    coordenadasNavio: Array<Pair<Int, Int>>, coordenadasFronteira: Array<Pair<Int, Int>>
): Array<Pair<Int, Int>> {
    return coordenadasFronteira.filter { coord -> !coordenadasNavio.contains(coord) }.toTypedArray()
}

fun removerCoordenadasRepetidas(coordenadas: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    return coordenadas.distinct().toTypedArray()
}

fun limparCoordenadasInvalidas(
    tabuleiro: Array<Array<Char?>>, coordenadas: Array<Pair<Int, Int>>
): Array<Pair<Int, Int>> {
    return coordenadas.filter { it -> it.first > 0 && it.second > 0 && it.first <= tabuleiro.size && it.second <= tabuleiro.size }
        .toTypedArray()
}
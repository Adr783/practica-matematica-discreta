import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/*
 * Aquesta entrega consisteix en implementar tots els mètodes anomenats "exerciciX". Ara mateix la
 * seva implementació consisteix en llançar `UnsupportedOperationException`, ho heu de canviar així
 * com els aneu fent.
 *
 * Criteris d'avaluació:
 *
 * - Si el codi no compila tendreu un 0.
 *
 * - Les úniques modificacions que podeu fer al codi són:
 *    + Afegir un mètode (dins el tema que el necessiteu)
 *    + Afegir proves a un mètode "tests()"
 *    + Òbviament, implementar els mètodes que heu d'implementar ("exerciciX")
 *   Si feu una modificació que no sigui d'aquesta llista, tendreu un 0.
 *
 * - Principalment, la nota dependrà del correcte funcionament dels mètodes implementats (provant
 *   amb diferents entrades).
 *
 * - Tendrem en compte la neteja i organització del codi. Un estandard que podeu seguir és la guia
 *   d'estil de Google per Java: https://google.github.io/styleguide/javaguide.html . Per exemple:
 *    + IMPORTANT: Aquesta entrega està codificada amb UTF-8 i finals de línia LF.
 *    + Indentació i espaiat consistent
 *    + Bona nomenclatura de variables
 *    + Declarar les variables el més aprop possible al primer ús (és a dir, evitau blocs de
 *      declaracions).
 *    + Convé utilitzar el for-each (for (int x : ...)) enlloc del clàssic (for (int i = 0; ...))
 *      sempre que no necessiteu l'índex del recorregut. Igualment per while si no és necessari.
 *
 * Per com està plantejada aquesta entrega, no necessitau (ni podeu) utilitzar cap `import`
 * addicional, ni qualificar classes que no estiguin ja importades. El que sí podeu fer és definir
 * tots els mètodes addicionals que volgueu (de manera ordenada i dins el tema que pertoqui).
 *
 * Podeu fer aquesta entrega en grups de com a màxim 3 persones, i necessitareu com a minim Java 10.
 * Per entregar, posau els noms i cognoms de tots els membres del grup a l'array `Entrega.NOMS` que
 * està definit a la línia 53.
 *
 * L'entrega es farà a través d'una tasca a l'Aula Digital que obrirem abans de la data que se us
 * hagui comunicat. Si no podeu visualitzar bé algun enunciat, assegurau-vos de que el vostre editor
 * de texte estigui configurat amb codificació UTF-8.
 */
class Entrega {
  static final String[] NOMS = {Adrián Gozález Alcalá, Adrián Javier Ballesteros Romero, Adrián Reina Sorribas};

  /*
   * Aquí teniu els exercicis del Tema 1 (Lògica).
   */
  static class Tema1 {
    /*
     * Determinau si l'expressió és una tautologia o no:
     *
     * (((vars[0] ops[0] vars[1]) ops[1] vars[2]) ops[2] vars[3]) ...
     *
     * Aquí, vars.length == ops.length+1, i cap dels dos arrays és buid. Podeu suposar que els
     * identificadors de les variables van de 0 a N-1, i tenim N variables diferents (mai més de 20
     * variables).
     *
     * Cada ops[i] pot ser: CONJ, DISJ, IMPL, NAND.
     *
     * Retornau:
     *   1 si és una tautologia
     *   0 si és una contradicció
     *   -1 en qualsevol altre cas.
     *
     * Vegeu els tests per exemples.
     */
    static final char CONJ = '∧';
    static final char DISJ = '∨';
    static final char IMPL = '→';
    static final char NAND = '.';

    static int exercici1(char[] ops, int[] vars) {
      int variableMasGrande = -1; // Todas las variables deberían ser iguales o mayores a 0
      for (int var : vars) {
        if (var > variableMasGrande) {
          variableMasGrande = var;
        }
      }
  
      boolean[] valoresVars = new boolean[variableMasGrande + 1];
      boolean tautologia = true;
      boolean contradiccion = true;
      final int posibilidades = (int) Math.pow(2, valoresVars.length);
      // Comprueba todas las posibles combinaciones de variables para comprobar si todas son
      // verdaderas o falsas
      for (int i = 0; i < posibilidades; i++) {
        if (funcionProposicional(ops, vars, valoresVars)) {
          contradiccion = false;
        } else {
          tautologia = false;
        }
        valoresVars = contadorBinario(valoresVars);
      }
  
      if (tautologia) {
        return 1;
      } else if (contradiccion) {
        return 0;
      } else {
        return -1;
      }
    }

    // Supone que el numero binario tiene el bit mas significativo a la derecha
    // Cuando llega al maximo valor de numeroBinario vuelve a empezar en 0
    private static boolean[] contadorBinario(boolean[] numeroBinario) {
      int i = 0;
      int bits = numeroBinario.length;
      while (i < bits && numeroBinario[i]) {
        numeroBinario[i] = false;
        i++;
      }
      if (i < bits) {
        numeroBinario[i] = true;
      }
      return numeroBinario;
    }
  
    // Supone que la funcion proposicional se puede resolver de derecha a izquierda
    private static boolean funcionProposicional(char[] ops, int[] vars, boolean[] valoresVars) {
      boolean ultimoValor = valoresVars[vars[0]];
      for (int i = 0; i < ops.length; i++) {
        switch (ops[i]) {
          case '∧' ->
            ultimoValor = ultimoValor && valoresVars[vars[i]];
          case '∨' ->
            ultimoValor = ultimoValor || valoresVars[vars[i]];
          case '→' ->
            ultimoValor = ultimoValor || (!valoresVars[vars[i]]);
          case '.' ->
            ultimoValor = (!ultimoValor) || (!valoresVars[vars[i]]);
        }
      }
      return ultimoValor;
    }

    /*
     * Aquest mètode té de paràmetre l'univers (representat com un array) i els predicats
     * adients `p` i `q`. Per avaluar aquest predicat, si `x` és un element de l'univers, podeu
     * fer-ho com `p.test(x)`, que té com resultat un booleà (true si `P(x)` és cert).
     *
     * Amb l'univers i els predicats `p` i `q` donats, returnau true si la següent proposició és
     * certa.
     *
     * (∀x : P(x)) <-> (∃!x : Q(x))
     */
    static boolean exercici2(int[] universe, Predicate<Integer> p, Predicate<Integer> q) {
      int numeroXCumplenP = 0;
      int numeroXCumplenQ = 0;
      for (int x : universe) {
        if (p.test(x)) {
          numeroXCumplenP++;
        }
        if (q.test(x)) {
          numeroXCumplenQ++;
        }
      }
  
      boolean primerPredicado = numeroXCumplenP == universe.length; // Se cumple siempre
      boolean segundoPredicado = numeroXCumplenQ == 1; // Se cumple una vez
      return primerPredicado == segundoPredicado;
    }

    static void tests() {
      // Exercici 1
      // Taules de veritat

      // Tautologia: ((p0 → p2) ∨ p1) ∨ p0
      test(1, 1, 1, () -> exercici1(new char[] { IMPL, DISJ, DISJ }, new int[] { 0, 2, 1, 0 }) == 1);

      // Contradicció: (p0 . p0) ∧ p0
      test(1, 1, 2, () -> exercici1(new char[] { NAND, CONJ }, new int[] { 0, 0, 0 }) == 0);

      // Exercici 2
      // Equivalència

      test(1, 2, 1, () -> {
        return exercici2(new int[] { 1, 2, 3 }, (x) -> x == 0, (x) -> x == 0);
      });

      test(1, 2, 2, () -> {
        return exercici2(new int[] { 1, 2, 3 }, (x) -> x >= 1, (x) -> x % 2 == 0);
      });
    }
  }

  /*
   * Aquí teniu els exercicis del Tema 2 (Conjunts).
   *
   * Per senzillesa tractarem els conjunts com arrays (sense elements repetits). Per tant, un
   * conjunt de conjunts d'enters tendrà tipus int[][]. Podeu donar per suposat que tots els
   * arrays que representin conjunts i us venguin per paràmetre estan ordenats de menor a major.
   *
   * Les relacions també les representarem com arrays de dues dimensions, on la segona dimensió
   * només té dos elements. L'array estarà ordenat lexicogràficament. Per exemple
   *   int[][] rel = {{0,0}, {0,1}, {1,1}, {2,2}};
   * i també donarem el conjunt on està definida, per exemple
   *   int[] a = {0,1,2};
   * Als tests utilitzarem extensivament la funció generateRel definida al final (també la podeu
   * utilitzar si la necessitau).
   *
   * Les funcions f : A -> B (on A i B son subconjunts dels enters) les representam o bé amb el seu
   * graf o bé amb un objecte de tipus Function<Integer, Integer>. Sempre donarem el domini int[] a
   * i el codomini int[] b. En el cas de tenir un objecte de tipus Function<Integer, Integer>, per
   * aplicar f a x, és a dir, "f(x)" on x és d'A i el resultat f.apply(x) és de B, s'escriu
   * f.apply(x).
   */
  static class Tema2 {
    /*
     * Trobau el nombre de particions diferents del conjunt `a`, que podeu suposar que no és buid.
     *
     * Pista: Cercau informació sobre els nombres de Stirling.
     */
    static int exercici1(int[] a) {
      if (a.length == 0) {//Caso de conjunto vacio
        return 1;
      } else {
        int sol = 0;
        for (int i = 0; i <= a.length; i++) {//bucle por número elementos del conjunto
          sol = sol + stirling(a.length, i);
        }
        return sol;
      }
    }
  
    static int stirling(int x, int y) { //Calculo del numero de stirling
      if ((y == 0)) {
        return 0;
      } else if ((x == y) || (y == 1)) {
        return 1;
      } else {
        return y * stirling(x - 1, y) + stirling(x - 1, y - 1);
      }
    }
  
    /*
       * Trobau el cardinal de la relació d'ordre parcial sobre `a` més petita que conté `rel` (si
       * existeix). En altres paraules, el cardinal de la seva clausura reflexiva, transitiva i
       * antisimètrica.
       *
       * Si no existeix, retornau -1.
     */
    static int exercici2(int[] a, int[][] rel) {
      boolean posible = true;
      boolean reflexiva = false;
      boolean transitiva = true;
      int counter = 0;
      for (int i = 0; i < a.length; i++) { //Comprueba reflexividad
        for (int j = 0; j < rel.length; j++) {
          if (rel[j][0] == a[i] && rel[j][1] == a[i]) {
            reflexiva = true;
          }
        }
        if (!reflexiva) {
          counter = counter + 1;
        }
        reflexiva = false;
      }
      for (int j = 0; j < rel.length; j++) {//Comprueba transitividad
        for (int k = 0; k < rel.length; k++) {
          if (rel[j][1] == rel[k][0]) {
            transitiva = false;
            for (int m = 0; m < rel.length; m++) {
              if (rel[j][0] == rel[m][0] && rel[k][1] == rel[m][1]) {
                transitiva = true;
              }
            }
          }
        }
        if (!transitiva) {
          counter = counter + 1;
        }
        transitiva = true;
      }
      for (int j = 0; j < rel.length; j++) {//Comprueba antisimetria 
        for (int k = 0; k < rel.length; k++) {
          if (rel[j][0] == rel[k][1] && rel[j][1] == rel[k][0]) {
            if (!(rel[j][0] == rel[j][1])) {
              posible = false;
            }
          }
        }
      }
      if (posible) {
        return counter + rel.length;
      } else {
        return -1;
      }
    }
  
    /*
       * Donada una relació d'ordre parcial `rel` definida sobre `a` i un subconjunt `x` de `a`,
       * retornau:
       * - L'ínfim de `x` si existeix i `op` és false
       * - El suprem de `x` si existeix i `op` és true
       * - null en qualsevol altre cas
     */
    static Integer exercici3(int[] a, int[][] rel, int[] x, boolean op) {
      if (!op) {
        boolean isMin = true;
        boolean isInfim = false;
        for (int i = 0; i < rel.length; i++) {
          for (int j = 0; j < rel.length; j++) {
            if (rel[i][1] == rel[j][0] && rel[i][0] != rel[i][1]) {
              isMin = false;
            }
          }
          if (isMin) {
            for (int j = 0; j < x.length; j++) {
              if (!(rel[i][0] == x[j])) {
                isInfim = true;
              }
            }
          }
          if (isInfim) {
            return rel[i][1];
          }
        }
      } else {
        boolean isMax = true;
        boolean isSuprem = false;
        for (int i = 0; i < rel.length; i++) {
          for (int j = 0; j < rel.length; j++) {
            if (rel[i][0] == rel[j][1]) {
              isMax = false;
            }
          }
          if (isMax) {
            for (int j = 0; j < x.length; j++) {
              if (rel[i][1] == x[j]) {
                isSuprem = true;
              }
            }
          }
          if (isSuprem) {
            return rel[i][0];
          }
        }
      }
      return null;
    }
  
    /*
       * Donada una funció `f` de `a` a `b`, retornau:
       *  - El graf de la seva inversa (si existeix)
       *  - Sinó, el graf d'una inversa seva per l'esquerra (si existeix)
       *  - Sinó, el graf d'una inversa seva per la dreta (si existeix)
       *  - Sinó, null.
     */
    static int[][] exercici4(int[] a, int[] b, Function<Integer, Integer> f) {
      boolean inyectiva = true;
      boolean exaustiva = true;
      int[] temp = new int[a.length];
      for (int i = 1; i < a.length; i++) {
        temp[i] = f.apply(a[i]);
      }
      for (int i = 0; i < temp.length; i++) {
        boolean temp1 = true;
        for (int j = i + 1; j < temp.length; j++) {
          if (temp[i] == temp[j]) {
            temp1 = false;
          }
        }
        if (!(temp1)) {
          inyectiva = false;
        }
      }
      boolean temp1 = false;
      for (int i = 0; i < b.length; i++) {
        for (int j = i + 1; j < temp.length; j++) {
          if (b[i] == temp[j]) {
            temp1 = true;
          }
        }
        if (!(temp1)) {
          exaustiva = false;
        }
      }
      if (exaustiva && inyectiva) {
        int[][] grafoInvertido = new int[a.length][2];
        for (int i = 0; i < grafoInvertido.length; i++) {
          grafoInvertido[i][0] = temp[i];
          grafoInvertido[i][1] = a[i];
        }
        return grafoInvertido;
      }
      if (inyectiva) {
        int[][] grafoInvertido = new int[b.length][2];
        for (int i = 0; i < grafoInvertido.length; i++) {
          grafoInvertido[i][0] = b[i];
          grafoInvertido[i][1] = b[i]; //Función contraria a x -> x
        }
        return grafoInvertido;
      }
      if (exaustiva) {
        int[][] grafoInvertido = new int[b.length][2];
        for (int i = 0; i < grafoInvertido.length; i++) {
          grafoInvertido[i][0] = b[i];
          grafoInvertido[i][1] = b[i] * 2; //Función contraria a x -> x/2
        }
        return grafoInvertido;
      }
      return null;
    }

    /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
     */
    static void tests() {
      // Exercici 1
      // Nombre de particions

      test(2, 1, 1, () -> exercici1(new int[] { 1 }) == 1);
      test(2, 1, 2, () -> exercici1(new int[] { 1, 2, 3 }) == 5);

      // Exercici 2
      // Clausura d'ordre parcial

      final int[] INT02 = { 0, 1, 2 };

      test(2, 2, 1, () -> exercici2(INT02, new int[][] { {0, 1}, {1, 2} }) == 6);
      test(2, 2, 2, () -> exercici2(INT02, new int[][] { {0, 1}, {1, 0}, {1, 2} }) == -1);

      // Exercici 3
      // Ínfims i suprems

      final int[] INT15 = { 1, 2, 3, 4, 5 };
      final int[][] DIV15 = generateRel(INT15, (n, m) -> m % n == 0);
      final Integer ONE = 1;

      test(2, 3, 1, () -> ONE.equals(exercici3(INT15, DIV15, new int[] { 2, 3 }, false)));
      test(2, 3, 2, () -> exercici3(INT15, DIV15, new int[] { 2, 3 }, true) == null);

      // Exercici 4
      // Inverses

      final int[] INT05 = { 0, 1, 2, 3, 4, 5 };

      test(2, 4, 1, () -> {
        var inv = exercici4(INT05, INT02, (x) -> x/2);

        if (inv == null)
          return false;

        inv = lexSorted(inv);

        if (inv.length != INT02.length)
          return false;

        for (int i = 0; i < INT02.length; i++) {
          if (inv[i][0] != i || inv[i][1]/2 != i)
            return false;
        }

        return true;
      });

      test(2, 4, 2, () -> {
        var inv = exercici4(INT02, INT05, (x) -> x);

        if (inv == null)
          return false;

        inv = lexSorted(inv);

        if (inv.length != INT05.length)
          return false;

        for (int i = 0; i < INT02.length; i++) {
          if (inv[i][0] != i || inv[i][1] != i)
            return false;
        }

        return true;
      });
    }

    /*
     * Ordena lexicogràficament un array de 2 dimensions
     * Per exemple:
     *  arr = {{1,0}, {2,2}, {0,1}}
     *  resultat = {{0,1}, {1,0}, {2,2}}
     */
    static int[][] lexSorted(int[][] arr) {
      if (arr == null)
        return null;

      var arr2 = Arrays.copyOf(arr, arr.length);
      Arrays.sort(arr2, Arrays::compare);
      return arr2;
    }

    /*
     * Genera un array int[][] amb els elements {a, b} (a de as, b de bs) que satisfàn pred.test(a, b)
     * Per exemple:
     *   as = {0, 1}
     *   bs = {0, 1, 2}
     *   pred = (a, b) -> a == b
     *   resultat = {{0,0}, {1,1}}
     */
    static int[][] generateRel(int[] as, int[] bs, BiPredicate<Integer, Integer> pred) {
      var rel = new ArrayList<int[]>();

      for (int a : as) {
        for (int b : bs) {
          if (pred.test(a, b)) {
            rel.add(new int[] { a, b });
          }
        }
      }

      return rel.toArray(new int[][] {});
    }

    // Especialització de generateRel per as = bs
    static int[][] generateRel(int[] as, BiPredicate<Integer, Integer> pred) {
      return generateRel(as, as, pred);
    }
  }

  /*
   * Aquí teniu els exercicis del Tema 3 (Grafs).
   *
   * Els (di)grafs vendran donats com llistes d'adjacència (és a dir, tractau-los com diccionaris
   * d'adjacència on l'índex és la clau i els vèrtexos estan numerats de 0 a n-1). Per exemple,
   * podem donar el graf cicle no dirigit d'ordre 3 com:
   *
   * int[][] c3dict = {
   *   {1, 2}, // veïns de 0
   *   {0, 2}, // veïns de 1
   *   {0, 1}  // veïns de 2
   * };
   */
  static class Tema3 {
    /*
     * Determinau si el graf `g` (no dirigit) té cicles.
     */
    static boolean exercici1(int[][] g) {
      throw new UnsupportedOperationException("pendent");
    }

    /*
     * Determinau si els dos grafs són isomorfs. Podeu suposar que cap dels dos té ordre major que
     * 10.
     */
    static boolean exercici2(int[][] g1, int[][] g2) {
      throw new UnsupportedOperationException("pendent");
    }

    /*
     * Determinau si el graf `g` (no dirigit) és un arbre. Si ho és, retornau el seu recorregut en
     * postordre desde el vèrtex `r`. Sinó, retornau null;
     *
     * En cas de ser un arbre, assumiu que l'ordre dels fills vé donat per l'array de veïns de cada
     * vèrtex.
     */
    static int[] exercici3(int[][] g, int r) {
      throw new UnsupportedOperationException("pendent");
    }

    /*
     * Suposau que l'entrada és un mapa com el següent, donat com String per files (vegeu els tests)
     *
     *   _____________________________________
     *  |          #       #########      ####|
     *  |       O  # ###   #########  ##  ####|
     *  |    ####### ###   #########  ##      |
     *  |    ####  # ###   #########  ######  |
     *  |    ####    ###              ######  |
     *  |    ######################## ##      |
     *  |    ####                     ## D    |
     *  |_____________________________##______|
     *
     * Els límits del mapa els podeu considerar com els límits de l'array/String, no fa falta que
     * cerqueu els caràcters "_" i "|", i a més podeu suposar que el mapa és rectangular.
     *
     * Donau el nombre mínim de caselles que s'han de recorrer per anar de l'origen "O" fins al
     * destí "D" amb les següents regles:
     *  - No es pot sortir dels límits del mapa
     *  - No es pot passar per caselles "#"
     *  - No es pot anar en diagonal
     *
     * Si és impossible, retornau -1.
     */
    static int exercici4(char[][] mapa) {
      int[][] grafo = convertirEnGrafo(mapa);
      // Devuelve un array de los vertices adjacentes de cada vertice con 'O' el primero y 
      // 'D' el segundo
      final int numVertices = grafo.length;
      final int indiceOrigen = 0;
      final int indiceDestino = 1;
  
      boolean[] visitados = new boolean[numVertices];
      int[] distancias = new int[numVertices];
      for (int i = 0; i < numVertices; i++) {
        visitados[i] = false;
        distancias[i] = Integer.MAX_VALUE;
      }
      distancias[indiceOrigen] = 0;
  
      int verticeVisitado;
      int distanciaTentativa;
      for (int i = 0; i < numVertices; i++) {
        verticeVisitado = verticeMasCercanoNoVisitado(distancias, visitados);
        distanciaTentativa = distancias[verticeVisitado] + 1;
        for (int verticeAdjacente : grafo[verticeVisitado]) {
          // Si es -1 significa que no hay vertice adjacente
          if (verticeAdjacente != -1 && distanciaTentativa < distancias[verticeAdjacente]) {
            distancias[verticeAdjacente] = distanciaTentativa;
          }
        }
        visitados[verticeVisitado] = true;
      }
  
      if (distancias[1] < Integer.MAX_VALUE) {
        return distancias[indiceDestino];
      } else {
        return -1; // El laberinto no e puede resolver
      }
    }

    private static int[][] convertirEnGrafo(char[][] mapa) {
      int[][] mapaEnteros = new int[mapa.length][mapa[0].length];
      final int indiceOrigen = 0;
      final int indiceDestino = 1;
      // Añadir constante de -1
      int numeroVertices = 2;
      for (int i = 0; i < mapa.length; i++) {
        for (int j = 0; j < mapa[i].length; j++) {
          if (mapa[i][j] == 'O') {
            mapaEnteros[i][j] = indiceOrigen;
          } else if (mapa[i][j] == 'D') {
            mapaEnteros[i][j] = indiceDestino;
          } else if (mapa[i][j] == ' ') {
            mapaEnteros[i][j] = numeroVertices;
            numeroVertices++;
          } else if (mapa[i][j] == '#') {
            mapaEnteros[i][j] = -1;
          }
        }
      }
  
      int[][] grafo = new int[numeroVertices][4];
      for (int i = 0; i < mapa.length; i++) {
        for (int j = 0; j < mapa[i].length; j++) {
          if (mapa[i][j] != '#') {
            int[] verticesAdjacentes = new int[4];
            if (i == 0) {
              verticesAdjacentes[0] = -1;
            } else {
              verticesAdjacentes[0] = mapaEnteros[i - 1][j];
            }
            if (j == 0) {
              verticesAdjacentes[1] = -1;
            } else {
              verticesAdjacentes[1] = mapaEnteros[i][j - 1];
            }
            if (i == mapaEnteros.length - 1) {
              verticesAdjacentes[2] = -1;
            } else {
              verticesAdjacentes[2] = mapaEnteros[i + 1][j];
            }
            if (j == mapaEnteros[i].length - 1) {
              verticesAdjacentes[3] = -1;
            } else {
              verticesAdjacentes[3] = mapaEnteros[i][j + 1];
            }
            grafo[mapaEnteros[i][j]] = verticesAdjacentes;
          }
        }
      }
  
      return grafo;
    }

    private static int verticeMasCercanoNoVisitado(int[] distancias, boolean[] visitados) {
      int verticeMasPequeño = 0; // Deja el primer vertice si no encuentra ninguno
      // Así, si hay una parte aislada, el programa se queda en el vertice 0
      int distanciaMasPequeña = Integer.MAX_VALUE;
      for (int i = 0; i < distancias.length; i++) {
        if (distancias[i] < distanciaMasPequeña && !visitados[i]) {
          verticeMasPequeño = i;
          distanciaMasPequeña = distancias[i];
        }
      }
      return verticeMasPequeño;
    }
    
    /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
     */
    static void tests() {

      final int[][] D2 = { {}, {} };
      final int[][] C3 = { {1, 2}, {0, 2}, {0, 1} };

      final int[][] T1 = { {1, 2}, {0}, {0} };
      final int[][] T2 = { {1}, {0, 2}, {1} };

      // Exercici 1
      // G té cicles?

      test(3, 1, 1, () -> !exercici1(D2));
      test(3, 1, 2, () -> exercici1(C3));

      // Exercici 2
      // Isomorfisme de grafs

      test(3, 2, 1, () -> exercici2(T1, T2));
      test(3, 2, 2, () -> !exercici2(T1, C3));

      // Exercici 3
      // Postordre

      test(3, 3, 1, () -> exercici3(C3, 1) == null);
      test(3, 3, 2, () -> Arrays.equals(exercici3(T1, 0), new int[] { 1, 2, 0 }));

      // Exercici 4
      // Laberint

      test(3, 4, 1, () -> {
        return -1 == exercici4(new char[][] {
            " #O".toCharArray(),
            "D# ".toCharArray(),
            " # ".toCharArray(),
        });
      });

      test(3, 4, 2, () -> {
        return 8 == exercici4(new char[][] {
            "###D".toCharArray(),
            "O # ".toCharArray(),
            " ## ".toCharArray(),
            "    ".toCharArray(),
        });
      });
    }
  }

  /*
   * Aquí teniu els exercicis del Tema 4 (Aritmètica).
   *
   * En aquest tema no podeu:
   *  - Utilitzar la força bruta per resoldre equacions: és a dir, provar tots els nombres de 0 a n
   *    fins trobar el que funcioni.
   *  - Utilitzar long, float ni double.
   *
   * Si implementau algun dels exercicis així, tendreu un 0 d'aquell exercici.
   */
  static class Tema4 {
    /*
     * Primer, codificau el missatge en blocs de longitud 2 amb codificació ASCII. Després encriptau
     * el missatge utilitzant xifrat RSA amb la clau pública donada.
     *
     * Per obtenir els codis ASCII del String podeu utilitzar `msg.getBytes()`.
     *
     * Podeu suposar que:
     * - La longitud de `msg` és múltiple de 2
     * - El valor de tots els caràcters de `msg` està entre 32 i 127.
     * - La clau pública (n, e) és de la forma vista a les transparències.
     * - n és major que 2¹⁴, i n² és menor que Integer.MAX_VALUE
     *
     * Pista: https://en.wikipedia.org/wiki/Exponentiation_by_squaring
     */
    static int[] exercici1(String msg, int n, int e) {
      byte[][] parejas = extraeBloquesBytes(msg.getBytes(), 2);
      int[] mensajeCifrado = new int[parejas.length];
      final int numeroPosiblesCaracteres = 128; // Byte puede arrojar 128 caracteres diferentes
      for (int i = 0; i < parejas.length; i++) {
        int m = parejas[i][0] * numeroPosiblesCaracteres + parejas[i][1];
        mensajeCifrado[i] = calcularExponenteModN(m, e, n); // Calcula m^e mod n
      }
      return mensajeCifrado;
    }

    private static byte[][] extraeBloquesBytes(byte[] bytes, int n) {
      byte[][] bloques = new byte[bytes.length / n][n];
      for (int i = 0; i < bloques.length; i++) {
        for (int j = 0; j < n; j++) {
          bloques[i][j] = bytes[i * n + j];
        }
      }
      return bloques;
    }

    private static int calcularExponenteModN(int m, int e, int n) {
      if (1 == e) {
        return m % n;
      } else if (e % 2 == 0) {
        int mitadExponente = calcularExponenteModN(m, e / 2, n);
        return (mitadExponente * mitadExponente) % n;
      } else { // Si es impar
        return (calcularExponenteModN(m, e - 1, n) * m) % n;
      }
    }

    /*
     * Primer, desencriptau el missatge utilitzant xifrat RSA amb la clau pública donada. Després
     * descodificau el missatge en blocs de longitud 2 amb codificació ASCII (igual que l'exercici
     * anterior, però al revés).
     *
     * Per construir un String a partir d'un array de bytes podeu fer servir el constructor
     * `new String(byte[])`. Si heu de factoritzar algun nombre, ho podeu fer per força bruta.
     *
     * També podeu suposar que:
     * - La longitud del missatge original és múltiple de 2
     * - El valor de tots els caràcters originals estava entre 32 i 127.
     * - La clau pública (n, e) és de la forma vista a les transparències.
     * - n és major que 2¹⁴, i n² és menor que Integer.MAX_VALUE
     */
    static String exercici2(int[] m, int n, int e) {
      throw new UnsupportedOperationException("pendent");
    }

    static void tests() {
      // Exercici 1
      // Codificar i encriptar
      test(4, 1, 1, () -> {
        var n = 2*8209;
        var e = 5;

        var encr = exercici1("Patata", n, e);
        return Arrays.equals(encr, new int[] { 4907, 4785, 4785 });
      });

      // Exercici 2
      // Desencriptar i decodificar
      test(4, 2, 1, () -> {
        var n = 2*8209;
        var e = 5;

        var encr = new int[] { 4907, 4785, 4785 };
        var decr = exercici2(encr, n, e);
        return "Patata".equals(decr);
      });
    }
  }

  /*
   * Aquest mètode `main` conté alguns exemples de paràmetres i dels resultats que haurien de donar
   * els exercicis. Podeu utilitzar-los de guia i també en podeu afegir d'altres (no els tendrem en
   * compte, però és molt recomanable).
   *
   * Podeu aprofitar el mètode `test` per comprovar fàcilment que un valor sigui `true`.
   */
  public static void main(String[] args) {
    System.out.println("---- Tema 1 ----");
    Tema1.tests();
    System.out.println("---- Tema 2 ----");
    Tema2.tests();
    System.out.println("---- Tema 3 ----");
    Tema3.tests();
    System.out.println("---- Tema 4 ----");
    Tema4.tests();
  }

  // Informa sobre el resultat de p, juntament amb quin tema, exercici i test es correspon.
  static void test(int tema, int exercici, int test, BooleanSupplier p) {
    try {
      if (p.getAsBoolean())
        System.out.printf("Tema %d, exercici %d, test %d: OK\n", tema, exercici, test);
      else
        System.out.printf("Tema %d, exercici %d, test %d: Error\n", tema, exercici, test);
    } catch (Exception e) {
      if (e instanceof UnsupportedOperationException && "pendent".equals(e.getMessage())) {
        System.out.printf("Tema %d, exercici %d, test %d: Pendent\n", tema, exercici, test);
      } else {
        System.out.printf("Tema %d, exercici %d, test %d: Excepció\n", tema, exercici, test);
        e.printStackTrace();
      }
    }
  }
}

// vim: set textwidth=100 shiftwidth=2 expandtab :

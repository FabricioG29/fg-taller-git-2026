#!/usr/bin/env bash
# Recorrido de la colección POO-09 con curl. Guarda cada respuesta en insomnia/respuestas-poo09/NN.txt
# Uso: levantar A2 (:8081) y A3 (:8082), borrar data/ y levantar A1 (:8080); después: bash insomnia/recorrido-poo09.sh
A1=${A1:-http://localhost:8080}; A2=${A2:-http://localhost:8081}; A3=${A3:-http://localhost:8082}
DIR="$(dirname "$0")/respuestas-poo09"; mkdir -p "$DIR"
p(){ n="$1"; m="$2"; u="$3"; b="$4"; f="$DIR/${n%% *}.txt"
  { echo "### $n"; echo "$m $u"; [ -n "$b" ] && echo "$b"; echo;
    if [ -n "$b" ]; then curl -s -w "\n-> HTTP %{http_code}\n" -X "$m" -H "Content-Type: application/json" -d "$b" "$u";
    else curl -s -w "\n-> HTTP %{http_code}\n" -X "$m" "$u"; fi; } > "$f"
  echo; cat "$f"; }
p "00 GET OpenAPI propio (A1)" GET "$A1/v3/api-docs" >/dev/null; head -c 400 "$DIR/00.txt"; echo
p "01 GET OpenAPI de A2 (Matias Enciso)" GET "$A2/v3/api-docs" >/dev/null; head -c 400 "$DIR/01.txt"; echo
p "02 GET OpenAPI de A3 (Diana Aquino)" GET "$A3/v3/api-docs" >/dev/null; head -c 400 "$DIR/02.txt"; echo
p "03 POST jugador Fabri (TERRORISTAS)" POST "$A1/api/jugadores" '{"nombre": "Fabri", "equipo": "TERRORISTAS"}'
p "04 POST jugador Rival (ANTITERRORISTAS)" POST "$A1/api/jugadores" '{"nombre": "Rival", "equipo": "ANTITERRORISTAS"}'
p "05 POST jugador Compa (TERRORISTAS, mismo equipo que Fabri)" POST "$A1/api/jugadores" '{"nombre": "Compa", "equipo": "TERRORISTAS"}'
p "06 POST granada HE #1" POST "$A1/api/armas" '{"tipo": "granada", "nombre": "HE Grenade #1", "precio": 300, "equipo": "AMBOS", "danio": 98, "radioExplosion": 5.0, "tiempoActivacion": 1.5, "efecto": "Explosión de fragmentación"}'
p "07 POST granada HE #2" POST "$A1/api/armas" '{"tipo": "granada", "nombre": "HE Grenade #2", "precio": 300, "equipo": "AMBOS", "danio": 98, "radioExplosion": 5.0, "tiempoActivacion": 1.5, "efecto": "Explosión de fragmentación"}'
p "08 POST granada HE #3" POST "$A1/api/armas" '{"tipo": "granada", "nombre": "HE Grenade #3", "precio": 300, "equipo": "AMBOS", "danio": 98, "radioExplosion": 5.0, "tiempoActivacion": 1.5, "efecto": "Explosión de fragmentación"}'
p "09 POST escenario: Fabri lanza la HE #1 a Rival a 2 m → 201" POST "$A1/api/escenarios/granada" '{"atacanteId": 1, "objetivoId": 2, "granadaId": 1, "distancia": 2.0}'
p "10 GET jugadas guardadas → 1" GET "$A1/api/escenarios/jugadas" ''
p "11 GET Rival después de la jugada" GET "$A1/api/jugadores/2" ''
p "12 GET HE #1 → usada" GET "$A1/api/armas/1" ''
p "13 POST rechazo de A2: distancia negativa → 409 RECHAZO_A2" POST "$A1/api/escenarios/granada" '{"atacanteId": 1, "objetivoId": 2, "granadaId": 2, "distancia": -3.0}'
p "14 POST rechazo de A3: fuego amigo (Fabri → Compa) → 409 RECHAZO_A3" POST "$A1/api/escenarios/granada" '{"atacanteId": 1, "objetivoId": 3, "granadaId": 2, "distancia": 2.0}'
p "15 GET jugadas → sigue 1" GET "$A1/api/escenarios/jugadas" ''
p "16 GET HE #2 → sigue sin usar" GET "$A1/api/armas/2" ''
p "17 GET Compa → vida 100" GET "$A1/api/jugadores/3" ''
p "18 POST lanzar otra vez la HE #1 en el escenario → 409 GRANADA_USADA" POST "$A1/api/escenarios/granada" '{"atacanteId": 1, "objetivoId": 2, "granadaId": 1, "distancia": 2.0}'
p "19 POST simulación con la HE #3 → 201" POST "$A1/api/simulaciones/disparos" '{"atacanteId": 1, "objetivoId": 2, "armaId": 3, "veces": 1}'
p "20 POST simulación con la HE #3 otra vez → 409 GRANADA_USADA" POST "$A1/api/simulaciones/disparos" '{"atacanteId": 1, "objetivoId": 2, "armaId": 3, "veces": 1}'
p "21 POST regla munición: alcanza → 200" POST "$A1/api/reglas/municion" '{"cargador": 3, "disparos": 2}'
p "22 POST regla munición: no alcanza → 409 SIN_MUNICION" POST "$A1/api/reglas/municion" '{"cargador": 3, "disparos": 5}'
p "23 POST regla daño: chaleco absorbe la mitad → 200" POST "$A1/api/reglas/danio" '{"vida": 100, "armadura": 100, "danio": 36}'
p "24 POST regla daño: jugador eliminado → 422" POST "$A1/api/reglas/danio" '{"vida": 0, "armadura": 50, "danio": 10}'

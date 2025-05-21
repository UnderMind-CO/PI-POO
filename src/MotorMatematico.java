public class MotorMatematico {
    public double evaluarFuncion(String funcion, double x) throws ExcepcionMatematica {
        try {
            // Normaliza la función para soportar notación polinómica estándar
            String expr = funcion
                .replaceAll("(?<=[0-9])x", "*x") // 3x -> 3*x
                .replaceAll("([+-])x\\^([+-]?[0-9]+)", "$1 1*x^$2") // -x^2 -> -1*x^2
                .replaceAll("([+-]?[0-9]+)x\\^([+-]?[0-9]+)", "$1*x^$2") // 3x^2 -> 3*x^2
                .replaceAll("([+-])x([^a-zA-Z0-9^]|$)", "$11*x$2") // -x -> -1*x
                .replaceAll("\\+\\+", "+").replaceAll("--", "+").replaceAll("-\\+", "-").replaceAll("\\+-", "-");
            expr = expr.replaceAll("(?<![a-zA-Z0-9_])x(?![a-zA-Z0-9_])", "(" + x + ")");
            return evaluarExpresion(expr);
        } catch (Exception e) {
            throw new ExcepcionMatematica("Error en evaluación: " + e.getMessage());
        }
    }

    private double evaluarExpresion(String expresion) {
        return new Object() {
            int pos = -1, ch;
            void nextChar() { ch = (++pos < expresion.length()) ? expresion.charAt(pos) : -1; }
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expresion.length()) throw new RuntimeException("Caracter inesperado: " + (char) ch);
                return x;
            }
            // Soporta suma y resta
            double parseExpression() {
                double x = parseTerm();
                while (true) {
                    if      (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }
            // Soporta multiplicación y división
            double parseTerm() {
                double x = parseFactor();
                while (true) {
                    if      (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }
            // Soporta potencias y funciones
            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();
                double x;
                int startPos = pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expresion.substring(startPos, pos));
                } else if (ch >= 'a' && ch <= 'z') {
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = expresion.substring(startPos, pos);
                    if (eat('(')) {
                        double a = parseExpression();
                        double b = 0;
                        if (func.equals("pow")) {
                            eat(',');
                            b = parseExpression();
                        }
                        eat(')');
                        // Usar if-else clásico para compatibilidad con Java 8+
                        if (func.equals("sqrt")) x = Math.sqrt(a);
                        else if (func.equals("sin")) x = Math.sin(a);
                        else if (func.equals("cos")) x = Math.cos(a);
                        else if (func.equals("tan")) x = Math.tan(a);
                        else if (func.equals("cot")) x = 1.0 / Math.tan(a);
                        else if (func.equals("sec")) x = 1.0 / Math.cos(a);
                        else if (func.equals("csc")) x = 1.0 / Math.sin(a);
                        else if (func.equals("asin")) x = Math.asin(a);
                        else if (func.equals("acos")) x = Math.acos(a);
                        else if (func.equals("atan")) x = Math.atan(a);
                        else if (func.equals("sinh")) x = Math.sinh(a);
                        else if (func.equals("cosh")) x = Math.cosh(a);
                        else if (func.equals("tanh")) x = Math.tanh(a);
                        else if (func.equals("log")) x = Math.log(a);
                        else if (func.equals("abs")) x = Math.abs(a);
                        else if (func.equals("exp")) x = Math.exp(a);
                        else if (func.equals("pow")) x = Math.pow(a, b);
                        else throw new RuntimeException("Función desconocida: " + func);
                    } else {
                        throw new RuntimeException("Función mal formada: " + func);
                    }
                } else if (ch == 'x') {
                    // Soporte para la variable x
                    nextChar();
                    x = getXValue();
                } else {
                    throw new RuntimeException("Carácter inesperado: " + (char) ch);
                }
                // Soporte para potencia ^, incluyendo exponentes negativos y anidados
                while (eat('^')) {
                    double exp = parseFactor();
                    x = Math.pow(x, exp);
                }
                return x;
            }
            // Método auxiliar para obtener el valor de x
            double getXValue() {
                // Aquí x ya fue reemplazada por su valor, pero si queda alguna, la tratamos como 1.0
                return 1.0;
            }
        }.parse();
    }
}

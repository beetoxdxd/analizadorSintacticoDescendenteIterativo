import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class ASDI implements Parser {

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;
    private List<TipoToken> terminales = new ArrayList<>();


    public ASDI(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse(){
        int band=0;
        Stack pila=new Stack();
        setTerminales();

        pila.push(TipoToken.EOF);
        pila.push("Q");

        while(i < tokens.size()){
            Iterator<TipoToken> iter = terminales.iterator();
            while (iter.hasNext()){
                if(pila.peek() == iter.next()) band = 1;
            }

            if(pila.peek() == TipoToken.EOF){
                System.out.println("Consulta correcta");
                return true;
            }

            if(band == 1){
                match((TipoToken) pila.peek());
                if(hayErrores) return false;
                pila.pop();
                band = 0;
            } else {
                if(pila.peek() == "Q"){
                    if(preanalisis.tipo == TipoToken.SELECT){
                        pila.pop();
                        pila.push("T");
                        pila.push(TipoToken.FROM);
                        pila.push("D");
                        pila.push(TipoToken.SELECT);
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba 'select'");
                        return false;
                    }
                } else if(pila.peek() == "D"){
                    if(preanalisis.tipo == TipoToken.DISTINCT){
                        pila.pop();
                        pila.push("P");
                        pila.push(TipoToken.DISTINCT);
                    } else if(preanalisis.tipo == TipoToken.ASTERISCO || preanalisis.tipo == TipoToken.IDENTIFICADOR){
                        pila.pop();
                        pila.push("P");
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba 'distinct', '*' o 'identificador'");
                        return false;
                    }
                } else if(pila.peek() == "P"){
                    if(preanalisis.tipo == TipoToken.ASTERISCO){
                        pila.pop();
                        pila.push(TipoToken.ASTERISCO);
                    } else if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                        pila.pop();
                        pila.push("A");
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba '*' o 'identificador'");
                        return false;
                    }
                } else if(pila.peek() == "A"){
                    if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                        pila.pop();
                        pila.push("A1");
                        pila.push("A2");
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba 'identificador'");
                        return false;
                    }
                } else if(pila.peek() == "A1"){
                    if(preanalisis.tipo == TipoToken.COMA){
                        pila.pop();
                        pila.push("A");
                        pila.push(TipoToken.COMA);
                    } else if(preanalisis.tipo == TipoToken.FROM){
                        pila.pop();
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba ',' o 'from'");
                        return false;
                    }
                } else if(pila.peek() == "A2"){
                    if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                        pila.pop();
                        pila.push("A3");
                        pila.push(TipoToken.IDENTIFICADOR);
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba 'identificador'");
                        return false;
                    }
                } else if(pila.peek() == "A3"){
                    if(preanalisis.tipo == TipoToken.PUNTO){
                        pila.pop();
                        pila.push(TipoToken.IDENTIFICADOR);
                        pila.push(TipoToken.PUNTO);
                    } else if(preanalisis.tipo == TipoToken.COMA || preanalisis.tipo == TipoToken.FROM){
                        pila.pop();
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba 'from', ',' o '.'");
                        return false;
                    }
                } else if(pila.peek() == "T"){
                    if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                        pila.pop();
                        pila.push("T1");
                        pila.push("T2");
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba 'identificador'");
                        return false;
                    }
                } else if(pila.peek() == "T1"){
                    if(preanalisis.tipo == TipoToken.COMA){
                        pila.pop();
                        pila.push("T");
                        pila.push(TipoToken.COMA);
                    } else if(preanalisis.tipo == TipoToken.EOF){
                        pila.pop();
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba ',' o 'EOF'");
                        return false;
                    }
                } else if(pila.peek() == "T2"){
                    if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                        pila.pop();
                        pila.push("T3");
                        pila.push(TipoToken.IDENTIFICADOR);
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba 'identificador'");
                        return false;
                    }
                } else if(pila.peek() == "T3"){
                    if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                        pila.pop();
                        pila.push(TipoToken.IDENTIFICADOR);
                    } else if(preanalisis.tipo == TipoToken.COMA || preanalisis.tipo == TipoToken.EOF){
                        pila.pop();
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba ',', 'identificador' o 'EOF'");
                        return false;
                    }
                }
            }
        }

        return false;
    }

    private void match(TipoToken tt){
        if(preanalisis.tipo == tt){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println("ERROR IDENTIFICADO: Se esperaba " + tt);
        }

    }

    private void setTerminales(){
        terminales.add(TipoToken.SELECT);
        terminales.add(TipoToken.FROM);
        terminales.add(TipoToken.DISTINCT);
        terminales.add(TipoToken.ASTERISCO);
        terminales.add(TipoToken.COMA);
        terminales.add(TipoToken.IDENTIFICADOR);
        terminales.add(TipoToken.PUNTO);
    }
}

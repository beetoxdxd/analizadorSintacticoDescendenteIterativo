import java.util.ArrayList;
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
        Stack pila=new Stack();
        setTerminales();
        pila.push(TipoToken.EOF);
        pila.push("Q");
        System.out.println(terminales);
        while(i < tokens.size()){
            if(pila.peek() == terminales){
                System.out.println("hola");
            } else {
                if(pila.peek() == "Q"){
                    System.out.println("bien");
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
                }
            }

            i++;
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
            System.out.println("Error encontrado");
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

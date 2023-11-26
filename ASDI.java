import java.util.List;

public class ASDI implements Parser {

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;


    public ASDI(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse(){

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
}

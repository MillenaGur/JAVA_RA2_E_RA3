package exceptions;

public class PacienteNaoEncontradoException extends Exception {
    
    public PacienteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
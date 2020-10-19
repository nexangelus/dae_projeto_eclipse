package exceptions;

public class MyIllegalArgumentException extends Exception {
    public MyIllegalArgumentException(String msg){
        super(msg);
    }
    public MyIllegalArgumentException(){}
}

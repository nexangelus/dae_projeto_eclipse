package exceptions;

import javax.ws.rs.core.Response;

public class MyWebApplicationException extends Exception {
    public MyWebApplicationException(String msg){
        super(msg);
    }
    public MyWebApplicationException(){}

}

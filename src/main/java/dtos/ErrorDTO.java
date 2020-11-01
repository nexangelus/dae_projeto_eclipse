package dtos;

import java.io.Serializable;

public class ErrorDTO implements Serializable {
	private String message;

	public ErrorDTO(String message) {
		this.message = message;
	}

	public ErrorDTO() {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static ErrorDTO error(String message) {
		return new ErrorDTO(message);
	}
}

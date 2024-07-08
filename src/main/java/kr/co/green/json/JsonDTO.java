package kr.co.green.json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class JsonDTO {
	private String name;
	private int age;
	private boolean isAdmin;
}

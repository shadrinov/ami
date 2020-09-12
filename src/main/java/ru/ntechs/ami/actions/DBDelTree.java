package ru.ntechs.ami.actions;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.ntechs.ami.AMI;

@Getter
@Setter
@ToString
public class DBDelTree extends Action {
	String family;
	String key;

	public DBDelTree(AMI ami) {
		super(ami, "DBDelTree");
	}

	@Override
	public ArrayList<String> getMessageText() {
		ArrayList<String> request = super.getMessageText();

		if (family != null)
			request.add(String.format("Family: %s", family));

		if (key != null)
			request.add(String.format("Key: %s", key));

		return request;
	}
}

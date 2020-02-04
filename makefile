Menu.class: Menu.java
	javac -g Menu.java

User.class: User.java
	javac -g User.java

Account.class: Account.java
	javac -g Account.java

HashPassword.class: HashPassword.java
	javac -g HashPassword.java

StandardPassword.class: StandardPassword.java
	javac -g StandardPassword.java

Password.class: Password.java
	javac -g Password.java

PasswordGenerator.class: PasswordGenerator.java
	javac -g PasswordGenerator.java

clean:
	rm -rf *.class
	rm -rf *.bin
	rm -rf *.enc
	rm -rf *.jar

jar:
	make
	jar -cvfm passwordManager.jar manifest.txt *.*

passwordManager.jar:
	make
	jar -cvfm passwordManager.jar manifest.txt *.*

run: passwordManager.jar
	java -jar passwordManager.jar
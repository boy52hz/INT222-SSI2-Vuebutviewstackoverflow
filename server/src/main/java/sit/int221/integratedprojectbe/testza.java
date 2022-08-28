package sit.int221.integratedprojectbe;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class testza {
    public static void main(String[] args) {
        Argon2 argon2 = Argon2Factory.create();
        String password = "admin";
        String hash = argon2.hash(22, 65536, 1, password);
        System.out.println(hash);

        if(argon2.verify(hash, "password")) {
            System.out.println("Matched");
        } else {
            System.out.println("Not Matched");
        }
    }
}

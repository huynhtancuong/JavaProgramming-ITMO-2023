package client.utility;

import common.interaction.Request;
import common.interaction.User;

import java.util.Scanner;

/**
 * Handle user login and password.
 */
public class AuthHandler {
    private final String loginCommand = "login";
    private final String registerCommand = "register";

    private Scanner userScanner;

    public AuthHandler(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * Handle user authentication.
     *
     * @return Request of user.
     */
    public Request handle() {
        AuthAsker authAsker = new AuthAsker(userScanner);
        String command = authAsker.askQuestion("You already have an account?") ? loginCommand : registerCommand;
        User user = new User(authAsker.askLogin(), authAsker.askPassword());
        return new Request(command, "", user);
    }
}

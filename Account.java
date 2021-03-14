package sample;
import java.util.ArrayList;
import java.io.*;

public class Account {
    String username;
    String password;
    String role;
    String messages = "";
    ArrayList<Drink> orderHist;

    // called when a user trys to log in. Attempts to find an existing account with supplied credentials.
    // returns "worker for worker account, customer for a user account
    public String accountExists(String un, String pw) {
        File file = new File("credentials.txt");
        try {
            String line;
            boolean foundUsername = false;
            BufferedReader br = new BufferedReader(new FileReader(file));
            while(((line = br.readLine()) != null) && !foundUsername){
                if(line.contains(un)){
                    username = un;
                    foundUsername = true;
                    line = br.readLine();
                    if(line.contains(pw)){
                        password = pw;
                        line = br.readLine();
                        role = line;
                        while((line = br.readLine()).contains("!") == false){
                            messages = messages + line;
                        }
                        return role;
                    }
                }
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return "failure";
    }

    public String getMessages(){
        return messages;
    }

}

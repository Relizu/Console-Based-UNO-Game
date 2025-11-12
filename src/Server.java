
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class ClientHandler {
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;

    ClientHandler(Socket s) throws IOException {
        socket = s;
        out = new ObjectOutputStream(s.getOutputStream());
        in = new ObjectInputStream(s.getInputStream());
    }
}


public class Server {
    private List<Player> Players = new ArrayList<>();
    private List<ClientHandler> Playerclients = new ArrayList<>();
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private BufferedReader inkey;
    private boolean started = false;
    private int iterator =0;
    private List<card> deck = new ArrayList<>();
    private card groundCard;
    private boolean reversed = false;
    private int didntUno=-1;

    void updateList(){
        if(started){
            for(ClientHandler client: Playerclients){
                if(Players.get(Playerclients.indexOf(client)).cards.size()==0){
                    System.out.println(Players.get(Playerclients.indexOf(client)).name+" won!");
                    for(ClientHandler cc: Playerclients){
                        try{
                            cc.out.writeUTF("won");
                            cc.out.writeUTF(Players.get(Playerclients.indexOf(client)).name);
                        }catch(IOException e){
                            System.out.println(e);
                        }
                    }
                    break;
                }
            }
        }
        for(ClientHandler client: Playerclients){
            try{
                client.out.reset();
                client.out.writeUTF("updateList");
                client.out.writeObject(new ArrayList<>(Players));
                client.out.writeObject(groundCard);
                client.out.writeInt(iterator);
                client.out.flush();
            }catch(IOException e){
                System.out.println(e);
            }
        }
    }

    public Server(int port){
        try{
        ServerSocket ss = new ServerSocket(port);
        System.out.println("Server Started...");
        // Thread for joining members
        new Thread(()->{
            while (started == false) {
                try{
                    Socket s = ss.accept();
                    if(started) break;
                    Player player;
                    ClientHandler client = new ClientHandler(s);
                    String name = client.in.readUTF();
                    if(Players.size()<8){
                        player = new Player(Playerclients.size(), name);
                        Players.add(player);
                        Playerclients.add(client);
                        updateList();
                        System.out.println(name +" joined the Game");

                    }else{
                        client.out.writeUTF("max_players");
                        client.out.flush();
                    }
                    
                }catch(IOException e){
                    System.out.println(e);
                }
            }
        }).start();
        
        // make a deck
        for(int color = 1; color<=4; color++){
            deck.add(new card(color, "0"));
            for(int number =1; number<=9; number++){
                deck.add(new card(color, String.valueOf(number)));
                deck.add(new card(color, String.valueOf(number)));
            }
            deck.add(new card(color, "X"));
            deck.add(new card(color, "X"));
            deck.add(new card(color, "R"));
            deck.add(new card(color, "R"));
            deck.add(new card(color, "+2"));
            deck.add(new card(color, "+2"));

        }
        for(int i=0; i<4; i++){
            deck.add(new card(0, "*"));
            deck.add(new card(0, "*+4"));
        }
        Collections.shuffle(deck);
        

        // When you input "start" in console
        inkey = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String input = inkey.readLine();
            if (input.toUpperCase().equals("START")){
                started = true;
                break;
            }
        }


        int firstground=0;
        while (deck.get(firstground).color==0) {
            firstground++;
        }
        groundCard = deck.remove(firstground);
        
        // make a thread per client
        for(ClientHandler client: Playerclients){
            Players.get(Playerclients.indexOf(client)).cards = new ArrayList<>(deck.subList(0, 7));
            deck.subList(0,7).clear();

            
            new Thread(()->{
                while (true) {
                    try{
                        String m ;
                        System.out.println(groundCard);
                        System.out.println(Players.get(iterator).name+"'s turn");
                        m = client.in.readUTF();
                        if(client ==Playerclients.get(iterator)){
                            try{
                                int num = Integer.parseInt(m);
                                if(num>Players.get(iterator).cards.size()-1){continue;}
                                
                                if(Players.get(iterator).cards.get(num).color == groundCard.color || (Players.get(iterator).cards.get(num).value.equals(groundCard.value) && !groundCard.value.startsWith("*")) ){
                                    didntUno=-1;
                                    groundCard = Players.get(iterator).cards.remove(num);
                                    if(Players.get(iterator).cards.size()==1){didntUno = iterator;}
                                    updateList();
                                    if(groundCard.value.toUpperCase().equals("X")){
                                        if(reversed){
                                            iterator--;
                                            if(iterator < 0){iterator = Playerclients.size()-1;}
                                        }else{
                                            iterator++;
                                            if(iterator >= Playerclients.size()){iterator = 0;}
                                        }
                                    }else if(groundCard.value.equals("+2")){
                                        if(reversed){
                                            iterator--;
                                            if(iterator < 0){iterator = Playerclients.size()-1;}
                                        }else{
                                            iterator++;
                                            if(iterator >= Playerclients.size()){iterator = 0;}
                                        }
                                        Players.get(iterator).cards.add(deck.remove(0));
                                        Players.get(iterator).cards.add(deck.remove(0));
                                        updateList();
                                    }else if(groundCard.value.toUpperCase().equals("R")){
                                        if(reversed){reversed = false;}
                                        else{
                                            reversed = true;
                                        }
                                    }
                                    if(reversed){
                                        iterator--;
                                        if(iterator < 0){iterator = Playerclients.size()-1;}
                                    }else{
                                        iterator++;
                                        if(iterator >= Playerclients.size()){iterator = 0;}
                                    }
                                    updateList();
                                }else if(Players.get(iterator).cards.get(num).color ==0){
                                    didntUno=-1;
                                    groundCard = Players.get(iterator).cards.remove(num);
                                    Playerclients.get(iterator).out.reset();
                                    Playerclients.get(iterator).out.writeUTF("pickColor");
                                    Playerclients.get(iterator).out.flush();
                                    String chosencolor;
                                    chosencolor = Playerclients.get(iterator).in.readUTF();
                                    groundCard.color = Integer.valueOf(chosencolor);
                                    if(Players.get(iterator).cards.size()==1){didntUno = iterator;}
                                    updateList();
                                    
                                    if(groundCard.value.equals("*+4")){
                                        if(reversed){
                                            iterator--;
                                            if(iterator < 0){iterator = Playerclients.size()-1;}
                                        }else{
                                            iterator++;
                                            if(iterator >= Playerclients.size()){iterator = 0;}
                                        }
                                        Players.get(iterator).cards.add(deck.remove(0));
                                        Players.get(iterator).cards.add(deck.remove(0));
                                        Players.get(iterator).cards.add(deck.remove(0));
                                        Players.get(iterator).cards.add(deck.remove(0));
                                        updateList();
                                    }
                                    if(reversed){
                                        iterator--;
                                        if(iterator < 0){iterator = Playerclients.size()-1;}
                                    }else{
                                        iterator++;
                                        if(iterator >= Playerclients.size()){iterator = 0;}
                                    }
                                    updateList();
                                }
                            }catch (NumberFormatException e) {
                                if(m.toUpperCase().equals("DRAW")){
                                    boolean candraw = true;
                                    for(card c: Players.get(iterator).cards){
                                        if(c.color==groundCard.color){
                                            candraw =false;
                                            break;
                                        }
                                        if(c.value.equals(groundCard.value)){
                                            candraw =false;
                                            break;
                                        }
                                        if(c.color == 0){
                                            candraw =false;
                                            break;
                                        }
                                    }
                                    if(candraw){
                                        didntUno=-1;
                                        Players.get(iterator).cards.add(deck.remove(0));
                                        updateList();
                                        if(reversed){
                                            iterator--;
                                            if(iterator < 0){iterator = Playerclients.size()-1;}
                                        }else{
                                            iterator++;
                                            if(iterator >= Playerclients.size()){iterator = 0;}
                                        }
                                        updateList();
                                    }
                                }else if(m.toUpperCase().equals("UNO")){
                                    if(didntUno != -1){
                                        Players.get(didntUno).cards.add(deck.remove(0));
                                        Players.get(didntUno).cards.add(deck.remove(0));
                                        updateList();
                                    }
                                }else if(m.toUpperCase().startsWith("UNO")){
                                    didntUno=-1;
                                    int y = Integer.parseInt(m.substring(4));
                                    if(Players.get(iterator).cards.size() ==2){
                                        if(y>Players.get(iterator).cards.size()-1){continue;}
                                        didntUno=-1;
                                        if(Players.get(iterator).cards.get(y).color == groundCard.color || (Players.get(iterator).cards.get(y).value.equals(groundCard.value) && !groundCard.value.startsWith("*")) ){
                                            groundCard = Players.get(iterator).cards.remove(y);
                                            updateList();
                                            if(groundCard.value.toUpperCase().equals("X")){
                                                if(reversed){
                                                    iterator--;
                                                    if(iterator < 0){iterator = Playerclients.size()-1;}
                                                }else{
                                                    iterator++;
                                                    if(iterator >= Playerclients.size()){iterator = 0;}
                                                }
                                            }else if(groundCard.value.equals("+2")){
                                                if(reversed){
                                                    iterator--;
                                                    if(iterator < 0){iterator = Playerclients.size()-1;}
                                                }else{
                                                    iterator++;
                                                    if(iterator >= Playerclients.size()){iterator = 0;}
                                                }
                                                Players.get(iterator).cards.add(deck.remove(0));
                                                Players.get(iterator).cards.add(deck.remove(0));
                                                updateList();
                                            }else if(groundCard.value.toUpperCase().equals("R")){
                                                if(reversed){reversed = false;}
                                                else{
                                                    reversed = true;
                                                }
                                            }
                                            if(reversed){
                                                iterator--;
                                                if(iterator < 0){iterator = Playerclients.size()-1;}
                                            }else{
                                                iterator++;
                                                if(iterator >= Playerclients.size()){iterator = 0;}
                                            }
                                            updateList();
                                        }else if(Players.get(iterator).cards.get(y).color ==0){
                                            groundCard = Players.get(iterator).cards.remove(y);
                                            Playerclients.get(iterator).out.reset();
                                            Playerclients.get(iterator).out.writeUTF("pickColor");
                                            Playerclients.get(iterator).out.flush();
                                            String chosencolor;
                                            chosencolor = Playerclients.get(iterator).in.readUTF();
                                            groundCard.color = Integer.valueOf(chosencolor);
                                            updateList();
                                            
                                            if(groundCard.value.equals("*+4")){
                                                if(reversed){
                                                    iterator--;
                                                    if(iterator < 0){iterator = Playerclients.size()-1;}
                                                }else{
                                                    iterator++;
                                                    if(iterator >= Playerclients.size()){iterator = 0;}
                                                }
                                                Players.get(iterator).cards.add(deck.remove(0));
                                                Players.get(iterator).cards.add(deck.remove(0));
                                                Players.get(iterator).cards.add(deck.remove(0));
                                                Players.get(iterator).cards.add(deck.remove(0));
                                                updateList();
                                            }
                                            if(reversed){
                                                iterator--;
                                                if(iterator < 0){iterator = Playerclients.size()-1;}
                                            }else{
                                                iterator++;
                                                if(iterator >= Playerclients.size()){iterator = 0;}
                                            }
                                            updateList();
                                        }
                                    }
                                }
                            }
                        }else{
                            if(m.toUpperCase().equals("UNO")){
                                    if(didntUno != -1){
                                        Players.get(didntUno).cards.add(deck.remove(0));
                                        Players.get(didntUno).cards.add(deck.remove(0));
                                        updateList();
                                    }
                                }
                        }
                    }catch(IOException e){
                        System.out.println(e);
                    }
                }
            }).start();
        }
        
        
        updateList();
        
        System.out.println(deck.size());

        }catch(IOException e){
            System.err.println(e);
        }
    }
    public static void main(String[] args) {
        Server s = new Server(5000);
    }
}

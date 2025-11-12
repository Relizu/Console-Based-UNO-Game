import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
public class Player implements Serializable {
    int socketnum;
    String name;
    List<card> cards = new ArrayList<>();
    List<List<String>> showcardsList = new ArrayList<>();

    public void ShowCards(){
        if(cards.size()==0) return;
        for(card c : cards){
            showcardsList.add(Arrays.asList(c.toString().split("\n")));
        }
        for(int x=0; x<showcardsList.get(0).size();x++){
            for(int y =0; y<showcardsList.size();y++){
                System.out.print(showcardsList.get(y).get(x));
            }
            System.out.print("\n");
        }
    }
    public String ShowPlayer(int color){
        String output;
        output=String.format("""
╔═%s═╗\u001B[0m
║ %s ║\u001B[0m
╠═%s═╣\u001B[0m
║%s %d %s║\u001B[0m
╚═%s═╝\u001B[0m
                ""","═".repeat(name.length()),name,"═".repeat(name.length())," ".repeat((int)(name.length()/2)),cards.size()," ".repeat((int)(name.length()/2)),"═".repeat(name.length()));
        if(color ==0){
output=String.format("""
\u001B[0m╔═%s═╗\u001B[0m
\u001B[0m║ %s ║\u001B[0m
\u001B[0m╠═%s═╣\u001B[0m
\u001B[0m║%s %d %s║\u001B[0m
\u001B[0m╚═%s═╝\u001B[0m
                ""","═".repeat(name.length()),name,"═".repeat(name.length())," ".repeat((int)(name.length()/2)),cards.size()," ".repeat((int)(name.length()/2)),"═".repeat(name.length()));
        }else if(color ==1){
output=String.format("""
\u001B[32m╔═%s═╗\u001B[0m
\u001B[32m║ %s ║\u001B[0m
\u001B[32m╠═%s═╣\u001B[0m
\u001B[32m║%s %d %s║\u001B[0m
\u001B[32m╚═%s═╝\u001B[0m
                ""","═".repeat(name.length()),name,"═".repeat(name.length())," ".repeat((int)(name.length()/2)),cards.size()," ".repeat((int)(name.length()/2)),"═".repeat(name.length()));
        }
        
        return output;
    }

    Player(int socket, String name){
        this.socketnum = socket;
        this.name = name;
    }
}

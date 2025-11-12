import java.io.Serializable;
public class card implements Serializable {
    int color;
    String value;

    card(int color, String value){
        this.color = color;
        this.value =  value;
    }

    @Override
    public String toString() { 
        String output="";
        if(this.color ==0){
        output =String.format("""
╔══%s══╗
║  %s  ║
║  %s  ║
║  %s  ║
╚══%s══╝
        ""","═".repeat(this.value.length())," ".repeat(this.value.length()),this.value," ".repeat(this.value.length()),"═".repeat(this.value.length()));
        }else if(this.color ==1){
        output =String.format("""
\u001B[31m╔══%s══╗\u001B[0m
\u001B[31m║  %s  ║\u001B[0m
\u001B[31m║  %s  ║\u001B[0m
\u001B[31m║  %s  ║\u001B[0m
\u001B[31m╚══%s══╝\u001B[0m
        ""","═".repeat(this.value.length())," ".repeat(this.value.length()),this.value," ".repeat(this.value.length()),"═".repeat(this.value.length()));
        }else if(this.color ==2){
        output =String.format("""
\u001B[34m╔══%s══╗\u001B[0m
\u001B[34m║  %s  ║\u001B[0m
\u001B[34m║  %s  ║\u001B[0m
\u001B[34m║  %s  ║\u001B[0m
\u001B[34m╚══%s══╝\u001B[0m
        ""","═".repeat(this.value.length())," ".repeat(this.value.length()),this.value," ".repeat(this.value.length()),"═".repeat(this.value.length()));
        }else if(this.color ==3){
        output =String.format("""
\u001B[32m╔══%s══╗\u001B[0m
\u001B[32m║  %s  ║\u001B[0m
\u001B[32m║  %s  ║\u001B[0m
\u001B[32m║  %s  ║\u001B[0m
\u001B[32m╚══%s══╝\u001B[0m
        ""","═".repeat(this.value.length())," ".repeat(this.value.length()),this.value," ".repeat(this.value.length()),"═".repeat(this.value.length()));
        }else if(this.color ==4){
        output =String.format("""
\u001B[33m╔══%s══╗\u001B[0m
\u001B[33m║  %s  ║\u001B[0m
\u001B[33m║  %s  ║\u001B[0m
\u001B[33m║  %s  ║\u001B[0m
\u001B[33m╚══%s══╝\u001B[0m
        ""","═".repeat(this.value.length())," ".repeat(this.value.length()),this.value," ".repeat(this.value.length()),"═".repeat(this.value.length()));
        }
        return output;
    }
}

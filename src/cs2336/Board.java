
package cs2336;


public class Board {
    private String status; // status of 1 square will be empty, border, black or white.
    public Board(String status){
        this.status = status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    
    public String getStatus(){
        return this.status;
    }
    
}

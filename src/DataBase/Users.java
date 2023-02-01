
package data_pro.DataBase;


public class Users {
    private  int id = -1;
    private String gmail  ;
    private String password ;
    private String first ;
    private String last ;
    private int age ;


    public Users(int id, String gmail, String password, String first, String last, int ag){
        this.id = id ;
        this.gmail = gmail ; 
        this.password = password ; 
        this.first = first ;
        this.last = last ;
        this.age = ag ;
    }


    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirst(String first_name) {
        this.first = first_name;
    }

    public void setLast(String last_name) {
        this.last = last_name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getGmail() {
        return gmail;
    }

    public String getPassword() {
        return password;
    }

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString(){
        return this.id + " " + this.first + " " + this.password ;
    }
}

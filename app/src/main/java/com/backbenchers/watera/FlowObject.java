package com.backbenchers.watera;

public class FlowObject {
    String created_at;
    String field1;
    int entry_id;

    public FlowObject(String created_at, String field1, int entry_id) {
        this.created_at = created_at;
        this.field1 = field1;
        this.entry_id = entry_id;
    }



    public String getTime(){
        String[] arr=created_at.split("T");
        String[] arr2=arr[1].split("Z");
        return arr2[0];
   }
    public String getDate(){
        String[] arr=created_at.split("T");
        return arr[0];
    }
    public String getFlow(){

        return field1;
    }
}

import com.google.gson.JsonElement;

public class StandarResponde {

    private StatusResponse status;
    private String message;
    private JsonElement data;

    public StandarResponde(){

    }

    public StandarResponde(StatusResponse status){

        this.status = status;

    }

    public StandarResponde(StatusResponse status, String message){

        this.status = status;
        this.message = message;

    }

    public StandarResponde(StatusResponse status, JsonElement data){

        this.status = status;
        this.data = data;

    }


}

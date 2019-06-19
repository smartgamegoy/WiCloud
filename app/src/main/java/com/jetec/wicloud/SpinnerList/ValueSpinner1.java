package com.jetec.wicloud.SpinnerList;

import android.content.Context;
import com.jetec.wicloud.R;
import com.jetec.wicloud.Value;
import org.json.JSONException;
import java.util.ArrayList;

public class ValueSpinner1 {

    private static final String TAG = "ValueSpinner1";
    private Context context;

    public ValueSpinner1(Context context){
        this.context = context;
    }

    public ArrayList<String> getmodel(){

        ArrayList<String> modelList = new ArrayList<>();
        modelList.clear();

        modelList.add(context.getString(R.string.add_module));
        for(int i = 0; i < Value.model.length(); i++){
            try {
                if(Value.model.getJSONObject(i).getString("isConnected").matches("true")){
                    String modelId = Value.model.getJSONObject(i).getString("id");
                    modelList.add(modelId);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return modelList;
    }
}

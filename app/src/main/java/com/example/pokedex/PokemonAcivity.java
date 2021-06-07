package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PokemonAcivity extends AppCompatActivity {

    TextView pokemon_name_text, pokemon_number_text, pokemon_type1, pokemon_type2;
    String pokemon_url;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_acivity);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        pokemon_name_text = findViewById(R.id.pokemon_name);
        pokemon_number_text = findViewById(R.id.pokemon_number);
        pokemon_type1 = findViewById(R.id.pokemon_type1);
        pokemon_type2 = findViewById(R.id.pokemon_type2);
        pokemon_url = getIntent().getStringExtra("url");

        loadPokemonTyps();
    }
    public void loadPokemonTyps(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, pokemon_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    pokemon_name_text.setText(response.getString("name"));
                    pokemon_number_text.setText(String.format( "%03d",response.getInt("id")));

                    JSONArray typesResponse = response.getJSONArray("types");
                    for(int i=0; i < typesResponse.length(); i++){
                        JSONObject typeResponse = typesResponse.getJSONObject(i);
                        int slot = typeResponse.getInt("slot");
                        String type = typeResponse.getJSONObject("type").getString("name");

                        if(slot == 1)
                            pokemon_type1.setText(type);
                        else if(slot == 2)
                            pokemon_type2.setText(type);
                    }
                } catch (JSONException e) {
                    Log.e("onResponse", "Pokemon json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", "Pokemon details error", error);
            }
        });
        requestQueue.add(request);

    }
}
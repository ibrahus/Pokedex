package com.example.pokedex;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> {
    @NonNull
    @NotNull
    @Override
    public PokedexViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pokedex_row, parent, false);
        return new PokedexViewHolder(view);
    }

    private List<Pokemon> pokemonList = new ArrayList<>();
    private RequestQueue requestQueue;

    PokedexAdapter(Context context){
        requestQueue = Volley.newRequestQueue(context);
        loadPokemon();
    }

    public void loadPokemon (){
        String url = "https://pokeapi.co/api/v2/pokemon/?limit=151";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    for(int i=0; i < results.length(); i++){
                        JSONObject result = results.getJSONObject(i);
                        pokemonList.add(new Pokemon(
                                result.getString("name"),
                                result.getString("url")
                        ));
                    }
                    notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.e("onResponse", "json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", "Pokemon list error", error);
            }
        });
        requestQueue.add(request);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PokedexAdapter.PokedexViewHolder holder, int position) {
        Pokemon current_pokemon = pokemonList.get(position);
        holder.textView.setText(current_pokemon.getName());
        holder.linearLayout.setTag(current_pokemon);
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public static class PokedexViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout linearLayout;
        public TextView textView;

        public PokedexViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.pokedex_row);
            textView = itemView.findViewById(R.id.pokedex_row_textview);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pokemon currentPokemon = (Pokemon) linearLayout.getTag();
                    Intent intent = new Intent(v.getContext(), PokemonAcivity.class);
//                    intent.putExtra("name", currentPokemon.getName());
                    intent.putExtra("url", currentPokemon.getUrl());

                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}

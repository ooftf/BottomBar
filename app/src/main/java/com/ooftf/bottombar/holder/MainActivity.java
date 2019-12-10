package com.ooftf.bottombar.holder;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ooftf.bottombar.BottomBar;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity {
    BottomBar bottomBar;
    TextView textView;
    Boolean isIntercept = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomBar = findViewById(R.id.bottomBar);
        textView = findViewById(R.id.text);
        bottomBar.setOnItemSelectChangedListener(new Function2<Integer, Integer, Unit>() {
            @Override
            public Unit invoke(Integer oldIndex, Integer newIndex) {
                if (newIndex == 0) {
                    textView.setText("First");
                } else if (newIndex == 1) {
                    textView.setText("Second");
                }
                return null;
            }
        });
        bottomBar.setOnItemSelectIInterceptor(new Function2<Integer, Integer, Boolean>() {
            @Override
            public Boolean invoke(Integer integer, Integer integer2) {
                return isIntercept;
            }
        });
        bottomBar.setOnItemRepeatListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer integer) {
                isIntercept = !isIntercept;
                return null;
            }
        });
        bottomBar.setAdapter(new BottomBar.Adapter<ViewHolder>() {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position, int selectedPosition) {
                if (position == selectedPosition) {
                    holder.title.setTextColor(Color.parseColor("#2196F3"));
                    switch (position) {
                        case 0:
                            holder.title.setText("First");
                            holder.icon.setImageResource(R.drawable.ic_app_selected_24dp);
                            break;
                        case 1:
                            holder.title.setText("Second");
                            holder.icon.setImageResource(R.drawable.ic_debug_selected_24dp);
                            break;
                    }
                } else {
                    holder.title.setTextColor(Color.parseColor("#000000"));
                    switch (position) {
                        case 0:
                            holder.title.setText("First");
                            holder.icon.setImageResource(R.drawable.ic_app_24dp);
                            break;
                        case 1:
                            holder.title.setText("Second");
                            holder.icon.setImageResource(R.drawable.ic_debug_24dp);
                            break;
                    }

                }
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(getLayoutInflater().inflate(R.layout.item_bottombar, parent, false));
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });
        bottomBar.setSelectIndex(0);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}

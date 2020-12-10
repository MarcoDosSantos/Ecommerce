package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ecommerce.Adapter.ProductAdapter;
import com.example.ecommerce.Fragments.CartFragment;
import com.example.ecommerce.Fragments.CategoriesFragment;
import com.example.ecommerce.Fragments.OrdersFragment;
import com.example.ecommerce.Fragments.SettingsFragment;
import com.example.ecommerce.Models.Product;
import com.example.ecommerce.Models.Users;
import com.example.ecommerce.Prevalent.Prevalent;



import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Book;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private Window window;
    private String blackColor = "#000000";

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private FloatingActionButton fab;

    private CircleImageView user_profile_image;
    private TextView user_profile_name_txt;

    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Product> products;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Paper.init(this);
        window = getWindow();
        window.setStatusBarColor(Color.parseColor(blackColor));

        fab = findViewById(R.id.floatingActionButton);

        fab.setBackgroundTintList (ColorStateList.valueOf(Color.parseColor("#FFF336AF")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Apretaste el floating action button", Toast.LENGTH_SHORT).show();
            }
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.home);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();


        //Código para identificar la foto y nombre del usuario en el header
        View headerView = navigationView.getHeaderView(0);
        CircleImageView user_profile_image = headerView.findViewById(R.id.user_profile_image);
        TextView user_profile_name_txt = headerView.findViewById(R.id.user_profile_name_txt);
        user_profile_name_txt.setText(Prevalent.currentOnlineUser.getName());
        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(user_profile_image);

        products = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ProductAdapter adapter = new ProductAdapter(products);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pid = products.get(recyclerView.getChildAdapterPosition(v)).getPid();
                Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                intent.putExtra("pid", pid);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.removeAll(products);
                for (DataSnapshot snap:
                        snapshot.getChildren()) {

                    Product product = snap.getValue(Product.class);
                    products.add(product);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

/*    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(productRef, Product.class)
                .build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int i, @NonNull @NotNull Product model) {
                        holder.txt_display_product_name.setText(model.getName());
                        holder.txt_display_product_description.setText(model.getDescription());
                        holder.txt_display_product_price.setText("$ " + model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.iv_display_product_image);

                    }

                    @NonNull
                    @NotNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);

        if (item.getItemId() == R.id.item_cart){
            startActivity(new Intent(HomeActivity.this, HomeActivity.class));
        }
        if (item.getItemId() == R.id.item_orders){
            /*fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new OrdersFragment());
            fragmentTransaction.commit();*/
        }
        if (item.getItemId() == R.id.item_categories){
            /*fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new CategoriesFragment());
            fragmentTransaction.commit();*/
        }
        if (item.getItemId() == R.id.item_settings){
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.item_logout){
            Paper.book().destroy();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        return false;
    }

}
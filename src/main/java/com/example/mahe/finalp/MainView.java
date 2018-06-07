package com.example.mahe.finalp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView uname,umail,uno;
    Button searchp,createp,exitp;
    FirebaseAuth mAuth;FirebaseDatabase database;DatabaseReference myRef;
    public String uid,email;    ArrayList<String> alst;  ArrayAdapter<String> aadapt;   String[] ndmails;
    ListView thed;
    public static String overlap="0",overlap2="1",joinedmails=" ";
    public int conti=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        alst=new ArrayList<String>();

        mAuth=FirebaseAuth.getInstance();
        Intent eager=getIntent();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        String name=eager.getStringExtra("name");
        email=eager.getStringExtra("email");
        uid=eager.getStringExtra("uid");
        String passw=eager.getStringExtra("password");
        mAuth.signInWithEmailAndPassword(email,passw);
        String[] truname=name.split("~");

        //Toast.makeText(this, "Uname:"+truname[0]+" PhoneNo:"+truname[1], Toast.LENGTH_SHORT).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchp=(Button) findViewById(R.id.search);
        createp=(Button) findViewById(R.id.cpool);
        exitp=(Button) findViewById(R.id.epool);
        thed=(ListView) findViewById(R.id.theD);
        alst.add("Currently empty");
        aadapt = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, alst);
        thed.setAdapter(aadapt);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        uname = (TextView) headerView.findViewById(R.id.username);
        umail = (TextView) headerView.findViewById(R.id.usermail);
        uno=(TextView) headerView.findViewById(R.id.userno);
        uname.setText(String.format("User name: %s",truname[0]));umail.setText(String.format("Email: %s",email));uno.setText(String.format("Phone no: %s",truname[1]));

        try
        {
            Query idg=myRef.child("uid").orderByChild("uid").equalTo(uid);
            idg.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {for(DataSnapshot d1: dataSnapshot.getChildren())
                    {
                        overlap=d1.getValue(vcreatepool.class).stend;conti=1;
                        //Toast.makeText(getApplicationContext(),"Overlap: "+overlap,Toast.LENGTH_SHORT).show();
                    }

                        if(conti==1)
                        {
                            Query idk=myRef.child("stend").orderByChild("stend").equalTo(overlap);
                            idk.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists())
                                    {
                                        for( DataSnapshot d2 : dataSnapshot.getChildren())
                                        {
                                            overlap2=d2.getValue(vcreatepool.class).stend;
                                            //Toast.makeText(getApplicationContext(),"Overlap2: "+overlap2,Toast.LENGTH_SHORT).show();
                                            joinedmails=d2.getValue(vcreatepool.class).email;
                                            ndmails=joinedmails.split("~");
                                        }

                                        if(overlap.equals(overlap2))
                                        {alst.clear();
                                            for(String gt: ndmails)
                                        {alst.add(gt);}
                                        aadapt.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) { }
                            });

                        }
                        else if(conti==0)
                        {alst.clear();
                            alst.add("No pool exists as of now");
                         aadapt.notifyDataSetChanged();
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //Query idq=myRef.child("uid").orderByChild("uid").equalTo(srcuid);
            //Fetch the matching parameter. If values match, delete both pools
            //work of arrayadapter over here

        }
        catch (Exception e)
        {Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();}


        searchp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent src=new Intent(MainView.this,Search.class);
                src.putExtra("email",email);
                src.putExtra("uid",uid);
                startActivity(src);

            }
        });

        createp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent src=new Intent(MainView.this,CreatePool.class);
                src.putExtra("uid",uid);
                startActivity(src);

            }
        });

        exitp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conti=0;
                try{
                    Query idg=myRef.child("uid").orderByChild("uid").equalTo(uid);
                    idg.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {for(DataSnapshot d1: dataSnapshot.getChildren())
                            {
                                overlap=d1.getValue(vcreatepool.class).stend;conti=1;
                                //Toast.makeText(getApplicationContext(),"Overlap: "+overlap,Toast.LENGTH_SHORT).show();
                            }

                                if(conti==1)
                                {
                                    Query idk=myRef.child("stend").orderByChild("stend").equalTo(overlap);
                                    idk.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists())
                                            {
                                                for( DataSnapshot d2 : dataSnapshot.getChildren())
                                                {
                                                    overlap2=d2.getValue(vcreatepool.class).stend;
                                                    //Toast.makeText(getApplicationContext(),"Overlap2: "+overlap2,Toast.LENGTH_SHORT).show();
                                                }

                                                if(overlap.equals(overlap2))
                                                {
                                                    myRef.child("stend").child(overlap).removeValue();
                                                    myRef.child("uid").child(uid).removeValue();
                                                    alst.clear();
                                                    alst.add("No pool exists as of now");
                                                    aadapt.notifyDataSetChanged();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) { }
                                    });

                                }
                                else if(conti==0)
                                {myRef.child("uid").child(uid).removeValue();}

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //Query idq=myRef.child("uid").orderByChild("uid").equalTo(srcuid);
                    //Fetch the matching parameter. If values match, delete both pools

                }
                catch (Exception e)
                {Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();}

                Toast.makeText(getApplicationContext(),"Pool has been deleted",Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        else if(id== R.id.details){}
        else if(id== R.id.about){}
        else if(id== R.id.logout){finish();}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

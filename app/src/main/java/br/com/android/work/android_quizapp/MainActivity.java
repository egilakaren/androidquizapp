package br.com.android.work.android_quizapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import br.com.android.work.android_quizapp.model.Usuario;

public class MainActivity extends AppCompatActivity {
    MaterialEditText editNewUser;
    MaterialEditText editNewPassword;
    MaterialEditText editEmail;

    MaterialEditText editUser;
    MaterialEditText editPassword;

    Button btnSignUp;
    Button btnSignIn;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        editUser = (MaterialEditText) findViewById(R.id.editUser);
        editPassword = (MaterialEditText) findViewById(R.id.editPassword);

        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(editUser.getText().toString(),
                        editPassword.getText().toString());
            }
        });
    }

    private void signIn(final String user, final String pass) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user).exists()){
                    if(!user.isEmpty()){
                        Usuario login = dataSnapshot.child(user).getValue(Usuario.class);
                        if(login.getPassword().equals(pass)) {
                            Intent homeActivity = new Intent(MainActivity.this, Home.class);
                            startActivity(homeActivity);
                            finish();
                        } else Toast.makeText(MainActivity.this, "Senha invalida!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Por favor, informe seu usuario", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Usuario informado não existe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Por favcr, preencha todos os campos");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout, null);

        editNewUser = (MaterialEditText) sign_up_layout.findViewById(R.id.editNewUsername);
        editEmail = (MaterialEditText) sign_up_layout.findViewById(R.id.editNewEmail);
        editPassword = (MaterialEditText) sign_up_layout.findViewById(R.id.editNewPassword);

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                final Usuario usuario = new Usuario(editNewUser.getText().toString(),
                                              editPassword.getText().toString(),
                                              editEmail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(usuario.getUsername()).exists()) {
                            Toast.makeText(MainActivity.this, "Usuario ja existe !", Toast.LENGTH_SHORT).show();
                        }else{
                            users.child(usuario.getUsername()).setValue(usuario);
                            Toast.makeText(MainActivity.this, "Usuario gravado com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }
}

package game.tuannv.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        Button button = findViewById(R.id.button);
        mAuth.signInWithEmailAndPassword("tuannv009@yopmail.com", "12345678")
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        final FirebaseUser user = mAuth.getCurrentUser();
                    } else {
                        // If sign in fails, display a message to the user.
                    }
                }
            });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAuthenticationEmail();
            }
        });
        if (intent == null) return;
        Uri data = intent.getData();
        if (data == null) return;
        WebView myWebView = findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(data.toString());
        myWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                if (firebaseAuth.getCurrentUser() == null) return;
                firebaseAuth.getCurrentUser().reload().addOnCompleteListener(
                    task -> {
                        boolean isVerify = firebaseAuth.getCurrentUser().isEmailVerified();
                        Log.e("tuanbg", isVerify + "");
                    });
            }
        });

    }

    public void sendAuthenticationEmail() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener
            (new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.e("tuanbg", "onComplete");
                }
            });
    }
}

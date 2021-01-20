//package com.example.bluecity.my.information.address;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.NavigationUI;
//
//import android.os.Bundle;
//
//import com.example.bluecity.R;
//
//public class AddressActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_address);
//
//        NavController controller = Navigation.findNavController(this,R.id.navhost_address);
//        NavigationUI.setupActionBarWithNavController(this,controller);
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {  //左上返回键功能
//        NavController controller = Navigation.findNavController(this,R.id.navhost_address);
//        return controller.navigateUp();
//    }
//}

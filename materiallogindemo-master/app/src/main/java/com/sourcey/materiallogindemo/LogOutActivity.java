package com.sourcey.materiallogindemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

public class LogOutActivity {

    public void afterLogOutActivity(MainActivity mainAct){
       mainAct.onBackPressed();

    }
}

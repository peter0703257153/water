package com.example.work;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class menu extends ListActivity{
String classes[]={"TakeAPicture"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(menu.this,android.R.layout.simple_expandable_list_item_1,classes));
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		
		String clas = classes[position];
		try{
		Class ourclass = Class.forName("com.example.work."+clas);
		Intent intent = new Intent(menu.this,ourclass);
		startActivity(intent);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}


	

}

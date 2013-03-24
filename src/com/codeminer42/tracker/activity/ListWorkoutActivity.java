package com.codeminer42.tracker.activity;

import java.util.List;

import roboguice.activity.RoboListActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.codeminer42.tracker.R;
import com.codeminer42.tracker.adapter.WorkoutAdapter;
import com.codeminer42.tracker.domain.Workout;
import com.codeminer42.tracker.manager.WorkoutManager;
import com.codeminer42.tracker.util.DateUtil;
import com.codeminer42.tracker.util.RequestCode;
import com.codeminer42.tracker.util.ResultCode;
import com.google.inject.Inject;

/**
 * @author clertonleal@gmail.com
 * @since 23/03/2013
 */
@ContentView(R.layout.activity_list_workout)
public class ListWorkoutActivity extends RoboListActivity {

	@InjectView(R.id.buttonOpenWorkout)
	private Button openWorkout;
	
	@InjectView(R.id.textTotalTimeSpent)
	private TextView totalTimeSpent;
	
	@Inject
	private WorkoutManager workoutManager;
	
	@Inject
	private WorkoutAdapter adapter;
	
	@Inject
	private DateUtil dateUtil;
	
	@Inject
	private Resources resources;
	
	private List<Workout> workouts;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setClickListenners();
		workouts = workoutManager.getAll();
		setTotalTimeSpent();
		adapter.setListProvider(workouts);
		setListAdapter(adapter);
	}
	

	private void setTotalTimeSpent() {
		totalTimeSpent.setText(dateUtil.getResourceSpentTime(workoutManager.getTotalSpentTime(), resources.getString(R.string.total_time_spent)));
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list_workout, menu);
		return true;
	}
	
	private void setClickListenners(){
		openWorkout.setOnClickListener(onClickOpenWorkoutListener);
	}

	private final OnClickListener onClickOpenWorkoutListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final Intent intent = new Intent(ListWorkoutActivity.this, AddWorkoutActivity.class);
			startActivityForResult(intent, RequestCode.ADD_WORKOUT);
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( RequestCode.ADD_WORKOUT == requestCode && ResultCode.WORKOUT_ADDED == resultCode ){
			workouts.clear();
			workouts.addAll(workoutManager.getAll());
			adapter.notifyDataSetChanged();
			setTotalTimeSpent();
		}
	};
}

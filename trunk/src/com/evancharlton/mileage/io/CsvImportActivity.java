package com.evancharlton.mileage.io;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.evancharlton.mileage.ImportActivity;
import com.evancharlton.mileage.R;
import com.evancharlton.mileage.adapters.CsvFieldAdapter;
import com.evancharlton.mileage.tasks.CsvColumnReaderTask;

public class CsvImportActivity extends Activity {
	private CsvColumnReaderTask mColumnReaderTask;
	private LinearLayout mMappingContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_progress_csv);

		mMappingContainer = (LinearLayout) findViewById(R.id.mapping_container);

		restoreTasks();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return new Object[] {
			mColumnReaderTask
		};
	}

	private void restoreTasks() {
		Object[] tasks = (Object[]) getLastNonConfigurationInstance();
		if (tasks != null) {
			mColumnReaderTask = (CsvColumnReaderTask) tasks[0];
		}

		if (mColumnReaderTask == null) {
			mColumnReaderTask = new CsvColumnReaderTask();
		}
		mColumnReaderTask.attach(this);
		if (mColumnReaderTask.getStatus() == AsyncTask.Status.PENDING) {
			mColumnReaderTask.execute(getIntent().getStringExtra(ImportActivity.FILENAME));
		}
	}

	public void setColumns(String[] columnNames) {
		LayoutInflater inflater = LayoutInflater.from(this);
		final int length = columnNames.length;
		for (int i = 0; i < length; i++) {
			String columnName = columnNames[i];
			Log.d("CsvImportActivity", "Adding a UI mapping for " + columnName);
			View v = inflater.inflate(R.layout.import_csv_mapping, mMappingContainer);
			TextView title = (TextView) v.findViewById(R.id.title);
			title.setText(columnName);
			title.setId(columnName.hashCode());
			Spinner spinner = (Spinner) v.findViewById(R.id.mappings);
			spinner.setAdapter(new CsvFieldAdapter(this));
			spinner.setId(columnName.hashCode());
			spinner.setSelection(i);
		}
	}
}

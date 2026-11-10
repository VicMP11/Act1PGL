package com.example.act1pgl;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Locale;

public class FocusFragment extends Fragment {

    private static final long WORK_TIME_IN_MILLIS = 25 * 60 * 1000;
    private static final long BREAK_TIME_IN_MILLIS = 5 * 60 * 1000;

    private TextView tvTimer, tvMode;
    private Button btnStartPause, btnReset;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private boolean isWorkMode = true;
    private long timeLeftInMillis = WORK_TIME_IN_MILLIS;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus, container, false);

        tvTimer = view.findViewById(R.id.tv_timer);
        tvMode = view.findViewById(R.id.tv_pomodoro_mode);
        btnStartPause = view.findViewById(R.id.btn_start_pause);
        btnReset = view.findViewById(R.id.btn_reset);

        updateCountDownText();

        btnStartPause.setOnClickListener(v -> {
            if (isTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        btnReset.setOnClickListener(v -> resetTimer());

        return view;
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                isWorkMode = !isWorkMode;
                timeLeftInMillis = isWorkMode ? WORK_TIME_IN_MILLIS : BREAK_TIME_IN_MILLIS;
                tvMode.setText(isWorkMode ? "Work Time 🎯" : "Break Time ☕");
                btnStartPause.setText("Start");
                updateCountDownText();
            }
        }.start();

        isTimerRunning = true;
        btnStartPause.setText("Pause");
    }

    private void pauseTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        isTimerRunning = false;
        btnStartPause.setText("Start");
    }

    private void resetTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        isTimerRunning = false;
        isWorkMode = true;
        timeLeftInMillis = WORK_TIME_IN_MILLIS;
        tvMode.setText("Work Time 🎯");
        btnStartPause.setText("Start");
        updateCountDownText();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
package com.example.bubbleshoter12345;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private RelativeLayout gameLayout;
    private ImageView aimingDevice;

    private int screenWidth;
    private int screenHeight;

    private List<ImageView> bubbles;
    private int bubbleSize;
    private int bubbleSpeed;

    private boolean isShooting;
    private ImageView currentBubble;
    private ValueAnimator bubbleAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamelayout);

        gameLayout = findViewById(R.id.gameLayout);
        aimingDevice = findViewById(R.id.aimingDevice);

        // Get screen dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // Initialize bubble list
        bubbles = new ArrayList<>();

        // Set bubble size and speed (customize as per your game design)
        bubbleSize = 100;
        bubbleSpeed = 5;

        // Set touch listener for aiming device
        aimingDevice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && !isShooting) {
                    // Start shooting
                    shootBubble();
                    return true;
                }
                return false;
            }
        });

        // Start generating bubbles
        startBubbleGeneration();
    }

    private void startBubbleGeneration() {
        final Random random = new Random();

        // Generate bubbles at regular intervals
        bubbleAnimator = ValueAnimator.ofFloat(0f, 1f);
        bubbleAnimator.setDuration(3000); // Adjust the interval between bubble generations
        bubbleAnimator.setInterpolator(new LinearInterpolator());
        bubbleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        bubbleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value >= 0.9f) {
                    // Generate a new bubble
                    generateBubble(random);
                }
            }
        });
        bubbleAnimator.start();
    }

    private void generateBubble(Random random) {
        final ImageView bubble = new ImageView(this);
        bubble.setImageResource(R.drawable.bubble); // Customize the bubble image as per your game design

        // Set bubble position
        int x = random.nextInt(screenWidth - bubbleSize);
        bubble.setX(x);
        bubble.setY(-bubbleSize);

        // Set bubble size
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(bubbleSize, bubbleSize);
        bubble.setLayoutParams(params);

        // Add bubble to the game layout
        gameLayout.addView(bubble);

        // Start bubble animation
        ValueAnimator bubbleMovement = ValueAnimator.ofFloat(-bubbleSize, screenHeight);
        bubbleMovement.setDuration((long) (screenHeight / bubbleSpeed));
        bubbleMovement.setInterpolator(new LinearInterpolator());
        bubbleMovement.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                bubble.setY(value);

                // Check if the bubble collides with the aiming device
                if (isColliding(bubble, aimingDevice)) {
                    // Remove the bubble
                    gameLayout.removeView(bubble);
                    bubbles.remove(bubble);
                    // Stop the bubble animation
                    animation.cancel();

                    // Handle bubble collision logic here
                    handleBubbleCollision();
                }

                // Check if the bubble reaches the bottom of the screen
                if (value >= screenHeight) {
                    // Remove the bubble
                    gameLayout.removeView(bubble);
                    bubbles.remove(bubble);

                    // Stop the bubble animation
                    animation.cancel();

                    // Handle bubble reaching the bottom logic here
                    handleBubbleReachedBottom();
                }
            }
        });
        bubbleMovement.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // Remove the bubble from the list when the animation ends
                bubbles.remove(bubble);
            }
        });
        bubbleMovement.start();

        // Add the bubble to the list
        bubbles.add(bubble);
    }

    private void shootBubble() {
        // Create a new bubble for shooting
        currentBubble = new ImageView(this);
        currentBubble.setImageResource(R.drawable.bubble); // Customize the bubble image as per your game design

        // Set bubble size
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(bubbleSize, bubbleSize);
        currentBubble.setLayoutParams(params);

        // Set initial position of the bubble
        int x = (int) aimingDevice.getX() + aimingDevice.getWidth() / 2 - bubbleSize / 2;
        int y = (int) aimingDevice.getY() - bubbleSize;
        currentBubble.setX(x);
        currentBubble.setY(y);

        // Add the bubble to the game layout
        gameLayout.addView(currentBubble);

        // Shoot the bubble upwards
        ValueAnimator bubbleShoot = ValueAnimator.ofFloat(y, -bubbleSize);
        bubbleShoot.setDuration((long) (screenHeight / bubbleSpeed));
        bubbleShoot.setInterpolator(new LinearInterpolator());
        bubbleShoot.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                currentBubble.setY(value);

                // Check if the bubble collides with any existing bubbles
                for (ImageView bubble : bubbles) {
                    if (isColliding(currentBubble, bubble)) {
                        // Remove the collided bubble
                        gameLayout.removeView(bubble);
                        bubbles.remove(bubble);

                        // Stop the bubble animation
                        animation.cancel();

                        // Handle bubble collision logic here
                        handleBubbleCollision();
                        return;
                    }
                }

                // Check if the bubble reaches the top of the screen
                if (value <= -bubbleSize) {
                    // Remove the bubble
                    gameLayout.removeView(currentBubble);

                    // Stop the bubble animation
                    animation.cancel();

                    // Handle bubble reaching the top logic here
                    handleBubbleReachedTop();
                }
            }
        });
        bubbleShoot.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // Remove the bubble from the game layout when the animation ends
                gameLayout.removeView(currentBubble);
            }
        });
        bubbleShoot.start();
    }

    private boolean isColliding(View view1, View view2) {
        int[] view1Location = new int[2];
        int[] view2Location = new int[2];
        view1.getLocationOnScreen(view1Location);
        view2.getLocationOnScreen(view2Location);

        int view1X = view1Location[0];
        int view1Y = view1Location[1];
        int view2X = view2Location[0];
        int view2Y = view2Location[1];

        int view1Right = view1X + view1.getWidth();
        int view1Bottom = view1Y + view1.getHeight();
        int view2Right = view2X + view2.getWidth();
        int view2Bottom = view2Y + view2.getHeight();

        return view1X < view2Right && view1Right > view2X && view1Y < view2Bottom && view1Bottom > view2Y;
    }

    private void handleBubbleCollision() {
        // Handle bubble collision logic here
        Toast.makeText(this, "Bubble collided!", Toast.LENGTH_SHORT).show();
    }

    private void handleBubbleReachedTop() {
        // Handle bubble reaching the top logic here
        Toast.makeText(this, "Bubble reached the top!", Toast.LENGTH_SHORT).show();
    }

    private void handleBubbleReachedBottom() {
        // Handle bubble reaching the bottom logic here
        Toast.makeText(this, "Bubble reached the bottom!", Toast.LENGTH_SHORT).show();
    }
}

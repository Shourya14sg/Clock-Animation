# Clock-Animation
In this project, I created 3 mutable states for sec, min & hours which will be updated in LaunchedEffect.
Then I used parent box with Center Alignent property and Back background property which contains "AnalogClockComponent" composable. This comosable takes hr,min and sec states and contains 2 boxes
one for outer light effect and inner for Canvas to design clock.
The Light effect is handled by coroutineScope and blurRadius states and using coroutineScope launcher to adjust blurRadius through our animation in LaunchEffect which creates infinitely for light effect .
The outer box aligns to center uses our newly made Modifier function property named shadowCircular
which takes offset values gradientcolors and blurRadius and takes CircleShape clip property inside which the clock is designed .
The inner Box also uses same shadowCircular,clip property, with additional aspect-ratio and black background property inside which Canvas is designed for second, hour, minute and quater-wise time indicator(12,3,6,9).
The shadowCircular Modifier takes color, list of gradientcolors,borderRadius state and offsets to draw /Paint  the gradient colours around clock using sweepGradientShader and blurmaskfilter into a circular canvas. 
# Preview/ Demostration
check ProjectPeview Directory for video and Photo Preview

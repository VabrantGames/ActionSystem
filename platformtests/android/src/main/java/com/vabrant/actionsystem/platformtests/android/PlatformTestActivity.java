package com.vabrant.actionsystem.platformtests.android;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import com.vabrant.actionsystem.platformtests.tests.PlatformTest;
import com.vabrant.actionsystem.platformtests.tests.PlatformTests;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class PlatformTestActivity extends AndroidApplication {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		// use the full display, even if we have a device with a notch
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			Window applicationWindow = getApplicationWindow();
			WindowManager.LayoutParams attrib = applicationWindow.getAttributes();
			attrib.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
		}

		// obtain the test info
		Bundle extras = getIntent().getExtras();
		String testName = (String)extras.get("test");
		PlatformTest test = PlatformTests.newTest(testName);

		// and run the application...
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		config.useRotationVectorSensor = true;
		config.useGyroscope = true;
		initialize(test, config);
    }
}

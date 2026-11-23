package app.kaizen1;

import android.os.Bundle;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    registerPlugin(KaizenBioPlugin.class);
    super.onCreate(savedInstanceState);
  }
}

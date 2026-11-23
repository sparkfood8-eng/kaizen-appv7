package app.kaizen1;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "KaizenBio")
public class KaizenBioPlugin extends Plugin {

  @PluginMethod
  public void isAvailable(PluginCall call) {
    int r = BiometricManager.from(getContext()).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK);
    JSObject o = new JSObject();
    o.put("isAvailable", r == BiometricManager.BIOMETRIC_SUCCESS);
    call.resolve(o);
  }

  @PluginMethod
  public void verifyIdentity(final PluginCall call) {
    final String title = call.getString("title", "Kaizen 1%");
    final String sub = call.getString("subtitle", "");
    final String desc = call.getString("description", "");
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        try {
          BiometricPrompt.PromptInfo info = new BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(sub)
            .setDescription(desc)
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK)
            .setNegativeButtonText("Dùng PIN")
            .build();
          BiometricPrompt prompt = new BiometricPrompt(
            getActivity(),
            ContextCompat.getMainExecutor(getContext()),
            new BiometricPrompt.AuthenticationCallback() {
              @Override
              public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                call.resolve();
              }
              @Override
              public void onAuthenticationError(int errorCode, CharSequence errString) {
                call.reject(String.valueOf(errString));
              }
            }
          );
          prompt.authenticate(info);
        } catch (Exception e) {
          call.reject(String.valueOf(e.getMessage()));
        }
      }
    });
  }
}

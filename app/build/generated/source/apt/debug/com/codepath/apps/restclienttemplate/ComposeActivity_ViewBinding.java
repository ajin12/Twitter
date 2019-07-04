// Generated code from Butter Knife. Do not modify!
package com.codepath.apps.restclienttemplate;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ComposeActivity_ViewBinding<T extends ComposeActivity> implements Unbinder {
  protected T target;

  @UiThread
  public ComposeActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.etTweetInput = Utils.findRequiredViewAsType(source, R.id.etTweetInput, "field 'etTweetInput'", EditText.class);
    target.btnSend = Utils.findRequiredViewAsType(source, R.id.bTweet, "field 'btnSend'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.etTweetInput = null;
    target.btnSend = null;

    this.target = null;
  }
}

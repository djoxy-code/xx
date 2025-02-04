// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.ui.navigation;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class BackAction extends NavigationAction {

  public BackAction(JComponent c, @NotNull Disposable parentDisposable) {
    super(c, "Back", parentDisposable);
  }

  @Override
  protected void doUpdate(final AnActionEvent e) {
    e.getPresentation().setEnabled(getHistory(e).canGoBack());
  }

  @Override
  public void actionPerformed(@NotNull final AnActionEvent e) {
    getHistory(e).back();
  }
}

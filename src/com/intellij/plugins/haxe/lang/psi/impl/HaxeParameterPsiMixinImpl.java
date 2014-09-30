/*
 * Copyright 2000-2013 JetBrains s.r.o.
 * Copyright 2014-2014 AS3Boyan
 * Copyright 2014-2014 Elias Ku
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.plugins.haxe.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.plugins.haxe.lang.lexer.HaxeTokenTypes;
import com.intellij.plugins.haxe.lang.psi.HaxeComponent;
import com.intellij.plugins.haxe.lang.psi.HaxeComponentName;
import com.intellij.plugins.haxe.lang.psi.HaxeParameter;
import com.intellij.plugins.haxe.lang.psi.HaxeParameterPsiMixin;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * @author: Srikanth.Ganapavarapu
 */
public abstract class HaxeParameterPsiMixinImpl extends AbstractHaxeNamedComponent implements HaxeParameterPsiMixin {

  private static final Logger LOG = Logger.getInstance("#com.intellij.plugins.haxe.lang.psi.impl.HaxeParameterBase");

  public HaxeParameterPsiMixinImpl(ASTNode node) {
    super(node);
  }

  public HaxeParameterPsiMixinImpl(PsiParameter parameter) {
    super(parameter.getNode());
  }

  public HaxeParameterPsiMixinImpl(HaxeParameter parameter) {
    super(parameter.getNode());
  }

  @Override
  @NotNull
  public PsiElement getDeclarationScope() {
    // Lifted, lock, stock, and barrel from PsiParameterImpl.java
    // which was for the Java language.
    // TODO: [TiVo] Need to verify against the Haxe language spec.
    //              Are there other situations?
    final PsiElement parent = getParent();
    if (parent == null) return this;

    if (parent instanceof PsiParameterList) {
      return parent.getParent();
    }
    if (parent instanceof PsiForeachStatement) {
      return parent;
    }
    if (parent instanceof PsiCatchSection) {
      return parent;
    }

    PsiElement[] children = parent.getChildren();
    //noinspection ConstantConditions
    if (children != null) {
      ext:
      for (int i = 0; i < children.length; i++) {
        if (children[i].equals(this)) {
          for (int j = i + 1; j < children.length; j++) {
            if (children[j] instanceof PsiCodeBlock) return children[j];
          }
          break ext;
        }
      }
    }

    LOG.error("Code block not found among parameter' (" + this + ") parent' (" + parent + ") children: " + Arrays.asList(children));
    return null;
  }

  @Override
  public boolean isVarArgs() {
    // In Haxe (http://old.haxe.org/doc/cross/reflect), there are no
    // varargs parameters, but the function is made to accept variable
    // arguments.  So, at this level it's always false.
    return false;
  }

  @Nullable
  @Override
  public PsiTypeElement getTypeElement() {
    // Lifted, lock, stock, and barrel from PsiParameterImpl.java
    // which was for the Java language.
    // TODO: [TiVo] Need to verify against the Haxe language spec.
    //              Are there other situations?
    for (PsiElement child = getFirstChild(); child != null; child = child.getNextSibling()) {
      if (child instanceof PsiTypeElement) {
        //noinspection unchecked
        return (PsiTypeElement)child;
      }
    }
    return null;
  }

  @NotNull
  @Override
  public PsiType getType() {
    // The Haxe language variable type (int, float, etc.), not the psi token type.
    // TODO: [TiVo] Implement.
    return null;
  }

  @Nullable
  @Override
  public PsiExpression getInitializer() {
    // TODO: [TiVo] Implement.
    return null;
  }

  @Override
  public boolean hasInitializer() {
    // TODO: [TiVo] Implement.
    return false;
  }

  @Override
  public void normalizeDeclaration() throws IncorrectOperationException {
    // TODO: [TiVo] Implement.
  }

  @Nullable
  @Override
  public Object computeConstantValue() {
    // TODO: [TiVo] Implement.
    return null;
  }

  @Nullable
  @Override
  public PsiIdentifier getNameIdentifier() {
    // TODO: [TiVo] Implement.
    return null;
  }

  @Nullable
  @Override
  public PsiModifierList getModifierList() {
    // TODO: [TiVo] Implement.
    return null;
  }

  @Override
  public boolean hasModifierProperty(@PsiModifier.ModifierConstant @NonNls @NotNull String name) {
    // TODO: [TiVo] Implement.
    return false;
  }
}
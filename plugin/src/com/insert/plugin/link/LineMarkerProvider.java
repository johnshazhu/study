package com.insert.plugin.link;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.ProjectScope;
import com.intellij.psi.search.searches.AnnotatedElementsSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.lib.annotation.Insert;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class LineMarkerProvider extends RelatedItemLineMarkerProvider {
    private static final String INJECT_ANNOTATION_NAME = Insert.class.getName();
    private static final String INJECT_ANNOTATION_TARGET = "target";
    private static final String INJECT_ANNOTATION_CLASSPATH = "classPath";
    private static final String INJECT_ANNOTATION_TARGET_NAME = "name";

    private CopyOnWriteArrayList<LineMarkerRelateInfo> list = new CopyOnWriteArrayList<>();
    private Icon icon;
    private PsiClass psiClass = null;
    private boolean isFoundInjectAnnotation = false;

    public LineMarkerProvider() {
        icon = IconLoader.getIcon("/images/tip.png");
    }

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo> result) {
        Project project = element.getProject();
        if (element instanceof PsiFile) {
            // 先找到定义的注解类
            if (psiClass == null) {
                psiClass = JavaPsiFacade.getInstance(project).findClass(INJECT_ANNOTATION_NAME, GlobalSearchScope.allScope(project));
            }
            if (!isFoundInjectAnnotation && psiClass != null) {
                isFoundInjectAnnotation = true;
                System.out.println("psiClass : " + psiClass.getName() + ", element : " + element);
                // 查找使用注解类的方法及字段
                AnnotatedElementsSearch.searchElements(psiClass,
                        ProjectScope.getAllScope(project),
                        PsiMethod.class, PsiField.class).forEach(psiNameIdentifierOwner -> {
                            addInjectElement(psiNameIdentifierOwner);
                        });
            }
        } else if (element instanceof PsiAnnotation) {
            if (INJECT_ANNOTATION_NAME.equals(((PsiAnnotation) element).getQualifiedName())) {
                PsiNameIdentifierOwner psiNameIdentifierOwner = PsiTreeUtil.getParentOfType(element, PsiMethod.class, PsiField.class);
                //System.out.println("PsiAnnotation element owner : " + psiNameIdentifierOwner);
                addInjectElement(psiNameIdentifierOwner);
            }
        }

        if (list.size() > 0 && (element instanceof PsiMethod || element instanceof PsiField)) {
            System.out.println("element : " + element);
            ArrayList<PsiNameIdentifierOwner> targets = new ArrayList<>();
            for (LineMarkerRelateInfo info : list) {
                if (info.target != null && info.target.isEquivalentTo(element)) {
                    targets.add(info.source);
                }
            }

            if (targets.size() > 0) {
                System.out.println("size : " + targets.size());
                try {
                    result.add(NavigationGutterIconBuilder.create(icon)
                            .setTargets(targets)
                            .setTooltipText("click to inject source")
                            .createLineMarkerInfo(((PsiNameIdentifierOwner) element).getNameIdentifier()));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addInjectElement(PsiNameIdentifierOwner psiNameIdentifierOwner) {
        LineMarkerRelateInfo info = new LineMarkerRelateInfo();
        info.source = psiNameIdentifierOwner;
        info.target = getTarget(psiNameIdentifierOwner);
        list.add(info);
    }

    private PsiNameIdentifierOwner getTarget(PsiNameIdentifierOwner owner) {
        String targetCls = "";
        String targetName = "";
        if (owner instanceof PsiModifierListOwner) {
            PsiAnnotation psiAnnotation = ((PsiModifierListOwner) owner).getAnnotation(INJECT_ANNOTATION_NAME);
            if (psiAnnotation != null) {
                PsiAnnotationMemberValue classPath = psiAnnotation.findAttributeValue(INJECT_ANNOTATION_CLASSPATH);
                if ((classPath instanceof PsiLiteralExpression) && (((PsiLiteralExpression) classPath).getValue() != null)) {
                    targetCls = ((PsiLiteralExpression) classPath).getValue().toString();
                }
                if (targetCls == null || targetCls.isEmpty()) {
                    PsiAnnotationMemberValue target = psiAnnotation.findAttributeValue(INJECT_ANNOTATION_TARGET);
                    if (target instanceof PsiClassObjectAccessExpression) {
                        targetCls = ((PsiClassObjectAccessExpression) target).getOperand().getType().getCanonicalText();
                    }
                }
                PsiAnnotationMemberValue name = psiAnnotation.findAttributeValue(INJECT_ANNOTATION_TARGET_NAME);
                if ((name instanceof PsiLiteralExpression) && (((PsiLiteralExpression) name).getValue() != null)) {
                    targetName = ((PsiLiteralExpression) name).getValue().toString();
                }
            }
        }

        if (targetCls != null && !targetCls.isEmpty()) {
            if (targetName == null || targetName.isEmpty()) {
                targetName = owner.getName();
            }

            PsiClass targetClass = JavaPsiFacade.getInstance(owner.getProject()).findClass(targetCls, GlobalSearchScope.allScope(owner.getProject()));
            System.out.println("getTarget targetClass : " + targetClass);
            if (owner instanceof PsiMethod) {
                PsiMethod method = (PsiMethod) owner;
                PsiMethod targetMethod = null;
                if (targetClass != null) {
                    if (targetClass.isInterface()) {
                        targetMethod = method;
                    } else {
                        PsiMethod[] psiMethods = targetClass.findMethodsByName(targetName, false);
                        for (PsiMethod psiMethod : psiMethods) {
                            if (isSameMethodParams(psiMethod, method)) {
                                targetMethod = psiMethod;
                                break;
                            }
                        }
                    }
                }

                return targetMethod;
            } else if (owner instanceof PsiField) {
                PsiField targetField = null;
                if (targetClass != null) {
                    targetField = targetClass.findFieldByName(targetName, false);
                }
                return targetField;
            }
        }

        return null;
    }

    private boolean isSameMethodParams(PsiMethod left, PsiMethod right) {
        if (left == null || right == null || left.getParameterList().getParametersCount() != right.getParameterList().getParametersCount()) {
            return false;
        }

        for (int i = 0; i < left.getParameterList().getParametersCount(); i++) {
            if (!left.getParameterList().getParameters()[i].getName().equals(right.getParameterList().getParameters()[i].getName())) {
                return false;
            }
        }
        return true;
    }

    class LineMarkerRelateInfo {
        PsiNameIdentifierOwner source;
        PsiNameIdentifierOwner target;
    }
}

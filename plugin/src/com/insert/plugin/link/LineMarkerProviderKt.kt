package com.insert.plugin.link

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.ProjectScope
import com.intellij.psi.search.searches.AnnotatedElementsSearch
import com.lib.annotation.Inject
import org.jetbrains.kotlin.asJava.classes.KtLightClassImpl
import org.jetbrains.kotlin.asJava.elements.*
import org.jetbrains.kotlin.idea.references.KtSimpleNameReference
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.findPropertyByName
import java.util.concurrent.CopyOnWriteArrayList
import javax.swing.Icon

/**
 * refer : https://intellij-support.jetbrains.com/hc/en-us/community/posts/115000724850-Make-Intellij-Idea-plugin-work-with-Kotlin-files?flash_digest=5ff9dab2613759d5822ed7ca3db04beebb43a36a
 *
 */
class LineMarkerProviderKt : RelatedItemLineMarkerProvider() {
    var markerIcon: Icon
    var list: CopyOnWriteArrayList<LineMarkerRelateInfo> = CopyOnWriteArrayList()
    var isFoundInjectAnnotation: Boolean = false
    var psiClass: PsiClass? = null

    init {
        markerIcon = IconLoader.getIcon("/images/tip.png")
    }

    /*
    * Finally, you need to use the Kotlin PSI classes to implement your logic (KtFile instead of PsiJavaFile, KtSimpleNameExpression instead of PsiReferenceExpression, etc.)
    * */
    override fun collectNavigationMarkers(element: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<PsiElement>>) {
        var project = element.project
        if (element is PsiFile) {
            println("collectNavigationMarkers PsiFile : $element")
        }
        if (element is KtFile) {
            psiClass = JavaPsiFacade.getInstance(project).findClass(INJECT_ANNOTATION_NAME, GlobalSearchScope.allScope(project))
            if (!isFoundInjectAnnotation) {
                psiClass?.let {
                    isFoundInjectAnnotation = true
                    println("psiClass : ${psiClass?.name}")

                    AnnotatedElementsSearch.searchElements(psiClass!!,
                            ProjectScope.getAllScope(project),
                            KtLightMethod::class.java, KtLightField::class.java).forEach {
                        println("it : $it")
                        addInjectElement(it)
                    }
                }
            }
        }

        if (list.size > 0 && (element is KtProperty || element is KtFunction)) {
            println("element :  $element")
            var targets: ArrayList<PsiNameIdentifierOwner> = ArrayList<PsiNameIdentifierOwner>()
            list.forEach {
                if (it.target!!.isEquivalentTo(element)) {
                    targets.add(it.source!!)
                }
            }

            if (targets.size > 0) {
                println("size :  ${targets.size}")
                result.add(NavigationGutterIconBuilder.create(markerIcon)
                        .setTargets(targets)
                        .setTooltipText("click to inject source")
                        .createLineMarkerInfo((element as PsiNameIdentifierOwner).nameIdentifier!!))
            }
        }
    }

    private fun addInjectElement(owner: PsiModifierListOwner) {
        var info: LineMarkerRelateInfo = LineMarkerRelateInfo()
        owner.annotations
                .filter {
                    it.attributes.size > 0
                }
                .forEach {
                    var map = it.attributes.associateBy({ it.attributeName }, { getAttributeValue(it) })
                    println("map : $map")
                    if (map.isNotEmpty()) {
                        val targetCls = map[INJECT_ANNOTATION_CLASSPATH]?:map[INJECT_ANNOTATION_TARGET]
                        var targetName = map[INJECT_ANNOTATION_TARGET_NAME]
                        targetCls?.isEmpty().let {
                            if (targetName == null || targetName!!.isEmpty()) {
                                targetName = (owner as PsiNameIdentifierOwner).name
                            }

                            var targetClass: PsiClass? = JavaPsiFacade.getInstance(owner.project).findClass(targetCls!!, GlobalSearchScope.allScope(owner.project))
                            println("owner : $owner")
                            if (owner is KtLightMethod) {
                                var method: KtLightMethod = owner as KtLightMethod
                                var targetMethod: KtLightMethod? = null
                                targetClass?.let {
                                    if (targetClass!!.isInterface) {
                                        targetMethod = method
                                        info.source = owner
                                        info.target = method
                                    } else {
                                        var ktMethods = targetClass!!.findMethodsByName(targetName, false)
                                        run loop@{
                                            ktMethods.forEach {
                                                println("method : $it")
                                                if (isSameMethodParams(it as KtLightMethod, method)) {
                                                    targetMethod = it
                                                    info.source = owner
                                                    info.target = it
                                                    return@loop
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (owner is KtLightField) {
                                var targetField: KtLightField? = null
                                targetClass?.let {
                                    targetField = targetClass!!.findFieldByName(targetName, false) as KtLightField
                                }
                            } else {

                            }
                        }
                    }
                }

        if (owner.annotations.size == 1 && owner.annotations[0].attributes.size == 0) {
            println("owner : $owner")
            if (owner is KtLightFieldImpl.KtLightFieldForDeclaration) {
                val valueArgument = (owner.navigationElement as KtProperty).annotationEntries[0].valueArguments.find {
                    it.getArgumentName()?.asName?.identifier == INJECT_ANNOTATION_CLASSPATH
                } as? KtValueArgument

                val expression = valueArgument?.lastChild
                val references = when (expression) {
                    is KtDotQualifiedExpression -> expression.lastChild.references
                    is KtNameReferenceExpression -> expression.references
                    is KtStringTemplateExpression -> expression.references
                    else -> null
                }
                if (expression is KtStringTemplateExpression) {
                    val reference = references!![0].canonicalText
                    println("KtStringTemplateExpression is $reference")
                    if (reference.contains("$")) {
                        var tmp = reference.replace("$", ".")
                        var targetClass: PsiClass? = JavaPsiFacade.getInstance(owner.project).findClass(tmp, GlobalSearchScope.allScope(owner.project))
                        targetClass?.let {
                            var targetField = (targetClass as KtLightClassImpl).kotlinOrigin.findPropertyByName(owner.name)
                            targetField?.let {
                                info.source = owner.kotlinOrigin as PsiNameIdentifierOwner
                                info.target = targetField
                            }
                        }
                    } else {
                        var targetClass: PsiClass? = JavaPsiFacade.getInstance(owner.project).findClass(reference, GlobalSearchScope.allScope(owner.project))
                        targetClass?.let {
                            var targetField = (targetClass as KtLightClassImpl).kotlinOrigin.findPropertyByName(owner.name)
                            targetField?.let {
                                info.source = owner
                                info.target = targetField
                            }
                        }
                    }
                } else {
                    val reference = references?.find { it is KtSimpleNameReference }?.resolve()
                    println(reference)
                }
            }
        }
        info.target?.let {
            list.add(info)
        }
    }

    private fun getAttributeValue(it: Any): String? {
        var value: String? = ""
        var attribute: KtLightPsiNameValuePair = it as KtLightPsiNameValuePair
        if (attribute.value is KtLightPsiClassObjectAccessExpression) {
            value = (attribute.value as KtLightPsiClassObjectAccessExpression).operand.type.canonicalText
        } else if (attribute.value is KtLightPsiLiteral) {
            value = (attribute.value as KtLightPsiLiteral).value.toString()
        }

        return value
    }

    private fun isSameMethodParams(left: KtLightMethod?, right: KtLightMethod?): Boolean {
        var count = left?.parameterList?.parametersCount
        if (count == right?.parameterList?.parametersCount) {
            for (i in left?.parameterList?.parameters?.indices!!) {
                if (!left.parameterList.parameters[i].name.equals(right?.parameterList?.parameters!![i].name)) {
                    return false
                }
            }
            return true
        }

        return false
    }

    companion object {
        private val INJECT_ANNOTATION_NAME = Inject::class.java.name
        private const val INJECT_ANNOTATION_TARGET = "target"
        private const val INJECT_ANNOTATION_CLASSPATH = "classPath"
        private const val INJECT_ANNOTATION_TARGET_NAME = "name"
        private const val INJECT_ANNOTATION_REPLACE = "replace"
    }

    class LineMarkerRelateInfo {
        var source: PsiNameIdentifierOwner? = null
        var target: PsiNameIdentifierOwner? = null
    }
}
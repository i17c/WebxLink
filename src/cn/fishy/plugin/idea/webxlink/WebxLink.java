package cn.fishy.plugin.idea.webxlink;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 *
 * vm&java»¥ÏàÌø×ªÆ÷
 *
 * User: duxing
 * Date: 14-9-14.
 */
public class WebxLink extends AnAction {

    public void actionPerformed(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Project project = e.getProject();
        if(project!=null && psiFile!=null){
            Jumper myComponent = Jumper.getInstance(project);
            myComponent.jumper(psiFile);
        }
    }

}

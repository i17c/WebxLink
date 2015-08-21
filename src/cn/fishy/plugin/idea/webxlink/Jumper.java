package cn.fishy.plugin.idea.webxlink;

import cn.fishy.plugin.idea.webxlink.domain.FileMatcher;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.PsiShortNamesCache;
import org.jetbrains.annotations.NotNull;

/**
 * User: duxing
 * Date: 14-9-14.
 */
public class Jumper implements ProjectComponent {
    private Project project;

    public static Jumper getInstance(Project project){
        return project.getComponent(Jumper.class);
    }
    
    public Jumper(Project project) {
        this.project = project;
    }

    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @NotNull
    public String getComponentName() {
        return "cn.fishy.plugin.idea.webxlink.Jumper";
    }

    public void projectOpened() {
        // called when project is opened
    }

    public void projectClosed() {
        // called when project is being closed
    }

    public void jumper(PsiFile psiFile){
        FileEditorManager fe = FileEditorManager.getInstance(project);
        if(psiFile!=null && fe!=null) {
            FileMatcher fileMatcher = new FileMatcher(psiFile);
            if(fileMatcher.isMatch()){
                String jumperFileNameWithExt = fileMatcher.getJumperFileNameWithExt();
                PsiShortNamesCache shortNamesCache = PsiShortNamesCache.getInstance(project);
                if(shortNamesCache!=null){
                    PsiFile[] files = shortNamesCache.getFilesByName(jumperFileNameWithExt);
                    PsiFile file = FileMatcher.checkJumperFile(files, fileMatcher);
                    if(file!=null && file.getVirtualFile()!=null){
                        fe.openFile(file.getVirtualFile(),true);
                        System.out.println(file.getVirtualFile().getPath());
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws Exception {
        String s = "/transformer/src/main/java/com/taobao/ju/transformer/web/module/screen/model/ModelEdit.java";
        String v = "/transformer/src/main/webapp/wodelEditeb/templates/screen/model/rangeIntro.vm";
        String x = "/transformer/src/main/java/com/taobao/ju/transformer/web/module/control/Index.java";
        String y = "/transformer/src/main/webapp/web/templates/control/intro.vm";

    }


}

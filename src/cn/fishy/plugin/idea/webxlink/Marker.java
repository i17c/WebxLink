package cn.fishy.plugin.idea.webxlink;

import cn.fishy.plugin.idea.webxlink.domain.FileMatcher;
import cn.fishy.plugin.idea.webxlink.util.PsiClassManager;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.Function;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * User: duxing
 * Date: 14-9-16.
 */
public class Marker implements ProjectComponent {

    public static final Icon Commit = IconLoader.getIcon("/actions/commit.png"); // 16x16
    Icon icon = AllIcons.General.PackagesTab;
    private Project project;
    private Editor selectedEditor;
    private Timer timer;
    private Jumper jumper;
    Function<PsiElement,String> tooltip = new Function<PsiElement, String>() {
        @Override
        public String fun(PsiElement psiElement) {
            return "jumper between vm & screen/control";
        }
    };
    GutterIconNavigationHandler<PsiElement> gutter = new GutterIconNavigationHandler<PsiElement>() {
        @Override
        public void navigate(MouseEvent e, PsiElement elt) {
            getJumper().jumper(elt.getContainingFile());
        }
    };

    public static Marker getInstance(Project project){
        return project.getComponent(Marker.class);
    }

    public Marker(Project project) {
        this.project = project;
    }

    @Override
    public void projectOpened() {
        /*StartupManager.getInstance(project).registerPostStartupActivity(new Runnable() {
            public void run() {
//                startTimer();
               // markIcon();
                FileEditorManager.getInstance(project).addFileEditorManagerListener(new MarkerListener(project));
            }
        });*/
        MessageBus bus = project.getMessageBus();
        bus.connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER , new MarkerListener(project));

    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "cn.fishy.plugin.idea.webxlink.Marker";
    }

    public void markClass(PsiClass psiClass, Editor editor) {
        Integer line = getClassStartLineNumber(psiClass, editor);
        if(line==null)return;
        markElement(editor, line, psiClass);
    }

    private void markFile(PsiFile psiFile, Editor editor) {
        Integer line = 0;
        PsiElement e = psiFile.getFirstChild();
        markElement(editor, line, e);
    }

    private void markElement(Editor editor, Integer line, PsiElement e) {
        MarkupModel markupModel = editor.getMarkupModel();
        RangeHighlighter[] lighters = markupModel.getAllHighlighters();
        if(lighters.length>0){
            for(RangeHighlighter l : lighters){
                if(l!=null && l.getGutterIconRenderer()!=null && icon.equals(l.getGutterIconRenderer().getIcon())){
                    return;
                }
            }
        }
        RangeHighlighter rangeHighlighter = markupModel.addLineHighlighter(line, HighlighterLayer.FIRST, null);
        addGutterIcon(rangeHighlighter, e);
    }

    public void markIcon() {
        FileEditorManager fe = FileEditorManager.getInstance(project);
        Editor ed = fe.getSelectedTextEditor();
        if (ed==null)return;
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(ed.getDocument());
        FileMatcher fileMatcher = new FileMatcher(psiFile);
        if(fileMatcher.isMatch()){
            if(fileMatcher.getFileType().isJava()){
                Editor editor = fe.getSelectedTextEditor();
                PsiClassManager psiClassManager = PsiClassManager.getInstance(project);
                PsiClass clz = psiClassManager.findPrimaryClass(psiFile);
                markClass(clz,editor);
            }else{
                Editor editor = fe.getSelectedTextEditor();
                markFile(psiFile,editor);
            }
        }

    }

    private void addGutterIcon(RangeHighlighter rangeHighlighter, PsiElement psiElement) {
        LineMarkerInfo<PsiElement> lineMarkInfo = new LineMarkerInfo<PsiElement>(psiElement,rangeHighlighter.getStartOffset(),
                icon, Pass.UPDATE_OVERRIDEN_MARKERS, tooltip, gutter);
        GutterIconRenderer gutterIconRender = new LineMarkerInfo.LineMarkerGutterIconRenderer<PsiElement>(lineMarkInfo);
        rangeHighlighter.setGutterIconRenderer(gutterIconRender);
    }

    private Integer getClassStartLineNumber(PsiClass psiClass, Editor editor) {
        Integer offset = getClassStartOffset(psiClass);
        if(offset==null)return null;
        if(editor!=null){
            return editor.getDocument().getLineNumber(offset);
        }
        return null;
    }

    private Integer getClassStartOffset(PsiClass psiClass) {
        if(psiClass!=null && psiClass.getNameIdentifier()!=null){
            return psiClass.getNameIdentifier().getTextOffset();
        }
        return null;
    }

    public Jumper getJumper() {
        if (jumper==null)jumper=Jumper.getInstance(project);
        return jumper;
    }

//    private void startTimer() {
//        stopTimer();
//        timer = new Timer(4000, new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                stopTimer();
//                PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
//                if (documentManager.hasUncommitedDocuments()) {
//                    documentManager.commitAllDocuments();
//                }
//
//                markClass(selectedEditor);
//            }
//        });
//        timer.setRepeats(false);
//        timer.setCoalesce(true);
//        timer.start();
//    }

//    private void stopTimer() {
//        if (timer != null) {
//            timer.stop();
//            timer = null;
//        }
//    }
}

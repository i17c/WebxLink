package cn.fishy.plugin.idea.webxlink;

import cn.fishy.plugin.idea.webxlink.domain.FileType;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * User: duxing
 * Date: 14-9-16.
 */
public class MarkerListener implements FileEditorManagerListener {
    public MarkerListener(Project project) {
        this.project = project;
    }

    Project project;
    @Override
    public void fileOpened(@NotNull FileEditorManager fileEditorManager, @NotNull VirtualFile virtualFile) {
        String fileType = virtualFile.getFileType().getName();
        if(FileType.getTypes().contains(fileType)){
            Marker.getInstance(project).markIcon();
        }
    }

    @Override
    public void fileClosed(@NotNull FileEditorManager fileEditorManager, @NotNull VirtualFile virtualFile) {

    }

    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent fileEditorManagerEvent) {
//        Marker marker = Marker.getInstance(project);
//        marker.markIcon();
    }
}

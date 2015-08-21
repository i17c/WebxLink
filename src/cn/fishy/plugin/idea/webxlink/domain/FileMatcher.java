package cn.fishy.plugin.idea.webxlink.domain;

import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: duxing
 * Date: 14-9-15.
 */
public class FileMatcher {
    private FileType fileType = FileType.JAVA;
    private TplType tplType = TplType.SCREEN;
    private boolean match = false;
    private String path = "";
    private String name = "";

    public static String fileSeparator = System.getProperty("file.separator");


    public FileMatcher() {
    }

    public FileMatcher(PsiFile psiFile) {
        if(psiFile==null)return;
        String fileType=psiFile.getFileType().getName();
        String filePath = getFilePath(psiFile);
        if(StdFileTypes.JAVA.getName().equals(fileType)){ //java file
            this.fileType = FileType.JAVA;
            if(filePath.contains(TplType.CONTROL.getType())){
                checkForFile(filePath,TplType.CONTROL);
            }else if(filePath.contains(TplType.SCREEN.getType())){
                checkForFile(filePath,TplType.SCREEN);
            }
        }else if("VTL".equals(fileType)){ //vm file
            this.fileType = FileType.VM;
            if(filePath.contains(TplType.CONTROL_VM.getType())){
                checkForFile(filePath,TplType.CONTROL_VM);
            }else if(filePath.contains(TplType.SCREEN_VM.getType())){
                checkForFile(filePath,TplType.SCREEN_VM);
            }
        }
    }

    public boolean isMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.name = path.substring(path.lastIndexOf("/")+1);
        this.path = path.substring(0,path.lastIndexOf("/")+1);

    }

    public String getName() {
        return name;
    }

    public String getVmName() {
        return lowerCaseFirst(name);
    }

    public String getJavaName() {
        return upperCaseFirst(name);
    }

    public String getJumperFileName(){
        return this.fileType.isJava()?getVmName():(this.fileType.isVM()?getJavaName():null);
    }
    public String getJumperFileNameWithExt(){
        return this.isMatch()?getJumperFileName()+this.tplType.getFileType().getJumpExt():null;
    }

    public String getJumperFileNameWithExtAndMorePath(){
        return this.isMatch()?this.tplType.getJumperHeader()+getJumperFileNameWithExt():null;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String upperCaseFirst(String oldStr) {
        return oldStr.substring(0, 1).toUpperCase() + oldStr.substring(1);
    }

    private String lowerCaseFirst(String oldStr) {
        return oldStr.substring(0, 1).toLowerCase() + oldStr.substring(1);
    }

    public String toString(){
        return "{\"match\":"+this.match
                +(this.name!=null?",\"name\":\""+this.name+"\"":"")
                +(this.fileType!=null?",\"fileType\":\""+this.fileType.getType()+"\"":"")
                +(this.tplType!=null?",\"tplType\":\""+this.tplType.getType()+"\"":"")
                +(this.path!=null?",\"path\":\""+this.path+"\"":"")
                +(this.name!=null?",\"screen\":\""+this.getJavaName()+"\"":"")
                +(this.name!=null?",\"vm\":\""+this.getVmName()+"\"":"")
                +"}";
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public TplType getTplType() {
        return tplType;
    }

    public void setTplType(TplType tplType) {
        this.tplType = tplType;
    }

    private String getFilePath(PsiFile psiFile) {
        String filePath = "";
        VirtualFile vf = psiFile.getVirtualFile();
        if(vf!=null){
            filePath = vf.getPath();
        }
        return filePath;
    }

    private void checkForFile(String filePath,TplType tplType) {
        if(filePath==null)return;
        filePath = filePath.replace(fileSeparator,"/");
        this.tplType = tplType;
        Pattern p1 = Pattern.compile(tplType.getPattern());
        Matcher m1 = p1.matcher(filePath);
        if(m1.find()){
            this.match = true;
            this.setPath(m1.group(1));
        }
    }

    public static PsiFile checkJumperFile(PsiFile[] files, FileMatcher jumper) {
        if(files.length==1)return files[0];
        for(PsiFile psiFile : files){
            try {
                String path = psiFile.getVirtualFile().getPath().replace(fileSeparator,"/");
                String searchPath = jumper.getJumperFileNameWithExtAndMorePath();
                if(path.contains(searchPath)){
                    return psiFile;
                }
            }catch (Exception e){
//                log.error ignore
            }
        }
        return null;
    }

}

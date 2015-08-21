package cn.fishy.plugin.idea.webxlink.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件类型
 * User: duxing
 * Date: 14-9-15.
 */
public enum FileType {
    JAVA("JAVA",".java",".vm"),VM("VTL",".vm",".java");
    private String type;
    private String ext;
    private String jumpExt;

    FileType(String type,String ext,String jumpExt) {
        this.type = type;
        this.ext = ext;
        this.jumpExt = jumpExt;
    }

    public static FileType getFileType(String type){
        FileType f = null;
        for(FileType s:FileType.values()){
            if(s.getType().equals(type)){
                f=s;
                break;
            }
        }
        return f;
    }

    public static List<String> getTypes(){
        List<String> list = new ArrayList<String>();
        for(FileType s:FileType.values()){
            list.add(s.getType());
        }
        return list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isJava(){
        return this.getType().equals(JAVA.getType());
    }

    public boolean isVM(){
        return this.getType().equals(VM.getType());
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getJumpExt() {
        return jumpExt;
    }

    public void setJumpExt(String jumpExt) {
        this.jumpExt = jumpExt;
    }
}

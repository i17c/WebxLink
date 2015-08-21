package cn.fishy.plugin.idea.webxlink.domain;

/**
 * 文件模板类型
 * User: duxing
 * Date: 14-9-15.
 */
public enum TplType {

    SCREEN("screen","src/main/java/.*/screen/(.*)\\.java","/screen/","/templates/screen/", FileType.JAVA),
    CONTROL("control","src/main/java/.*/control/(.*)\\.java","/control/","/templates/control/",FileType.JAVA),
    SCREEN_VM("screen","src/main/webapp/.*/templates/screen/(.*)\\.vm","/screen/","/screen/",FileType.VM),
    CONTROL_VM("control","src/main/webapp/.*/templates/control/(.*)\\.vm","/control/","/control/",FileType.VM);
    private String type;
    private String pattern;
    private String header;
    private String jumperHeader;
    private FileType fileType;

    TplType(String type,String pattern,String header,String jumperHeader,FileType fileType) {
        this.type = type;
        this.pattern = pattern;
        this.header = header;
        this.jumperHeader = jumperHeader;
        this.fileType = fileType;
    }

    public static TplType getTplType(String type){
        TplType f = null;
        for(TplType s:TplType.values()){
            if(s.getType().equals(type)){
                f=s;
                break;
            }
        }
        return f;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isJava(){
        return this.getType().equals(SCREEN.getType());
    }

    public boolean isVM(){
        return this.getType().equals(CONTROL.getType());
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getJumperHeader() {
        return jumperHeader;
    }

    public void setJumperHeader(String jumperHeader) {
        this.jumperHeader = jumperHeader;
    }
}

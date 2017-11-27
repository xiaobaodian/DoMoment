package threecats.zhang.domoment.EventClass;

import threecats.zhang.domoment.ENUM.EditorMode;

/**
 * Created by zhang on 2017/11/27.
 */

public class TaskEditorEvent {
    EditorMode editorMode;
    public TaskEditorEvent(EditorMode editorMode){
       this.editorMode = editorMode;
    }
    public EditorMode getEditorMode(){
        return editorMode;
    }
}

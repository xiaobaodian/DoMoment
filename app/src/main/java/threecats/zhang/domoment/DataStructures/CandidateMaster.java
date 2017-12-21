package threecats.zhang.domoment.DataStructures;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
import threecats.zhang.domoment.App;
import threecats.zhang.domoment.DataStructures.CandidateItem;

/**
 * 由 zhang 于 2017/11/30 创建
 */

public class CandidateMaster {

    private Box<CandidateItem> candidateListBox;

    public CandidateMaster(){
        BoxStore boxStore = App.self().getBoxStore();
        candidateListBox = boxStore.boxFor(CandidateItem.class);
    }

    public List<CandidateItem> getAllCandidateList(){
        Query<CandidateItem> candidateListQuery = candidateListBox
                .query()
                .order(CandidateItem_.frequencySum)
                .build();
        return candidateListQuery.find();
    }

    public List<CandidateItem> getTypeCandidateList(int typeID){
        Query<CandidateItem> candidateListQuery = candidateListBox
                .query()
                .equal(CandidateItem_.typeID, typeID)
                .order(CandidateItem_.frequencySum)
                .build();
        return candidateListQuery.find();
    }

    public long put(CandidateItem item){
        return candidateListBox.put(item);
    }

    public CandidateItem get(long id){
        return candidateListBox.get(id);
    }

    public void remove(CandidateItem item){
        candidateListBox.remove(item);
    }
}

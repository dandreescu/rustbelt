package com.intellij.localvcs.core.changes;

import com.intellij.localvcs.core.IdPath;
import com.intellij.localvcs.core.Paths;
import com.intellij.localvcs.core.storage.Stream;
import com.intellij.localvcs.core.tree.RootEntry;

import java.io.IOException;

public class MoveChange extends StructuralChange {
  private String myNewParentPath; // transient
  private IdPath myTargetIdPath;

  public MoveChange(String path, String newParentPath) {
    super(path);
    myNewParentPath = newParentPath;
  }

  public MoveChange(Stream s) throws IOException {
    super(s);
    myTargetIdPath = s.readIdPath();
  }

  @Override
  public void write(Stream s) throws IOException {
    super.write(s);
    s.writeIdPath(myTargetIdPath);
  }

  @Override
  protected IdPath doApplyTo(RootEntry root) {
    IdPath firstIdPath = root.getEntry(myPath).getIdPath();

    root.move(myPath, myNewParentPath);
    myTargetIdPath = root.getEntry(getNewPath()).getIdPath();

    return firstIdPath;
  }

  private String getNewPath() {
    return Paths.appended(myNewParentPath, Paths.getNameOf(myPath));
  }

  @Override
  public void revertOn(RootEntry root) {
    IdPath newPath = myTargetIdPath;
    IdPath oldParentPath = myAffectedIdPath.getParent();
    root.move(newPath, oldParentPath);
  }

  @Override
  public IdPath[] getAffectedIdPaths() {
    return new IdPath[]{myAffectedIdPath, myTargetIdPath};
  }

  @Override
  public void accept(ChangeVisitor v) throws IOException, ChangeVisitor.StopVisitingException {
    v.visit(this);
  }
}

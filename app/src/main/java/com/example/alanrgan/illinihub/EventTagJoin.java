package com.example.alanrgan.illinihub;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

//@Entity(tableName = "event_tag_join",
//        primaryKeys = { "e_id", "tag_name"},
//        foreignKeys = {
//            @ForeignKey(entity = Event.class,
//                        parentColumns = "eventId",
//                        childColumns = "e_id"),
//            @ForeignKey(entity = Tag.class,
//                        parentColumns = "tagName",
//                        childColumns = "tag_name")
//        })
@Entity
public class EventTagJoin {
  @PrimaryKey(autoGenerate = true)
  public int id;

  @NonNull
  public Integer event_id;

  @NonNull
  public String tag_name;

  public EventTagJoin(int event_id, String tag_name){
    this.event_id = event_id;
    this.tag_name = tag_name;
  }
}

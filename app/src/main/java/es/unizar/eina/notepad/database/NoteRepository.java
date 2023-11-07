package es.unizar.eina.notepad.database;


import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {

    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;

    // Note that in order to unit test the NoteRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
        mAllNotes = mNoteDao.getOrderedNotes();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    /** Inserta una nota
     * @param note
     * @return un valor entero largo con el identificador de la nota que se ha creado.
     */
    public long insert(Note note) {
        final long[] result = {0};
        // You must call this on a non-UI thread or your app will throw an exception. Room ensures
        // that you're not doing any long running operations on the main thread, blocking the UI.
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            result[0] = mNoteDao.insert(note);
        });
        return result[0];
    }

    /** Modifica una nota
     * @param note
     * @return un valor entero con el número de filas modificadas.
     */
    public int update(Note note) {
        final int[] result = {0};
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            result[0] = mNoteDao.update(note);
        });
        return result[0];
    }

    /** Elimina una nota
     * @param note
     * @return un valor entero con el número de filas eliminadas.
     */
    public int delete(Note note) {
        final int[] result = {0};
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            result[0] = mNoteDao.delete(note);
        });
        return result[0];
    }
}

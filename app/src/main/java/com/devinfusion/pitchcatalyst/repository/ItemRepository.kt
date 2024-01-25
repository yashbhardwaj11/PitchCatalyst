import com.devinfusion.pitchcatalyst.model.Item
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class ItemRepository(private val fireStore: FirebaseFirestore) {

    fun observeItems() = callbackFlow<List<Item>> {
        val listener = fireStore.collection("item")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (value != null) {
                    val items = value.toObjects(Item::class.java)
                    trySend(items).isSuccess
                }
            }

        awaitClose {
            listener.remove()
        }
    }
}

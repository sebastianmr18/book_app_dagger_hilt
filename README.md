<h1>📱 book_app </h1> 
Esta aplicación, desarrollada en Kotlin, tiene como objetivo explorar y explicar 
funcionalidades clave para el desarrollo de aplicaciones móviles. En ella se abordan temas como: 
<ul>
  <li>La arquitectura <b>MVVM (Model-View-ViewModel)</b></li>
  <li>Implementación de CRUD con Room y SQLite</li>
</ul>

<h2>Requerimientos</h2>
Entre los requerimientos, debemos modificar los gradle:
<li>build.gradel.kts (del proyecto)</li>

```bash
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
}
```

<li>build.gradel.kts (de la app)</li>

```bash
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id ("kotlin-kapt")
}

}

...
dependencies {
    ...
    //corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    //viewmodel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.activity:activity-ktx:1.8.0")
    implementation ("androidx.fragment:fragment-ktx:1.6.2")

    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")

    // Room
    implementation ("androidx.room:room-runtime:2.5.2")
    implementation ("androidx.room:room-ktx:2.5.2")
    ksp("androidx.room:room-compiler:2.5.2")
    implementation ("com.getbase:floatingactionbutton:1.10.1")
}
```

<h2><b>TEMA 1: </b>Arquitectura MVVM</h2>
Este es su diagrama:
<p align="center">
  <img src="https://github.com/user-attachments/assets/44b8562c-9539-4c89-a56c-25e2ae47555a" alt="Gràfico de la arquitectura" width="500" height="500">
</p>

Así que, se debe organizar los directorios de la siguiente forma:
<p align="center">
  <img src="https://github.com/user-attachments/assets/43cc506d-d461-456e-b6a8-f5f67e58eec1" alt="Organización de directorios" width="300" height="500">
</p>

<h2><b>TEMA 2: </b>CRUD con Room y SQLite</h2>
Para esta practica, realizamos un CRUD con libros, así que el primer paso es modelarlo:

```bash
<!--Book.kt-->
package com.example.bd_app.model
imports ...

@Entity
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val price: Int,
    val genre: String,
    val author: String
): Serializable
```

Seguido, creamos la base de datos y su respectivo <b>Dao (Data Accees Object)</b>:

```bash
<!--BookDB.kt-->
package com.example.bd_app.data
imports ....

@Database(entities = [Book::class], version = 1)
abstract class BookDB : RoomDatabase() {
    abstract fun bookDao(): BookDao
    companion object{
        fun getDatabase(context: Context): BookDB {
            return Room.databaseBuilder(
                context.applicationContext,
                BookDB::class.java,
                NAME_BD
            ).build()
        }
    }
}
```

```bash
<!--BookDao.kt-->
package com.example.bd_app.data
imports ...

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBook(book: Book)

    @Query("SELECT * FROM Book")
    suspend fun getListBook(): MutableList<Book>

    @Delete
    suspend fun deleteBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)
}
```

Ahora creamos el Repositorio, que se encarga de conectar la base de datos con los view model:
```bash
<!--BookRepostory.kt-->
package com.example.bd_app.repository

imports...

class BookRepository(val context: Context) {
    private var bookDao:BookDao = BookDB.getDatabase(context).bookDao()
    suspend fun saveBook(book: Book){
        withContext(Dispatchers.IO){
            bookDao.saveBook(book)
        }
    }

    suspend fun getListBook():MutableList<Book>{
        return withContext(Dispatchers.IO){
            bookDao.getListBook()
        }
    }

    suspend fun deleteBook(book: Book){
        withContext(Dispatchers.IO){
            bookDao.deleteBook(book)
        }
    }

    suspend fun updateBook(book: Book){
        withContext(Dispatchers.IO){
            bookDao.updateBook(book)
        }
    }
}
```

Siguiendo el flujo, corresponde crear el ViewModel:
```bash
<!--BookViewModel.kt-->
package com.example.bd_app.viewmodel

imports...

class BookViewModel(application: Application): AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val bookRepository = BookRepository(context)

    private val _listBook = MutableLiveData<MutableList<Book>>()
    val listBook: LiveData<MutableList<Book>> get() = _listBook

    private val _progressState = MutableLiveData(false)
    val progressState: LiveData<Boolean> = _progressState

    fun saveBook(book: Book) {
        viewModelScope.launch {

            _progressState.value = true
            try {
                bookRepository.saveBook(book)
                _progressState.value = false
            } catch (e: Exception) {
                _progressState.value = false
            }
        }
    }

    fun getListBook() {
        viewModelScope.launch {
            _progressState.value = true
            try {
                _listBook.value = bookRepository.getListBook()
                _progressState.value = false
            } catch (e: Exception) {
                _progressState.value = false
            }
        }
    }

    fun deleteBook(book: Book){
        viewModelScope.launch {
            _progressState.value = true
            try {
                bookRepository.deleteBook(book)
                _progressState.value = false
            } catch (e: Exception) {
                _progressState.value = false
            }
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            _progressState.value = true
            try {
                bookRepository.updateBook(book)
                _progressState.value = false
            } catch (e: Exception) {
                _progressState.value = false
            }
        }
    }
}
```

EL ultimo paso es configurar las vistas, así que debemos crear el adapter y el viewHolder para la estructura del libro:
```bash
<!--BookAdapter.kt-->
package com.example.bd_app.view.adapter

imports...

class BookAdapter(private val listBook:MutableList<Book>,
                  private val navController: NavController): RecyclerView.Adapter<BookViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return BookViewHolder(binding, navController)
    }

    override fun getItemCount(): Int {
        return listBook.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = listBook[position]
        holder.setItemBook(book)
    }
}
```
```bash
<!--BookViewHolder.kt-->
package com.example.bd_app.view.viewholder

imports...

class BookViewHolder(binding: ItemBookBinding, navController: NavController) :
    RecyclerView.ViewHolder(binding.root){
    val bindingItem = binding
    val navController = navController
    fun setItemBook(book: Book) {
        bindingItem.textViewName.text = "Título: ${book.name}"
        bindingItem.textViewPrice.text = "Precio: $${book.price}"
        bindingItem.textViewGenre.text = "Genero: ${book.genre}"
        bindingItem.textViewAuthor.text = "Autor ${book.author}"

        bindingItem.cardViewBook.setOnClickListener{
            val bundle = Bundle()
            bundle.putSerializable("clave", book)
            navController.navigate(R.id.action_homeBookFragment_to_bookDetailsFragment, bundle)
        }
    }
}
```
Ahora, se configura la lógica en cada vista, para efectuar las distintas acciones del CRUD.

Se plantea el siguiente fragment como el de inicio, donde se muestran todos los libros guardados:
```bash
<!--HomeBookFragment.kt-->
package com.example.bd_app.view.fragment

imports ...

class HomeBookFragment : Fragment() {
    private lateinit var binding: FragmentHomeBookBinding
    private val bookViewModel: BookViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBookBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ...
        observerViewModel()

    }

    ...

    private fun observerViewModel() {
        observerListBook()
        observerProgress()
    }

    private fun observerListBook(){
        bookViewModel.getListBook()
        bookViewModel.listBook.observe(viewLifecycleOwner){listBook ->
            val recycler = binding.recyclerView
            val layoutManager = LinearLayoutManager(context)
            recycler.layoutManager = layoutManager
            val adapter = BookAdapter(listBook, findNavController())
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun observerProgress() {
        bookViewModel.progressState.observe(viewLifecycleOwner){status ->
            binding.progress.isVisible = status
        }
    }
}
```

Para ver los detalles de un libro especifico, y poder eliminarlo:
```bash
<!--BookDeatilsFragment.kt-->
package com.example.bd_app.view.fragment

imports...

class BookDetailsFragment : Fragment() {
    private lateinit var binding: FragmentBookDetailsBinding
    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var receivedBook: Book

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBook()
        controlator()
    }

    private fun controlator() {
        binding.buttonDelete.setOnClickListener{
            deleteBook()
        }

        binding.buttonEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("dataBook", receivedBook)
            findNavController().navigate(R.id.action_bookDetailsFragment_to_bookEditFRagment, bundle)
        }
    }

    private fun dataBook() {
        val receivedBundle = arguments
        receivedBook = receivedBundle?.getSerializable("clave") as Book
        binding.textViewBookNamePH.text = "${receivedBook.name}"
        binding.textViewBookPricePH.text = "$ ${receivedBook.price}"
        binding.textViewBookGenrePH.text = "${receivedBook.genre}"
        binding.textViewBookAuthorPH.text = "${receivedBook.author}"
    }

    private fun deleteBook(){
        bookViewModel.deleteBook(receivedBook)
        bookViewModel.getListBook()
        findNavController().popBackStack()
    }
}
```

Para ver añadir un nuevo libro:
```bash
<!--AddBookFragment.kt-->
package com.example.bd_app.view.fragment

import ....

class AddBookFragment : Fragment() {
    private lateinit var binding: FragmentAddBookBinding
    private val bookViewModel: BookViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBookBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controlator()
    }

    private fun controlator() {
        validateData()
        binding.buttonSaveBook.setOnClickListener {
            saveBook()
        }
    }

    private fun saveBook(){
        val name = binding.editTextBookName.text.toString()
        val price = binding.editTextBookPrice.text.toString().toInt()
        val genre = binding.editTextBookGenre.text.toString()
        val author = binding.editTextBookAuthor.text.toString()
        val book = Book(name = name, price = price, genre = genre, author = author)
        bookViewModel.saveBook(book)
        Log.d("test", book.toString())
        Toast.makeText(context, "Libro guardado", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    private fun validateData() {
        val listEditText = listOf(binding.editTextBookName, binding.editTextBookPrice,
        binding.editTextBookGenre, binding.editTextBookAuthor)

        for (editText in listEditText) {
            editText.addTextChangedListener {
                val isListFull = listEditText.all{
                    it.text?.isNotEmpty() == true// si toda la lista no está vacía
                }
                binding.buttonSaveBook.isEnabled = isListFull
            }
        }
    }
}
```

Y para editar un libro:
```bash
<!--BookEditFragment.kt-->
package com.example.bd_app.view.fragment

imports...

class BookEditFragment : Fragment() {
    private lateinit var binding: FragmentBookEditBinding
    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var receivedBook: Book

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookEditBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBook()
        controlator()
    }

    private fun controlator() {
        binding.buttonEdit.setOnClickListener{
            updateBook()
        }
    }

    private fun dataBook() {
        val receivedBundle = arguments
        receivedBook = receivedBundle?.getSerializable("dataBook") as Book
        binding.editTextBookName.setText(receivedBook.name)
        binding.editTextBookPrice.setText(receivedBook.price.toString())
        binding.editTextBookGenre.setText(receivedBook.genre)
        binding.editTextBookAuthor.setText(receivedBook.author)
    }

    private fun updateBook() {
        val name = binding.editTextBookName.text.toString()
        val price = binding.editTextBookPrice.text.toString().toInt()
        val genre = binding.editTextBookGenre.text.toString()
        val author = binding.editTextBookAuthor.text.toString()
        val book = Book(id = receivedBook.id, name=name, price=price, genre = genre, author = author)
        bookViewModel.updateBook(book)
        findNavController().navigate(R.id.action_bookEditFragment_to_homeBookFragment)
    }
    
}
```

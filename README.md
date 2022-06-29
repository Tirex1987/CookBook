# Кулинарная книга (CookBook)
Разработка приложения в рамках дипломного проекта курса «Android-разработчик» образовательной онлайн-платформы Нетология.

Приложение позволяет создавать, просматривать и редактировать собственные рецепты.

## Особенности приложения:
* Запрос разрешений доступа к памяти устройства для возможности загрузки изображений:

  ![](img/Screenshot_dialog_for_permissions.jpg)  ![](img/Screenshot_order_permissions.jpg)
* Создание рецепта с возможностью выбора изображения готового блюда:

  ![](img/Screenshot_recipe_create.jpg)   ![](img/Screenshot_create_recipe_message.jpg)
* Добавление этапов приготовления (шагов) с возможностью загрузки изображений отдельно на каждом шаге:

  ![](img/Screenshot_create_step.jpg)
* Редактирование / удаление этапов приготовления:

  ![](img/Screenshot_steps_edit_remove.jpg)
* Просмотр списка всех рецептов с возможностью фильтрации по категориям, поиск по названию рецепта:

  ![](img/Screenshot_recipes.jpg)   ![](img/Screenshot_filters.jpg)   ![](img/Screenshot_search.jpg)
* Добавление / удаление рецептов в Избранное:

  ![](img/Screenshot_favorites.jpg)
* Режим просмотра рецепта:

  ![](img/Screenshot_recipe_preview.jpg)    ![](img/Screenshot_steps_preview.jpg)
* Редактирование / удаление рецептов:

  ![](img/Screenshot_recipes_edit_remove.jpg)
* Картинка-заглушка для пустого экрана в случае отсутствия рецептов:

  ![](img/Screenshot_blank_page.jpg)
* Реализация drag & drop

## Особенности реализации:
* Адаптеры RecipesAdapter и StepsAdapter для вывода в RecyclerView соответственно списка рецептов и шагов (пакет *adapter*)

* Хранение рецептов и шагов приготовления в разных таблицах БД

* Для более удобной работы с фильтрами создан класс *EnabledCategories* (пакет *data*), который хранит текущее состояние фильтра по каждой категории. Между перезапусками приложения отмеченные фильтры хранятся в shared preferences

* Вспомогательный класс для редактирования рецептов (*data/RecipeEditor*)

* Отдельный класс для запроса разрешений доступа к памяти устройства (*util/OrderPermission*)

* Отдельный класс для запуска Intent выбора изображений (*util/OpenImageIntent*)

* Функция расширения *loadBitmapFromPath* для загрузки масштабированного изображения в ImageView (*util/loadScaledBitmap.kt*)

Дополнительно подключаемые библиотеки: *livedata*, *fragment*, *room*.

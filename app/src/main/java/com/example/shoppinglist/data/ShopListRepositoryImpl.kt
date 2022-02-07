package com.example.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import kotlin.random.Random

class ShopListRepositoryImpl(application: Application) : ShopListRepository {

    private val shopListDAO = AppDatabase.getInstance(application).shopListDAO()
    private val mapper = ShopListMapper()

    override suspend fun addShopItem(shopItem: ShopItem) {
        shopListDAO.addShopItem(mapper.mapEntityToDBModel(shopItem))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListDAO.deleteShopItem(shopItem.id)
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        shopListDAO.addShopItem(mapper.mapEntityToDBModel(shopItem))
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return Transformations.map(shopListDAO.getShopList()) {
            mapper.mapListDBModelToListEntity(it)
        }
    }

//    override fun getShopList(): LiveData<List<ShopItem>> {
//        return MediatorLiveData<List<ShopItem>>().apply {
//            addSource(shopListDAO.getShopList()) {
//                value = mapper.mapListDBModelToListEntity(it)
//            }
//        }
//    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem {
        val dbModel = shopListDAO.getShopItem(shopItemId)
        return mapper.mapDBModelToEntity(dbModel)
    }
}
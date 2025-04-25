import { defineStore } from 'pinia'
import { ref } from 'vue'

interface Item {
  quantity: number
  unit: string
  expirationDate: Date
}

interface itemTypeState {
  id: number | null
  isEditMode: boolean
  name: string
  items: Item[]
}

export const useItemTypeStore = defineStore('itemType', () => {
  const id = ref<number | null>(null)
  const name = ref<string>('')
  const items = ref<Item[]>([])
  const isEditMode = ref(false)

  const setItemType = (newId: number | null, newName: string, newItems: Item[]) => {
    id.value = newId
    name.value = newName
    items.value = newItems.map(item => ({
      quantity: item.quantity,
      unit: item.unit,
      expirationDate: item.expirationDate
    }))
  }

  const toggleEditMode = () => {
    isEditMode.value = !isEditMode.value
  }

  return {
    id,
    name,
    items,
    isEditMode,
    setItemType,
    toggleEditMode
  }
}, {
  persist: true
})

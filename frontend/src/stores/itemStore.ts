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

  const setItemType = (newId: number | null, newName: string) => {
    id.value = newId
    name.value = newName
  }

  const toggleEditMode = () => {
    isEditMode.value = !isEditMode.value
  }

  return {
    id,
    name,
    isEditMode,
    setItemType,
    toggleEditMode
  }
}, {
  persist: true
})

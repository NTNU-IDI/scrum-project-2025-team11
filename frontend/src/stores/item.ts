import { defineStore } from 'pinia'
import { ref } from 'vue'

interface itemTypeState {
  id: number | null
}

export const useItemTypeStore = defineStore('itemType', () => {
  const id = ref<number | null>(null)
  const isEditMode = ref(false)

  const setItemType = (newId: number | null) => {
    id.value = newId
  }

  const toggleEditMode = () => {
    isEditMode.value = !isEditMode.value
  }

  return {
    id,
    isEditMode,
    setItemType,
    toggleEditMode
  }
}, {
  persist: true
})

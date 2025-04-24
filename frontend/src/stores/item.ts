import { defineStore } from 'pinia'
import { ref } from 'vue'

interface itemTypeState {
  id: number | null
}

export const useItemTypeStore = defineStore('itemType', () => {
  const id = ref<number | null>(null)

  const setItemType = (newId: number | null) => {
    id.value = newId
  }

  return {
    id,
    setItemType
  }
}, {
  persist: true
})

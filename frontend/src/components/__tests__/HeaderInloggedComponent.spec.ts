import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import HeaderComponent from '@/components/HeaderInloggedComponent.vue'
import { createRouter, createWebHistory } from 'vue-router'
import { logOutUser } from '@/api/AuthService'

// Mock logOutUser, please dont touch >:(
vi.mock('@/api/AuthService', () => ({
  logOutUser: vi.fn()
}))

const router = createRouter({
  history: createWebHistory(),
  routes: []
})

beforeEach(async () => {
  if (router.currentRoute.value.fullPath !== '/') {
    await router.push('/')
    await router.isReady()
  }
})

describe('HeaderComponent', () => {
  it('wheter logos and links are showing', () => {
    const wrapper = mount(HeaderComponent, {
      global: {
        plugins: [router]
      }
    })

    expect(wrapper.find('img.logo').exists()).toBe(true)
    expect(wrapper.find('a[href="/"]').exists()).toBe(true)
    expect(wrapper.find('a[href="/map"]').exists()).toBe(true)
    expect(wrapper.find('a[href="/storage"]').exists()).toBe(true)
  })

  it('whether it navigates to the front page when the logo is clicked', async () => {
    const pushSpy = vi.spyOn(router, 'push')
    
    const wrapper = mount(HeaderComponent, {
      global: {
        plugins: [router]
      }
    })

    await wrapper.find('img.logo').trigger('click')

    expect(pushSpy).toHaveBeenCalledWith('/')
  })

  it('whether logOutUser is called when the logout button is clicked', async () => {
    const wrapper = mount(HeaderComponent, {
      global: {
        plugins: [router]
      }
    })

    await wrapper.find('a.logout').trigger('click')

    expect(logOutUser).toHaveBeenCalled()
  })

  it('whether "Min beredskap" has the right link', () => {
    const wrapper = mount(HeaderComponent, {
      global: {
        plugins: [router]
      }
    })

    const link = wrapper.find('a[href="/storage"]')
    expect(link.exists()).toBe(true)
    expect(link.text()).toContain('Min beredskap')
  })
})

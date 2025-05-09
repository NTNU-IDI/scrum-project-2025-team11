import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import HeaderComponent from '@/components/HeaderSuperAdminComponent.vue'
import { createRouter, createWebHistory } from 'vue-router'
import { logOutUser } from '@/api/AuthService'

vi.mock('@/api/AuthService', () => ({
  logOutUser: vi.fn()
}))

const router = createRouter({
  history: createWebHistory(),
  routes: [{ path: '/', component: { template: '<div>Home</div>' } }]
})

beforeEach(async () => {
  await router.push('/')
  await router.isReady()
})

describe('HeaderBaseComponent', () => {
  it('renders logo and logout button', () => {
    const wrapper = mount(HeaderComponent, {
      global: {
        plugins: [router]
      }
    })

    expect(wrapper.find('img.logo').exists()).toBe(true)
    expect(wrapper.find('a.logout').exists()).toBe(true)
    expect(wrapper.find('a.logout').text()).toContain('Logg ut')
  })

  it('navigates to home when logo is clicked', async () => {
    const pushSpy = vi.spyOn(router, 'push')

    const wrapper = mount(HeaderComponent, {
      global: {
        plugins: [router]
      }
    })

    await wrapper.find('img.logo').trigger('click')
    expect(pushSpy).toHaveBeenCalledWith('/')
  })

  it('calls logOutUser when logout is clicked', async () => {
    const wrapper = mount(HeaderComponent, {
      global: {
        plugins: [router]
      }
    })

    await wrapper.find('a.logout').trigger('click')
    expect(logOutUser).toHaveBeenCalled()
  })
})

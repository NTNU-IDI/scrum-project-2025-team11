import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import HeaderComponent from '@/components/Header.vue'

const push = vi.fn()

vi.mock('vue-router', () => ({
  useRouter: () => ({
    push
  })
}))

describe('HeaderComponent', () => {
  it('whether the logo and navigation buttons are showing', () => {
    const wrapper = mount(HeaderComponent)

    expect(wrapper.find('img.logo').exists()).toBe(true)
    expect(wrapper.text()).toContain('Hjem')
    expect(wrapper.text()).toContain('Kart')
    expect(wrapper.text()).toContain('Logg inn')
    expect(wrapper.text()).toContain('Registrer deg')
  })

  it(' whether the logo navigates to the front page', async () => {
    const wrapper = mount(HeaderComponent)

    await wrapper.find('img.logo').trigger('click')

    expect(push).toHaveBeenCalledWith('/')
  })
})

declare module "vue-recaptcha-v3" {
    import { Plugin } from "vue";
  
    export interface IReCaptchaOptions {
      siteKey: string;
      loaderOptions?: {
        autoHideBadge?: boolean;
        explicitRenderParameters?: {
          badge?: "bottomright" | "bottomleft" | "inline";
          theme?: "light" | "dark";
          tabindex?: number;
        };
      };
    }
  
    export const VueReCaptcha: Plugin<[IReCaptchaOptions]>;
    export function useReCaptcha(): {
      executeRecaptcha: (action: string) => Promise<string>;
    };
  }
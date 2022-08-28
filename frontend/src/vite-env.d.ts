/// <reference types="vite/client" />

interface ImportMetaEnv {
    readonly VITE_MNP_API_URL: string
  }
  
  interface ImportMeta {
    readonly env: ImportMetaEnv
  }

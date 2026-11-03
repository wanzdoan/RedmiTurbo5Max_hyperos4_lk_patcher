
void FUN_ffff000090741554(void)

{
  int iVar1;
  
  iVar1 = FUN_ffff000090737324(&PTR_LAB_ffff00009081a1d0,0,&PTR_s_mt6379_ffff00009081a1f0);
  if (iVar1 < 0) {
    FUN_ffff00009077e11c
              (s__s__d__Failed_to_init_MT6379_reg_ffff0000907f16a7,s_mt6379_init_ffff0000907efd3a,
               0x7e,iVar1);
  }
  FUN_ffff00009077e11c(s__s__d__LK_init_done__ffff0000907f3de7,s_mt6379_init_ffff0000907efd3a,0x8f);
  return;
}



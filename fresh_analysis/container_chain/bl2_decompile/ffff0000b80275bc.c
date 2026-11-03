
void FUN_ffff0000b80275bc(void)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000b8020fdc(&PTR_LAB_ffff0000b80ea3b8,0,&PTR_s_mt6379_ffff0000b80ea3d8);
  if (iVar1 < 0) {
    FUN_ffff0000b804c33c
              (s__s__d__Failed_to_init_MT6379_reg_ffff0000b809817a,s_mt6379_init_ffff0000b809731a,
               0x7e,iVar1);
  }
  FUN_ffff0000b804c33c(s__s__d__LK_init_done__ffff0000b80997d2,s_mt6379_init_ffff0000b809731a,0x8f);
  return;
}



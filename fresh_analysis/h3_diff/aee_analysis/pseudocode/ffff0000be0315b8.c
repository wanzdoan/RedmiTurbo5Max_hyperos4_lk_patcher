
void FUN_ffff0000be0315b8(void)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000be029728(&PTR_LAB_ffff0000be0d7508,0,&PTR_s_mt6379_ffff0000be0d7528);
  if (iVar1 < 0) {
    FUN_ffff0000be05fd98
              (s__s__d__Failed_to_init_MT6379_reg_ffff0000be0bafee,s_mt6379_init_ffff0000be0b9e09,
               0x7e,iVar1);
  }
  FUN_ffff0000be05fd98(s__s__d__LK_init_done__ffff0000be0bcb75,s_mt6379_init_ffff0000be0b9e09,0x8f);
  return;
}



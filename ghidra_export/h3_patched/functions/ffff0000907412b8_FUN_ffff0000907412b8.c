// ffff0000907412b8  size=4  FUN_ffff0000907412b8

void FUN_ffff0000907412b8(void)

{
  int iVar1;
  
  iVar1 = FUN_ffff00009073708c(&PTR_FUN_ffff00009081a1b0,0,&PTR_s_mt6379_ffff00009081a1d0);
  if (iVar1 < 0) {
    FUN_ffff00009077de54
              (s__s__d__Failed_to_init_MT6379_reg_ffff0000907f151f,s_mt6379_init_ffff0000907efbb2,
               0x7e,iVar1);
  }
  FUN_ffff00009077de54(s__s__d__LK_init_done__ffff0000907f3c44,s_mt6379_init_ffff0000907efbb2,0x8f);
  return;
}



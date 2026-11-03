// ffff00009076a7f4  size=256  FUN_ffff00009076a7f4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009076a7f4(void)

{
  undefined4 uVar1;
  long lVar2;
  
  FUN_ffff00009077e11c(s__LK_LCM__s____ffff0000907eb7af,s_lcm_suspend_power_ffff0000907d50dd);
  FUN_ffff000090785730(0x5a,0);
  FUN_ffff00009078561c(0x5a,1);
  FUN_ffff00009078568c(0x5a,0);
  (*_DAT_ffff00009087cd30)(1);
  FUN_ffff00009076be0c(0);
  (*_DAT_ffff00009087cd30)(1);
  lVar2 = FUN_ffff000090740610(1,4);
  if (lVar2 == 0) {
    FUN_ffff00009077e11c
              (s__LK_LCM__LCM__ERROR_get_mt6363_d_ffff0000907e9eab,
               s_mt6363_ldo_1p2_enable_ffff0000907d00a8);
  }
  else {
    FUN_ffff00009074056c(lVar2,0x1d07,0,1,0);
    uVar1 = FUN_ffff00009074056c(lVar2,0x149f,2,2,0);
    FUN_ffff00009077e11c
              (s__LK_LCM__LCM__s_ret__d_en__d_ffff0000907e1e73,
               s_mt6363_ldo_1p2_enable_ffff0000907d00a8,uVar1,0);
  }
  (*_DAT_ffff00009087cd30)(1);
  FUN_ffff00009076bcd4(0);
  return;
}



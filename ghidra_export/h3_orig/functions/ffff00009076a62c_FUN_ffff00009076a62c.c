// ffff00009076a62c  size=256  FUN_ffff00009076a62c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009076a62c(void)

{
  undefined4 uVar1;
  long lVar2;
  
  FUN_ffff00009077de54(s__LK_LCM__s____ffff0000907eb62c,s_lcm_suspend_power_ffff0000907d50a0);
  FUN_ffff000090785468(0x5a,0);
  FUN_ffff000090785354(0x5a,1);
  FUN_ffff0000907853c4(0x5a,0);
  (*_DAT_ffff00009087cd90)(1);
  FUN_ffff00009076bc08(0);
  (*_DAT_ffff00009087cd90)(1);
  lVar2 = FUN_ffff000090740378(1,4);
  if (lVar2 == 0) {
    FUN_ffff00009077de54
              (s__LK_LCM__LCM__ERROR_get_mt6363_d_ffff0000907e9d67,
               s_mt6363_ldo_1p2_enable_ffff0000907d0089);
  }
  else {
    FUN_ffff0000907402d4(lVar2,0x1d07,0,1,0);
    uVar1 = FUN_ffff0000907402d4(lVar2,0x149f,2,2,0);
    FUN_ffff00009077de54
              (s__LK_LCM__LCM__s_ret__d_en__d_ffff0000907e1da2,
               s_mt6363_ldo_1p2_enable_ffff0000907d0089,uVar1,0);
  }
  (*_DAT_ffff00009087cd90)(1);
  FUN_ffff00009076baa8(0);
  return;
}



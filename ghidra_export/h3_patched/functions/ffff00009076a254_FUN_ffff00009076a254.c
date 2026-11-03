// ffff00009076a254  size=364  FUN_ffff00009076a254

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009076a254(void)

{
  int iVar1;
  undefined4 uVar2;
  long lVar3;
  
  iVar1 = FUN_ffff00009075c0e4();
  FUN_ffff00009077de54
            (s__LK_LCM__s_lh__first_pre_init_po_ffff0000907eeaa6,
             s_lcm_pre_init_power_ffff0000907ffb1c,iVar1 == 0);
  if (iVar1 == 0) {
    FUN_ffff000090785468(0x5a,0);
    FUN_ffff000090785354(0x5a,1);
    FUN_ffff0000907853c4(0x5a,0);
    (*_DAT_ffff00009087cc98)(10);
    FUN_ffff00009076ba0c(1);
    (*_DAT_ffff00009087cc98)(5);
    lVar3 = FUN_ffff000090740378(1,4);
    if (lVar3 == 0) {
      FUN_ffff00009077de54
                (s__LK_LCM__LCM__ERROR_get_mt6363_d_ffff0000907e9d50,
                 s_mt6363_ldo_1p2_enable_ffff0000907d0089);
    }
    else {
      FUN_ffff0000907402d4(lVar3,0x149e,2,2,0);
      FUN_ffff0000907402d4(lVar3,0x1b3f,3,0xf,0);
      FUN_ffff0000907402d4(lVar3,0x1f88,5,0xf,0);
      uVar2 = FUN_ffff0000907402d4(lVar3,0x1d07,1,1,0);
      FUN_ffff00009077de54
                (s__LK_LCM__LCM__s_ret__d_en__d_ffff0000907e1d8b,
                 s_mt6363_ldo_1p2_enable_ffff0000907d0089,uVar2,1);
    }
    (*_DAT_ffff00009087cc98)(5);
    FUN_ffff00009076bb44(1);
    (*_DAT_ffff00009087cc98)(10);
    FUN_ffff00009075c0f8(1);
  }
  FUN_ffff00009077de54(s__LK_LCM__s_lh_____ffff0000907fc1dc,s_lcm_pre_init_power_ffff0000907ffb1c);
  return;
}



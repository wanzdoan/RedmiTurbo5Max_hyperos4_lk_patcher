// ffff00009076a688  size=364  FUN_ffff00009076a688

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009076a688(void)

{
  int iVar1;
  undefined4 uVar2;
  long lVar3;
  
  iVar1 = FUN_ffff00009075c37c();
  FUN_ffff00009077e11c
            (s__LK_LCM__s_lh__first_pre_init_po_ffff0000907eec2e,
             s_lcm_pre_init_power_ffff0000907ffcc4,iVar1 == 0);
  if (iVar1 == 0) {
    FUN_ffff000090785730(0x5a,0);
    FUN_ffff00009078561c(0x5a,1);
    FUN_ffff00009078568c(0x5a,0);
    (*_DAT_ffff00009087ce28)(10);
    FUN_ffff00009076bd70(1);
    (*_DAT_ffff00009087ce28)(5);
    lVar3 = FUN_ffff000090740610(1,4);
    if (lVar3 == 0) {
      FUN_ffff00009077e11c
                (s__LK_LCM__LCM__ERROR_get_mt6363_d_ffff0000907e9eab,
                 s_mt6363_ldo_1p2_enable_ffff0000907d00a8);
    }
    else {
      FUN_ffff00009074056c(lVar3,0x149e,2,2,0);
      FUN_ffff00009074056c(lVar3,0x1b3f,3,0xf,0);
      FUN_ffff00009074056c(lVar3,0x1f88,5,0xf,0);
      uVar2 = FUN_ffff00009074056c(lVar3,0x1d07,1,1,0);
      FUN_ffff00009077e11c
                (s__LK_LCM__LCM__s_ret__d_en__d_ffff0000907e1e73,
                 s_mt6363_ldo_1p2_enable_ffff0000907d00a8,uVar2,1);
    }
    (*_DAT_ffff00009087ce28)(5);
    FUN_ffff00009076bed0(1);
    (*_DAT_ffff00009087ce28)(10);
    FUN_ffff00009075c390(1);
  }
  FUN_ffff00009077e11c(s__LK_LCM__s_lh_____ffff0000907fc378,s_lcm_pre_init_power_ffff0000907ffcc4);
  return;
}



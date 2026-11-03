
void FUN_ffff0000be0527f0(void)

{
  int iVar1;
  undefined4 uVar2;
  long lVar3;
  
  iVar1 = FUN_ffff0000be04757c();
  FUN_ffff0000be05fd98
            (s__LK_LCM__s_lh__first_pre_init_po_ffff0000be0b9199,
             s_lcm_pre_init_power_ffff0000be0c47eb,iVar1 == 0);
  if (iVar1 == 0) {
    FUN_ffff0000be065ed8(0x5a,0);
    FUN_ffff0000be065dc4(0x5a,1);
    FUN_ffff0000be065e34(0x5a,0);
    (*DAT_ffff0000be126f50)(10);
    FUN_ffff0000be053fa8(1);
    (*DAT_ffff0000be126f50)(5);
    lVar3 = FUN_ffff0000be0303e8(1,4);
    if (lVar3 == 0) {
      FUN_ffff0000be05fd98
                (s__LK_LCM__LCM__ERROR_get_mt6363_d_ffff0000be0b5a90,
                 s_mt6363_ldo_1p2_enable_ffff0000be0a3b80);
    }
    else {
      FUN_ffff0000be030344(lVar3,0x149e,2,2,0);
      FUN_ffff0000be030344(lVar3,0x1b3f,3,0xf,0);
      FUN_ffff0000be030344(lVar3,0x1f88,5,0xf,0);
      uVar2 = FUN_ffff0000be030344(lVar3,0x1d07,1,1,0);
      FUN_ffff0000be05fd98
                (s__LK_LCM__LCM__s_ret__d_en__d_ffff0000be0b03c1,
                 s_mt6363_ldo_1p2_enable_ffff0000be0a3b80,uVar2,1);
    }
    (*DAT_ffff0000be126f50)(5);
    FUN_ffff0000be0540e0(1);
    (*DAT_ffff0000be126f50)(10);
    FUN_ffff0000be047590(1);
  }
  FUN_ffff0000be05fd98(s__LK_LCM__s_lh_____ffff0000be0c229f,s_lcm_pre_init_power_ffff0000be0c47eb);
  return;
}




/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000b8016e2c(void)

{
  uint uVar1;
  int iVar2;
  long *plVar3;
  ulong uVar4;
  undefined8 *puVar5;
  long lVar6;
  undefined8 uVar7;
  
  uVar1 = FUN_ffff0000b8017ddc();
  DAT_ffff0000b80e8a68 = DAT_ffff0000b80e8a68 & 0xfffffffe | uVar1 & 1;
  iVar2 = thunk_FUN_ffff0000b8067918();
  DAT_ffff0000b80e8a68 =
       DAT_ffff0000b80e8a68 & 0xffffff80 | DAT_ffff0000b80e8a68 & 0x3f | (uint)(iVar2 != 0) << 6;
  if ((iVar2 != 0) && (iVar2 = thunk_FUN_ffff0000b806ba1c(&DAT_ffff0000b80e8aec,0x20), iVar2 != 0))
  {
    FUN_ffff0000b804c33c
              (s__GZ_INIT__CONFIG___s__failed_to_g_ffff0000b80a0f1e,
               s_gz_config_platform_update_ffff0000b808fa36,iVar2);
  }
  iVar2 = FUN_ffff0000b804f718();
  uVar1 = DAT_ffff0000b80e8a68 & 0xffff8000 |
          DAT_ffff0000b80e8a68 & 0x3fff | (uint)(iVar2 != 0) << 0xe;
  if ((DAT_ffff0000b80e8a68 & 1) == 0) {
    DAT_ffff0000b80e8a70 = 0;
    DAT_ffff0000b80e8a68 = uVar1;
  }
  else {
    DAT_ffff0000b80e8a68 = uVar1;
    DAT_ffff0000b80e8a70 = FUN_ffff0000b8017b9c(1);
  }
  plVar3 = (long *)FUN_ffff0000b80176a0(0);
  if (plVar3 == (long *)0x0) {
    DAT_ffff0000b80e8a70 = 0x4e6f475a;
    FUN_ffff0000b804c33c
              (s__GZ_INIT__CONFIG___s__failed_to_g_ffff0000b8096195,
               s_gz_config_platform_update_ffff0000b808fa36);
    uVar7 = 0xfffffffb;
  }
  else {
    DAT_ffff0000b80e8a7c = 0;
    DAT_ffff0000b80e8a84 = 0;
    DAT_ffff0000b80e8a78 = (undefined4)plVar3[1];
    DAT_ffff0000b80e8a70 = *plVar3 + DAT_ffff0000b80e8a70 + -0x38000000;
    DAT_ffff0000b80e8a8c = 0;
    if ((DAT_ffff0000b80e8a68 & 1) != 0) {
      DAT_ffff0000b80e8a7c = FUN_ffff0000b8017b9c(1);
      DAT_ffff0000b80e8a84 = FUN_ffff0000b8017bdc(1);
      DAT_ffff0000b80e8a8c = FUN_ffff0000b8017c1c(1);
    }
    iVar2 = FUN_ffff0000b8003b0c();
    DAT_ffff0000b80e8a94 = 0x16000000;
    if (iVar2 != 0) {
      DAT_ffff0000b80e8a94 = 0x16010000;
    }
    DAT_ffff0000b80e8ad0 = FUN_ffff0000b80504cc();
    DAT_ffff0000b80e8ad2 = FUN_ffff0000b80504e4();
    uVar1 = FUN_ffff0000b80504b0();
    DAT_ffff0000b80e8a68 =
         DAT_ffff0000b80e8a68 & 0xfffe0000 | DAT_ffff0000b80e8a68 & 0x7fff | (uVar1 & 3) << 0xf;
    uVar4 = FUN_ffff0000b800dbfc();
    DAT_ffff0000b80e8ae0 = (undefined4)(uVar4 >> 0x14);
    DAT_ffff0000b80e8ae4 = thunk_FUN_ffff0000b802ce0c(0x1e);
    puVar5 = (undefined8 *)FUN_ffff0000b80176a0(3);
    _DAT_ffff0000b80e8ad8 = 0;
    DAT_ffff0000b80e8b18 = 0;
    if (puVar5 == (undefined8 *)0x0) {
      FUN_ffff0000b804c33c
                (s__GZ_INIT__CONFIG___s__failed_to_g_ffff0000b80961ce,
                 s_gz_config_platform_update_ffff0000b808fa36);
    }
    else {
      _DAT_ffff0000b80e8ad8 = CONCAT44((int)puVar5[1],(int)*puVar5);
      DAT_ffff0000b80e8b18 = (undefined4)((ulong)*puVar5 >> 0x20);
    }
    lVar6 = FUN_ffff0000b80143f4(s_hyp_cmdline_ffff0000b808dcf3);
    FUN_ffff0000b804d070(&DAT_ffff0000b80e8b1c,0,0x10);
    if (lVar6 != 0) {
      uVar4 = FUN_ffff0000b804d3cc(lVar6);
      if (0x10 < uVar4) {
        FUN_ffff0000b804c33c
                  (s__GZ_INIT__CONFIG___s__hyp_cmdlin_ffff0000b809f50c,
                   s_gz_config_platform_update_ffff0000b808fa36);
      }
      uVar4 = FUN_ffff0000b804d3cc(lVar6);
      if (0xf < uVar4) {
        uVar4 = 0x10;
      }
      FUN_ffff0000b804cd40(&DAT_ffff0000b80e8b1c,lVar6,uVar4);
    }
    uVar4 = FUN_ffff0000b800dccc();
    uVar7 = 0;
    DAT_ffff0000b80e8b2c = (undefined4)(uVar4 >> 0x14);
  }
  return uVar7;
}



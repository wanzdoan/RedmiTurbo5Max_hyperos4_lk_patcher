
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000b8016d20(void)

{
  int iVar1;
  ulong uVar2;
  
  iVar1 = FUN_ffff0000b8052b84(&DAT_ffff0000b80e8a20,0x10);
  if (iVar1 != 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b80e8a20,0,0x10);
    FUN_ffff0000b804c33c
              (s__GZ_INIT__CONFIG___s__failed_to_g_ffff0000b8094323,
               s_gz_config_param_update_ffff0000b8095327);
  }
  DAT_ffff0000b80e8a30 = 0;
  DAT_ffff0000b80e8a38 = 0;
  iVar1 = thunk_FUN_ffff0000b80729e0(0,&DAT_ffff0000b80e8a3c,0x20);
  if (iVar1 != 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b80e8a3c,0,0x20);
    FUN_ffff0000b804c33c
              (s__GZ_INIT__CONFIG___s__unable_to_f_ffff0000b809b642,
               s_gz_config_param_update_ffff0000b8095327);
  }
  iVar1 = FUN_ffff0000b801f73c();
  if (iVar1 == 1) {
    uVar2 = FUN_ffff0000b80522bc();
    uVar2 = uVar2 & 0xffffffff;
  }
  else {
    if (iVar1 != 2) goto LAB_ffff0000b8016e08;
    uVar2 = FUN_ffff0000b8059e98();
  }
  if (uVar2 >> 0x11 != 0) {
    _DAT_ffff0000b80e8a5c = CONCAT44(0x20000,(int)uVar2 + -0x20000);
    return 0;
  }
LAB_ffff0000b8016e08:
  _DAT_ffff0000b80e8a5c = 0;
  FUN_ffff0000b804c33c
            (s__GZ_INIT__CONFIG___s__insufficie_ffff0000b808c51f,
             s_gz_config_param_update_ffff0000b8095327);
  return 0;
}



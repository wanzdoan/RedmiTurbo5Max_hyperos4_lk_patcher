
undefined8 FUN_ffff0000b8017094(void)

{
  undefined8 *puVar1;
  
  DAT_ffff0000b80e8b68 = 0;
  DAT_ffff0000b80e8b70 = 0;
  DAT_ffff0000b80e8b78 = 0;
  DAT_ffff0000b80e8b84 = 0;
  DAT_ffff0000b80e8b8c = 0;
  DAT_ffff0000b80e8b90._0_4_ = 0;
  DAT_ffff0000b80e8b90._4_4_ = 0;
  DAT_ffff0000b80e8b98 = 0;
  DAT_ffff0000b80e8ba0 = 0;
  DAT_ffff0000b80e8ba8 = 0;
  DAT_ffff0000b80e8bb0 = 0;
  DAT_ffff0000b80e8bbc = 0;
  DAT_ffff0000b80e8bc4 = 0;
  DAT_ffff0000b80e8bcc = 0;
  puVar1 = (undefined8 *)FUN_ffff0000b80176a0(1);
  if (puVar1 == (undefined8 *)0x0) {
    FUN_ffff0000b804c33c
              (s__GZ_INIT__CONFIG___s__failed_to_g_ffff0000b808b690,
               s_gz_config_rkp_param_update_ffff0000b8099aa7);
  }
  else {
    DAT_ffff0000b80e8b84 = *puVar1;
    DAT_ffff0000b80e8b8c = (undefined4)puVar1[1];
  }
  puVar1 = (undefined8 *)FUN_ffff0000b80176a0(2);
  if (puVar1 == (undefined8 *)0x0) {
    FUN_ffff0000b804c33c
              (s__GZ_INIT__CONFIG___s__failed_to_g_ffff0000b8087f5e,
               s_gz_config_rkp_param_update_ffff0000b8099aa7);
  }
  else {
    DAT_ffff0000b80e8b90._0_4_ = (undefined4)*puVar1;
    DAT_ffff0000b80e8b90._4_4_ = (undefined4)((ulong)*puVar1 >> 0x20);
    DAT_ffff0000b80e8b98 = (undefined4)puVar1[1];
  }
  return 0;
}



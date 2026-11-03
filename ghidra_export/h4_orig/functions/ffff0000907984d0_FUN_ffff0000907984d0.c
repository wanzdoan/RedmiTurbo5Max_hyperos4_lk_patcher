// ffff0000907984d0  size=240  FUN_ffff0000907984d0

void FUN_ffff0000907984d0(void)

{
  uint uVar1;
  long lVar2;
  long local_60;
  long lStack_58;
  ulong local_50;
  undefined8 uStack_48;
  
  lVar2 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  lStack_58 = 0;
  do {
    uVar1 = *(uint *)((long)&DAT_ffff0000908373bc + lVar2);
    if ((uVar1 & 1) != 0) {
      if ((uVar1 >> 7 & 1) == 0) {
        local_60 = *(long *)((long)&DAT_ffff0000908373b0 + lVar2);
        lStack_58 = local_60 + (ulong)*(uint *)((long)&DAT_ffff0000908373b8 + lVar2);
        local_50 = CONCAT44(local_50._4_4_,*(undefined4 *)((long)&DAT_ffff0000908373c4 + lVar2));
        if ((uVar1 >> 1 & 1) != 0) {
          FUN_ffff00009078af1c
                    (*(undefined4 *)((long)&DAT_ffff0000908373c4 + lVar2),
                     *(undefined4 *)((long)&DAT_ffff0000908373c0 + lVar2));
        }
        FUN_ffff00009078b0fc(&local_60);
        FUN_ffff00009077e11c
                  (s__s__d___CCCI_MPU_Region_start_0x_ffff0000907ed609,
                   s_ccci_plat_apply_mpu_setting_ffff0000907d885f,0x99,local_60,
                   *(undefined4 *)((long)&DAT_ffff0000908373b8 + lVar2),lStack_58,
                   local_50 & 0xffffffff);
      }
      else {
        FUN_ffff00009077e11c
                  (s__s__d___CCCI_skip_config_region__ffff0000907fab8c,
                   s_ccci_plat_apply_mpu_setting_ffff0000907d885f,0x8c,
                   *(undefined4 *)((long)&DAT_ffff0000908373c4 + lVar2));
      }
    }
    lVar2 = lVar2 + 0x18;
  } while (lVar2 != 0x120);
  return;
}



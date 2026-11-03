// ffff000090798210  size=240  FUN_ffff000090798210

void FUN_ffff000090798210(void)

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
    uVar1 = *(uint *)((long)&DAT_ffff00009083739c + lVar2);
    if ((uVar1 & 1) != 0) {
      if ((uVar1 >> 7 & 1) == 0) {
        local_60 = *(long *)((long)&DAT_ffff000090837390 + lVar2);
        lStack_58 = local_60 + (ulong)*(uint *)((long)&DAT_ffff000090837398 + lVar2);
        local_50 = CONCAT44(local_50._4_4_,*(undefined4 *)((long)&DAT_ffff0000908373a4 + lVar2));
        if ((uVar1 >> 1 & 1) != 0) {
          FUN_ffff00009078ac5c
                    (*(undefined4 *)((long)&DAT_ffff0000908373a4 + lVar2),
                     *(undefined4 *)((long)&DAT_ffff0000908373a0 + lVar2));
        }
        FUN_ffff00009078ae3c(&local_60);
        FUN_ffff00009077de54
                  (s__s__d___CCCI_MPU_Region_start_0x_ffff0000907ed486,
                   s_ccci_plat_apply_mpu_setting_ffff0000907d87c9,0x99,local_60,
                   *(undefined4 *)((long)&DAT_ffff000090837398 + lVar2),lStack_58,
                   local_50 & 0xffffffff);
      }
      else {
        FUN_ffff00009077de54
                  (s__s__d___CCCI_skip_config_region__ffff0000907fa9f0,
                   s_ccci_plat_apply_mpu_setting_ffff0000907d87c9,0x8c,
                   *(undefined4 *)((long)&DAT_ffff0000908373a4 + lVar2));
      }
    }
    lVar2 = lVar2 + 0x18;
  } while (lVar2 != 0x120);
  return;
}



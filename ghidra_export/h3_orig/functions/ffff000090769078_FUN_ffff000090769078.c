// ffff000090769078  size=372  FUN_ffff000090769078

void FUN_ffff000090769078(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  undefined8 uStack_60;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined8 local_48;
  undefined8 uStack_40;
  undefined8 local_38;
  undefined8 uStack_30;
  undefined1 local_28;
  
  local_28 = 0;
  local_38 = 0;
  uStack_30 = 0;
  local_48 = 0;
  uStack_40 = 0;
  local_58 = 0;
  uStack_50 = 0;
  local_68 = 0;
  uStack_60 = 0;
  local_78 = 0;
  uStack_70 = 0;
  local_88 = 0;
  uStack_80 = 0;
  local_98 = 0;
  uStack_90 = 0;
  local_a8 = 0;
  uStack_a0 = 0;
  iVar1 = FUN_ffff00009076d75c(param_1,s__firmware_android_ffff000090800368);
  if ((-1 < iVar1) ||
     (((iVar1 = FUN_ffff00009076d75c(param_1,s__firmware_ffff0000907f3fe0), -1 < iVar1 ||
       (iVar1 = FUN_ffff00009076e878(param_1,0,s_firmware_ffff0000907d0b17), -1 < iVar1)) &&
      (iVar1 = FUN_ffff00009076e878(param_1,iVar1,s_android_ffff000090801dd2), -1 < iVar1)))) {
    iVar2 = FUN_ffff0000907c5750(s_androidboot_vbmeta_hash_alg_ffff0000907e9d31,&local_a8,0x80);
    if (iVar2 == 0) {
      iVar2 = FUN_ffff00009077ef88(&local_a8);
      iVar2 = FUN_ffff00009076e40c
                        (param_1,iVar1,s_vbmeta_hash_alg_ffff0000907f7008,&local_a8,iVar2 + 1);
      if (iVar2 != 0) {
        return;
      }
    }
    iVar2 = FUN_ffff0000907c5750(s_androidboot_vbmeta_size_ffff0000907fdfb4,&local_a8,0x80);
    if (iVar2 == 0) {
      iVar2 = FUN_ffff00009077ef88(&local_a8);
      iVar2 = FUN_ffff00009076e40c(param_1,iVar1,s_vbmeta_size_ffff0000907d36bb,&local_a8,iVar2 + 1)
      ;
      if (iVar2 != 0) {
        return;
      }
    }
    iVar2 = FUN_ffff0000907c5750(s_androidboot_vbmeta_digest_ffff0000907e9d4d,&local_a8,0x80);
    if (iVar2 == 0) {
      iVar2 = FUN_ffff00009077ef88(&local_a8);
      FUN_ffff00009076e40c(param_1,iVar1,s_vbmeta_digest_ffff0000907f8bc3,&local_a8,iVar2 + 1);
    }
  }
  return;
}



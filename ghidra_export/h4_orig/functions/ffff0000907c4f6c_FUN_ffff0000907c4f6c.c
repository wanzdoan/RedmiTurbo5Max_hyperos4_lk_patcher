// ffff0000907c4f6c  size=212  FUN_ffff0000907c4f6c

undefined8 FUN_ffff0000907c4f6c(long param_1)

{
  long lVar1;
  undefined8 uVar2;
  undefined8 uVar3;
  ulong uVar4;
  long local_48;
  
  local_48 = 0;
  uVar2 = FUN_ffff00009077d514(0x10);
  FUN_ffff00009077ee50(uVar2,0,0x10);
  if (*(long *)(param_1 + 0x10) != 0) {
    uVar4 = 0;
    do {
      FUN_ffff00009077e11c
                (s_vbmeta_image_name____s_ffff0000907fe80d,
                 *(undefined8 *)(*(long *)(param_1 + 8) + uVar4 * 0x20));
      lVar1 = *(long *)(param_1 + 8) + uVar4 * 0x20;
      uVar3 = FUN_ffff0000907bd9e4
                        (*(undefined8 *)(lVar1 + 8),*(undefined8 *)(lVar1 + 0x10),
                         s_com_android_build_init_boot_sku_ffff0000907d560d,0,&local_48);
      if (local_48 != 0) {
        FUN_ffff00009077eb20(uVar2,uVar3);
        return uVar2;
      }
      uVar4 = (ulong)((int)uVar4 + 1);
      local_48 = 0;
    } while (uVar4 < *(ulong *)(param_1 + 0x10));
  }
  return uVar2;
}



// ffff0000907c4a64  size=192  FUN_ffff0000907c4a64

undefined8 FUN_ffff0000907c4a64(long param_1,undefined8 param_2)

{
  long lVar1;
  undefined8 uVar2;
  ulong uVar3;
  long local_48;
  
  if (*(long *)(param_1 + 0x10) != 0) {
    uVar3 = 0;
    do {
      local_48 = 0;
      FUN_ffff00009077de54
                (s_vbmeta_image_name____s_ffff0000907fe665,
                 *(undefined8 *)(*(long *)(param_1 + 8) + uVar3 * 0x20));
      lVar1 = *(long *)(param_1 + 8) + uVar3 * 0x20;
      uVar2 = FUN_ffff0000907bd370
                        (*(undefined8 *)(lVar1 + 8),*(undefined8 *)(lVar1 + 0x10),
                         s_com_android_build_vbmeta_miep_ac_ffff0000907e3e9e,0,&local_48);
      if (local_48 != 0) {
        FUN_ffff00009077e858(param_2,uVar2);
        return 0;
      }
      uVar3 = (ulong)((int)uVar3 + 1);
    } while (uVar3 < *(ulong *)(param_1 + 0x10));
  }
  return 0;
}



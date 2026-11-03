
undefined8 FUN_ffff0000907c50d8(long param_1,undefined8 param_2)

{
  long lVar1;
  undefined8 uVar2;
  ulong uVar3;
  long local_48;
  
  if (*(long *)(param_1 + 0x10) != 0) {
    uVar3 = 0;
    do {
      local_48 = 0;
      FUN_ffff00009077e11c
                (s_vbmeta_image_name____s_ffff0000907fe80d,
                 *(undefined8 *)(*(long *)(param_1 + 8) + uVar3 * 0x20));
      lVar1 = *(long *)(param_1 + 8) + uVar3 * 0x20;
      uVar2 = FUN_ffff0000907bd9e4
                        (*(undefined8 *)(lVar1 + 8),*(undefined8 *)(lVar1 + 0x10),
                         s_com_android_build_vbmeta_miep_ac_ffff0000907e3f6f,0,&local_48);
      if (local_48 != 0) {
        FUN_ffff00009077eb20(param_2,uVar2);
        return 0;
      }
      uVar3 = (ulong)((int)uVar3 + 1);
    } while (uVar3 < *(ulong *)(param_1 + 0x10));
  }
  return 0;
}



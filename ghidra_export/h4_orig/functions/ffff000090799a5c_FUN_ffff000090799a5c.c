// ffff000090799a5c  size=464  FUN_ffff000090799a5c

int FUN_ffff000090799a5c(long param_1,int param_2,long param_3,ulong param_4)

{
  int iVar1;
  long lVar2;
  ulong uVar3;
  long lVar4;
  ulong uVar5;
  long lVar6;
  ulong local_48;
  
  local_48 = 0;
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000907991f0
              (s_hmac____NULL_ffff0000907d21cd,s_platform_mediatek_mt6991_sboot_c_ffff0000907da059,
               0x24);
  }
  if (param_3 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000907991f0
              (s_key____NULL_ffff0000907ea34b,s_platform_mediatek_mt6991_sboot_c_ffff0000907da059,
               0x25);
  }
  iVar1 = FUN_ffff0000907992b0(param_2);
  if (iVar1 != 0) {
    return iVar1;
  }
  *(int *)(param_1 + 0x70) = param_2;
  if (param_4 == 0) {
    return 3;
  }
  lVar2 = *(long *)((long)param_2 * 8 + -0xffff6f6f5588);
  uVar5 = *(ulong *)(lVar2 + 0x10);
  lVar2 = FUN_ffff00009077d514(*(undefined8 *)(lVar2 + 0x18));
  if (lVar2 == 0) {
    return 0xd;
  }
  lVar6 = (long)param_2;
  uVar3 = *(ulong *)(*(long *)(lVar6 * 8 + -0xffff6f6f5588) + 0x18);
  if (uVar3 < 0x91) {
    if (uVar3 < param_4) {
      local_48 = uVar3;
      iVar1 = FUN_ffff000090799368(param_2,param_3,param_4,param_1 + 0xe8,&local_48);
      if (iVar1 != 0) goto LAB_ffff000090799be0;
    }
    else {
      FUN_ffff00009077eb20(param_1 + 0xe8,param_3,param_4);
      uVar5 = param_4;
    }
    lVar4 = *(long *)(lVar6 * 8 + -0xffff6f6f5588);
    uVar3 = *(ulong *)(lVar4 + 0x18);
    if (uVar5 <= uVar3 && uVar3 - uVar5 != 0) {
      FUN_ffff00009079c0d4(param_1 + uVar5 + 0xe8,uVar3 - uVar5);
      lVar4 = *(long *)(lVar6 * 8 + -0xffff6f6f5588);
      uVar3 = *(ulong *)(lVar4 + 0x18);
    }
    if (uVar3 != 0) {
      uVar5 = 0;
      do {
        *(byte *)(lVar2 + uVar5) = *(byte *)(param_1 + 0xe8 + uVar5) ^ 0x36;
        uVar5 = uVar5 + 1;
      } while (uVar5 < *(ulong *)(lVar4 + 0x18));
    }
    iVar1 = (**(code **)(lVar4 + 0xa8))(param_1);
    if (iVar1 == 0) {
      lVar4 = *(long *)(lVar6 * 8 + -0xffff6f6f5588);
      iVar1 = (**(code **)(lVar4 + 0xb0))(param_1,lVar2,*(undefined8 *)(lVar4 + 0x18));
    }
  }
  else {
    iVar1 = 6;
  }
LAB_ffff000090799be0:
  FUN_ffff00009079c0d4(lVar2,*(undefined8 *)(*(long *)(lVar6 * 8 + -0xffff6f6f5588) + 0x18));
  thunk_FUN_ffff0000907c314c(lVar2);
  return iVar1;
}



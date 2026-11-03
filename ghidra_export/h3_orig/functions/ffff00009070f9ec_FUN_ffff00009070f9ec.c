// ffff00009070f9ec  size=276  FUN_ffff00009070f9ec

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009070f9ec(long param_1,uint param_2,undefined4 param_3)

{
  long *plVar1;
  undefined8 uVar2;
  int iVar3;
  int iVar4;
  char *pcVar5;
  uint uVar6;
  
  iVar3 = FUN_ffff0000907098fc();
  if (iVar3 == 3) {
LAB_ffff00009070fac4:
    FUN_ffff00009077de54(s_ERROR_ERROR_ERROR_______ffff0000907f2570);
    FUN_ffff000090709910(3);
    iVar3 = -1;
  }
  else {
    iVar3 = 0;
    plVar1 = _DAT_ffff00009083e070;
    for (; _DAT_ffff00009083e070 = plVar1, param_2 != 0; param_2 = param_2 - uVar6) {
      *plVar1 = param_1;
      uVar2 = _DAT_ffff00009083e0b8;
      uVar6 = param_2;
      if (0x3fff < param_2) {
        uVar6 = 0x4000;
      }
      plVar1[2] = (long)FUN_ffff00009070fb14;
      *(uint *)(plVar1 + 1) = uVar6;
      iVar4 = FUN_ffff00009078b6a8(uVar2);
      if (iVar4 < 0) {
        pcVar5 = s__s___queue_failed_ffff0000907dd8de;
LAB_ffff00009070fab8:
        FUN_ffff00009077de54(pcVar5,s_usb_write_with_timeout_ffff0000907f3e7b);
        goto LAB_ffff00009070fac4;
      }
      iVar4 = FUN_ffff000090717454(0xffff00009083e080,param_3);
      if (iVar4 != 0) goto LAB_ffff00009070fac4;
      if (_DAT_ffff00009083e0b0 < 0) {
        pcVar5 = s__s___transaction_failed_ffff0000907e0aaf;
        goto LAB_ffff00009070fab8;
      }
      uVar6 = *(uint *)(_DAT_ffff00009083e070 + 1);
      iVar3 = uVar6 + iVar3;
      param_1 = param_1 + (ulong)uVar6;
      plVar1 = _DAT_ffff00009083e070;
    }
  }
  return iVar3;
}



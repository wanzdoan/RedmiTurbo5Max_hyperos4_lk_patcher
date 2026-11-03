// ffff00009070fa64  size=276  FUN_ffff00009070fa64

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009070fa64(long param_1,uint param_2,undefined4 param_3)

{
  long *plVar1;
  undefined8 uVar2;
  int iVar3;
  int iVar4;
  char *pcVar5;
  uint uVar6;
  
  iVar3 = FUN_ffff000090709888();
  if (iVar3 == 3) {
LAB_ffff00009070fb3c:
    FUN_ffff00009077e11c(s_ERROR_ERROR_ERROR_______ffff0000907f26fc);
    FUN_ffff00009070989c(3);
    iVar3 = -1;
  }
  else {
    iVar3 = 0;
    plVar1 = _DAT_ffff00009083e0a8;
    for (; _DAT_ffff00009083e0a8 = plVar1, param_2 != 0; param_2 = param_2 - uVar6) {
      *plVar1 = param_1;
      uVar2 = _DAT_ffff00009083e0f0;
      uVar6 = param_2;
      if (0x3fff < param_2) {
        uVar6 = 0x4000;
      }
      plVar1[2] = (long)FUN_ffff00009070fb8c;
      *(uint *)(plVar1 + 1) = uVar6;
      iVar4 = FUN_ffff00009078b968(uVar2);
      if (iVar4 < 0) {
        pcVar5 = s__s___queue_failed_ffff0000907dd981;
LAB_ffff00009070fb30:
        FUN_ffff00009077e11c(pcVar5,s_usb_write_with_timeout_ffff0000907f4007);
        goto LAB_ffff00009070fb3c;
      }
      iVar4 = FUN_ffff000090717598(0xffff00009083e0b8,param_3);
      if (iVar4 != 0) goto LAB_ffff00009070fb3c;
      if (_DAT_ffff00009083e0e8 < 0) {
        pcVar5 = s__s___transaction_failed_ffff0000907e0b52;
        goto LAB_ffff00009070fb30;
      }
      uVar6 = *(uint *)(_DAT_ffff00009083e0a8 + 1);
      iVar3 = uVar6 + iVar3;
      param_1 = param_1 + (ulong)uVar6;
      plVar1 = _DAT_ffff00009083e0a8;
    }
  }
  return iVar3;
}



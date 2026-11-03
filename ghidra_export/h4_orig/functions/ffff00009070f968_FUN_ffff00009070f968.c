// ffff00009070f968  size=236  FUN_ffff00009070f968

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009070f968(long param_1,uint param_2)

{
  uint uVar1;
  long *plVar2;
  undefined8 uVar3;
  int iVar4;
  int iVar5;
  uint uVar6;
  
  iVar4 = FUN_ffff000090709888();
  if (iVar4 == 3) {
LAB_ffff00009070f99c:
    FUN_ffff00009070989c(3);
    iVar4 = -1;
  }
  else {
    iVar4 = 0;
    plVar2 = _DAT_ffff00009083e0a8;
    for (; _DAT_ffff00009083e0a8 = plVar2, param_2 != 0; param_2 = param_2 - uVar1) {
      *plVar2 = param_1;
      uVar3 = _DAT_ffff00009083e0b0;
      uVar6 = param_2;
      if (0x3fff < param_2) {
        uVar6 = 0x4000;
      }
      plVar2[2] = (long)FUN_ffff00009070fb8c;
      *(uint *)(plVar2 + 1) = uVar6;
      iVar5 = FUN_ffff00009078b968(uVar3);
      if (((iVar5 < 0) || (iVar5 = FUN_ffff000090717598(0xffff00009083e0b8,0xffffffff), iVar5 != 0))
         || (_DAT_ffff00009083e0e8 < 0)) goto LAB_ffff00009070f99c;
      uVar1 = *(uint *)(_DAT_ffff00009083e0a8 + 1);
      iVar4 = uVar1 + iVar4;
      if (uVar1 != uVar6) {
        return iVar4;
      }
      param_1 = param_1 + (ulong)uVar1;
      plVar2 = _DAT_ffff00009083e0a8;
    }
  }
  return iVar4;
}



// ffff00009070f8f0  size=236  FUN_ffff00009070f8f0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009070f8f0(long param_1,uint param_2)

{
  uint uVar1;
  long *plVar2;
  undefined8 uVar3;
  int iVar4;
  int iVar5;
  uint uVar6;
  
  iVar4 = FUN_ffff0000907098fc();
  if (iVar4 == 3) {
LAB_ffff00009070f924:
    FUN_ffff000090709910(3);
    iVar4 = -1;
  }
  else {
    iVar4 = 0;
    plVar2 = _DAT_ffff00009083e070;
    for (; _DAT_ffff00009083e070 = plVar2, param_2 != 0; param_2 = param_2 - uVar1) {
      *plVar2 = param_1;
      uVar3 = _DAT_ffff00009083e078;
      uVar6 = param_2;
      if (0x3fff < param_2) {
        uVar6 = 0x4000;
      }
      plVar2[2] = (long)FUN_ffff00009070fb14;
      *(uint *)(plVar2 + 1) = uVar6;
      iVar5 = FUN_ffff00009078b6a8(uVar3);
      if (((iVar5 < 0) || (iVar5 = FUN_ffff000090717454(0xffff00009083e080,0xffffffff), iVar5 != 0))
         || (_DAT_ffff00009083e0b0 < 0)) goto LAB_ffff00009070f924;
      uVar1 = *(uint *)(_DAT_ffff00009083e070 + 1);
      iVar4 = uVar1 + iVar4;
      if (uVar1 != uVar6) {
        return iVar4;
      }
      param_1 = param_1 + (ulong)uVar1;
      plVar2 = _DAT_ffff00009083e070;
    }
  }
  return iVar4;
}



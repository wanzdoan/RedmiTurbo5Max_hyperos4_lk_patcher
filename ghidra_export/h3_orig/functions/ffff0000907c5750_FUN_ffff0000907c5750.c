// ffff0000907c5750  size=260  FUN_ffff0000907c5750

undefined4 FUN_ffff0000907c5750(long param_1,long param_2,uint param_3)

{
  char *pcVar1;
  int iVar2;
  uint uVar3;
  ulong uVar4;
  ulong uVar5;
  undefined4 uVar6;
  long lVar7;
  
  uVar6 = 0x108002;
  if (param_1 == 0) {
    uVar6 = 0x108001;
  }
  else if (param_2 != 0) {
    lVar7 = 0;
    do {
      pcVar1 = (char *)(lVar7 + -0xffff6f6f26e8);
      if (*pcVar1 == '\0') break;
      uVar4 = FUN_ffff00009077ef88(pcVar1);
      uVar5 = FUN_ffff00009077ef88(param_1);
      if ((uVar5 == (uVar4 & 0xffffffff)) &&
         (iVar2 = FUN_ffff00009077e820(pcVar1,param_1,uVar5), iVar2 == 0)) {
        uVar3 = FUN_ffff00009077ef88(lVar7 + -0xffff6f6f26a7);
        if (uVar3 == 0) {
          return 0x108003;
        }
        if (param_3 < uVar3 + 1) {
          return 0x108002;
        }
        FUN_ffff00009077e858(param_2,lVar7 + -0xffff6f6f26a7,(ulong)uVar3);
        *(undefined1 *)(param_2 + (ulong)uVar3) = 0;
        return 0;
      }
      lVar7 = lVar7 + 0xc2;
    } while (lVar7 != 0xc20);
    uVar6 = 0x108000;
  }
  return uVar6;
}




undefined4 FUN_ffff0000907c5dc4(long param_1,long param_2,uint param_3)

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
      pcVar1 = &DAT_ffff00009090d9d8 + lVar7;
      if (*pcVar1 == '\0') break;
      uVar4 = FUN_ffff00009077f250(pcVar1);
      uVar5 = FUN_ffff00009077f250(param_1);
      if ((uVar5 == (uVar4 & 0xffffffff)) &&
         (iVar2 = FUN_ffff00009077eae8(pcVar1,param_1,uVar5), iVar2 == 0)) {
        uVar3 = FUN_ffff00009077f250(&DAT_ffff00009090da19 + lVar7);
        if (uVar3 == 0) {
          return 0x108003;
        }
        if (param_3 < uVar3 + 1) {
          return 0x108002;
        }
        FUN_ffff00009077eb20(param_2,&DAT_ffff00009090da19 + lVar7,(ulong)uVar3);
        *(undefined1 *)(param_2 + (ulong)uVar3) = 0;
        return 0;
      }
      lVar7 = lVar7 + 0xc2;
    } while (lVar7 != 0xc20);
    uVar6 = 0x108000;
  }
  return uVar6;
}



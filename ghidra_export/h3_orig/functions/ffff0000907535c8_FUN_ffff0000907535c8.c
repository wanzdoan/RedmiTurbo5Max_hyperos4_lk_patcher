// ffff0000907535c8  size=276  FUN_ffff0000907535c8

int FUN_ffff0000907535c8(int param_1,uint param_2,int param_3)

{
  uint uVar1;
  long lVar2;
  uint *puVar3;
  int iVar4;
  
  if ((param_1 == 0x2d) || (param_1 == 0x2b)) {
    puVar3 = (uint *)&DAT_ffff00003249000c;
  }
  else {
    puVar3 = (uint *)0x0;
  }
  if (param_3 < 1) {
    do {
    } while ((*puVar3 & param_2) == 0);
    iVar4 = 1;
  }
  else {
    iVar4 = param_3 * -10 + 1;
    do {
      if ((*puVar3 & param_2) != 0) {
        *puVar3 = ~*puVar3;
        if (iVar4 != 0) {
          return -iVar4;
        }
        break;
      }
      FUN_ffff00009077cbd8(100);
      iVar4 = iVar4 + 1;
    } while (iVar4 != 1);
    uVar1 = 0;
    if (param_1 != 0x2b) {
      uVar1 = (uint)(param_1 != 0x2d);
    }
    lVar2 = 1;
    if (param_1 != 0x2b) {
      lVar2 = 2;
    }
    lVar2 = lVar2 - (ulong)uVar1;
    do {
      lVar2 = lVar2 + -1;
    } while (lVar2 != 0);
    iVar4 = 0;
  }
  return iVar4;
}



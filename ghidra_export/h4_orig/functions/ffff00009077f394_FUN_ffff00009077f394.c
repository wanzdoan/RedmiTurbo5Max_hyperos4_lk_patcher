// ffff00009077f394  size=160  FUN_ffff00009077f394

int FUN_ffff00009077f394(byte *param_1,byte *param_2,long param_3)

{
  byte bVar1;
  byte bVar2;
  uint uVar3;
  uint uVar4;
  
  if (param_3 == 0) {
    uVar3 = 0;
    uVar4 = 0;
  }
  else {
    do {
      bVar1 = *param_1;
      uVar3 = (uint)bVar1;
      bVar2 = *param_2;
      uVar4 = (uint)bVar2;
      if ((bVar1 == 0) || (bVar2 == 0)) break;
      uVar4 = uVar3;
      if (bVar1 != bVar2) {
        bVar1 = FUN_ffff00009077db9c(bVar1);
        uVar3 = (uint)bVar1;
        bVar2 = FUN_ffff00009077db9c(bVar2);
        uVar4 = (uint)bVar2;
        if (bVar2 != bVar1) break;
      }
      param_3 = param_3 + -1;
      param_2 = param_2 + 1;
      param_1 = param_1 + 1;
    } while (param_3 != 0);
  }
  return uVar3 - uVar4;
}



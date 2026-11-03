// ffff000090780e98  size=188  FUN_ffff000090780e98

int FUN_ffff000090780e98(undefined8 param_1)

{
  long lVar1;
  int iVar2;
  int iVar3;
  undefined1 auStack_240 [512];
  
  lVar1 = FUN_ffff00009077bc50();
  if (lVar1 == 0) {
    iVar2 = 0;
  }
  else {
    FUN_ffff00009077eb88(auStack_240,0,0x200);
    iVar2 = 0;
    iVar3 = 0;
    do {
      FUN_ffff00009077f38c(auStack_240,s__sp_d_ffff0000907d84d2,param_1,iVar3);
      lVar1 = FUN_ffff00009077bc50(auStack_240);
      if (lVar1 != 0) {
        FUN_ffff00009077c798();
        FUN_ffff00009077bce4(lVar1);
        FUN_ffff00009077cba8(lVar1);
        iVar2 = iVar2 + 1;
      }
      iVar3 = iVar3 + 1;
    } while (iVar3 != 0xc0);
  }
  return iVar2;
}



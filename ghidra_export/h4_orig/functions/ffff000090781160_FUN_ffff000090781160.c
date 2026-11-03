// ffff000090781160  size=188  FUN_ffff000090781160

int FUN_ffff000090781160(undefined8 param_1)

{
  long lVar1;
  int iVar2;
  int iVar3;
  undefined1 auStack_240 [512];
  
  lVar1 = FUN_ffff00009077bf18();
  if (lVar1 == 0) {
    iVar2 = 0;
  }
  else {
    FUN_ffff00009077ee50(auStack_240,0,0x200);
    iVar2 = 0;
    iVar3 = 0;
    do {
      FUN_ffff00009077f654(auStack_240,s__sp_d_ffff0000907d8568,param_1,iVar3);
      lVar1 = FUN_ffff00009077bf18(auStack_240);
      if (lVar1 != 0) {
        FUN_ffff00009077ca60();
        FUN_ffff00009077bfac(lVar1);
        FUN_ffff00009077ce70(lVar1);
        iVar2 = iVar2 + 1;
      }
      iVar3 = iVar3 + 1;
    } while (iVar3 != 0xc0);
  }
  return iVar2;
}


